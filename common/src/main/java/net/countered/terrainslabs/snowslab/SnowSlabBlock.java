package net.countered.terrainslabs.snowslab;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.util.RandomSource;

import org.jetbrains.annotations.Nullable;

/**
 * Standalone TEST block: a single cell containing a bottom slab (lower 8px) plus
 * 1-4 snow layers (upper 8px, 2px each; 4 = full cell).
 *
 * Why this exists: the mod's existing snow-on-slab approach renders a vanilla snow
 * block shifted down half a block, which clips/breaks once snow exceeds 4 layers
 * (it was only ever designed to fit the 8px above the slab). This block makes the
 * snow physically live in the upper 8px and stops at 4 layers, so it composes
 * cleanly with vanilla snow stacked in the cell above instead of floating.
 *
 * This version is deliberately ISOLATED — it is its own block, does not touch the
 * mod's offset mixins, and uses a fixed bottom slab for testing. The BlockEntity
 * already stores an arbitrary slab BlockState so "any slab" support can be enabled
 * later without reworking this class.
 *
 * Rendering of the real bottom-slab texture is intentionally NOT implemented yet:
 * the blockstate JSON points the bottom at a plain placeholder model. All LOGIC
 * (collision, drops, melt, accumulation, shovel, sound) is complete and testable.
 */
public class SnowSlabBlock extends Block implements EntityBlock {

    /**
     * Snow layers in the UPPER half: 1-4, each 2px. 4 fills the cell's top 8px.
     *
     * NOTE: we define our OWN 1-4 property rather than reusing vanilla
     * BlockStateProperties.LAYERS (which is 1-8). Reusing the vanilla 1-8 property
     * makes Minecraft generate states for layers=5..8 at registration time, which
     * then index past our 1-4 SHAPES array and crash on startup.
     */
    public static final int MIN_LAYERS = 1;
    public static final int MAX_LAYERS = 4;
    public static final IntegerProperty LAYERS = IntegerProperty.create("layers", MIN_LAYERS, MAX_LAYERS);

    // Precomputed shapes: bottom slab (0..8) + snow up to (8 + 2*layers).
    // Visual/outline shapes: bottom slab (0..8) + snow up to (8 + 2*layers).
    private static final VoxelShape[] SHAPES = new VoxelShape[MAX_LAYERS + 1];
    // Collision shapes: same slab base, but the snow collides 2px LOWER than it
    // looks, so you can always step onto it like a normal slab. At 1 layer the
    // collision is just the bare slab (8px); higher layers stay 2px under visual.
    private static final VoxelShape[] COLLISION_SHAPES = new VoxelShape[MAX_LAYERS + 1];
    static {
        VoxelShape slab = Block.box(0, 0, 0, 16, 8, 16);
        for (int layers = MIN_LAYERS; layers <= MAX_LAYERS; layers++) {
            int visualTop = 8 + layers * 2;            // what you see: 10/12/14/16

            VoxelShape snowVisual = Block.box(0, 8, 0, 16, visualTop, 16);
            SHAPES[layers] = Shapes.or(slab, snowVisual);

            // Collision: the FIRST snow layer adds no step height (so 1 layer steps
            // exactly like a bare slab at 8px); each layer beyond the first adds 2px.
            // -> collisionTop = 8 + (layers - 1) * 2  => 8/10/12/14. This matches
            // vanilla snow step heights (8-layer snow tops out at 14, not 16). Snow
            // survival on a full (layer 4) slab is handled via the
            // #minecraft:snow_layer_can_survive_on tag instead of a full-height box.
            int collisionTop = 8 + (layers - 1) * 2;
            if (collisionTop > 8) {
                VoxelShape snowCollision = Block.box(0, 8, 0, 16, collisionTop, 16);
                COLLISION_SHAPES[layers] = Shapes.or(slab, snowCollision);
            } else {
                COLLISION_SHAPES[layers] = slab; // 1 layer: bare slab -> steppable
            }
        }
    }

    public SnowSlabBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(LAYERS, MIN_LAYERS));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LAYERS);
    }

    // --- Shape: bottom slab always solid, snow adds height on top ---

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES[state.getValue(LAYERS)];
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return COLLISION_SHAPES[state.getValue(LAYERS)];
    }

    @Override
    protected VoxelShape getOcclusionShape(BlockState state, BlockGetter level, BlockPos pos) {
        // The solid bottom slab can occlude downward; keep it simple and non-occluding
        // sideways so neighbouring snow/blocks render correctly.
        return SHAPES[state.getValue(LAYERS)];
    }

    // --- Snow sounds: behave like snow when walked on / placed / broken ---

    @Override
    public SoundType getSoundType(BlockState state) {
        return SoundType.SNOW;
    }

    // --- BlockEntity ---

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SnowSlabBlockEntity(pos, state);
    }

    // --- Interaction: shovel strips ALL snow at once, leaving the bare slab ---

    /**
     * Right-click with a shovel: strip ALL snow at once and leave the bare slab.
     *
     * In 1.21 the item-on-block path is useItemOn(...). We check for a shovel here
     * so shovelling works regardless of which slab is underneath.
     */
    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hit) {
        if (stack.getItem() instanceof ShovelItem) {
            ItemInteractionResult result = onShovelUsed(state, level, pos, player);
            if (result.consumesAction() && !player.getAbilities().instabuild) {
                stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
            }
            return result;
        }
        if (stack.is(Items.SNOW)) {
            return tryAddLayer(state, level, pos, player, stack);
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    /** Strip all snow, drop snowballs, revert to the stored slab (placement retained). */
    public ItemInteractionResult onShovelUsed(BlockState state, Level level, BlockPos pos, Player player) {
        if (!level.isClientSide) {
            int layers = state.getValue(LAYERS);
            // Drop the snow as snowballs (not in creative), then replace IN PLACE
            // with the bare slab. This is the same path melt uses, so the slab is
            // never "broken".
            if (!player.getAbilities().instabuild) {
                popResource(level, pos, new ItemStack(Items.SNOWBALL, layers));
            }
            revertToSlab(level, pos);
            level.playSound(null, pos, SoundType.SNOW.getBreakSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

    /**
     * Region detection: is the player aiming at the SNOW portion (upper part) of this
     * snow slab, versus the base slab (lower part)? Computes the hit point of the
     * player's view ray against this block and compares to the slab/snow split (y=8,
     * i.e. 0.5 within the block cell). Used to make break/pick behave per-region.
     *
     * Returns true if aiming at snow, false if aiming at the base slab (or if no clean
     * hit on this block, in which case callers treat it as the slab region).
     */
    public static boolean isAimingAtSnowRegion(Level level, BlockPos pos, BlockState state, Player player) {
        Vec3 eye = player.getEyePosition(1.0F);
        Vec3 look = player.getViewVector(1.0F);
        double reach = 6.0; // generous; the clip stops at the block anyway
        Vec3 end = eye.add(look.x * reach, look.y * reach, look.z * reach);

        // Clip against THIS block's outline shape to get the precise hit point.
        VoxelShape shape = SHAPES[state.getValue(LAYERS)];
        BlockHitResult hit = shape.clip(eye, end, pos);
        if (hit == null) {
            return false; // no clean hit -> treat as base slab (whole-block behavior)
        }
        double localY = hit.getLocation().y - pos.getY();
        // Snow sits above y=8 (0.5). At/below the split = base slab.
        return localY > 0.5;
    }

    /** Replace this block with the stored bottom slab, keeping it placed (no drop). */
    private void revertToSlab(Level level, BlockPos pos) {
        BlockState bottom = getStoredSlab(level, pos);
        level.setBlock(pos, bottom, Block.UPDATE_ALL);
    }

    /**
     * Called by the break-path mixin when a player left-click "breaks" this block.
     *
     * Instead of destroying the whole block, we strip ALL snow (dropping snowballs)
     * and leave the bare slab placed. Returns true if we handled it (and the caller
     * should CANCEL the real destruction); false if there's nothing to do and the
     * block should break normally.
     *
     * Static so the mixin can call it without touching block internals.
     */
    public static boolean clearSnowOnBreak(Level level, BlockPos pos, BlockState state, Player player) {
        if (!(state.getBlock() instanceof SnowSlabBlock block)) {
            return false;
        }
        if (!level.isClientSide) {
            int layers = state.getValue(LAYERS);
            if (!player.getAbilities().instabuild) {
                if (hasSilkTouch(level, player.getMainHandItem())) {
                    popResource(level, pos, new ItemStack(Items.SNOW, layers));
                } else {
                    popResource(level, pos, new ItemStack(Items.SNOWBALL, layers));
                }
            }
            block.revertToSlab(level, pos);
            level.playSound(null, pos, SoundType.SNOW.getBreakSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
        }
        return true; // handled -> cancel the real break
    }

    private ItemInteractionResult tryAddLayer(BlockState state, Level level, BlockPos pos, Player player, ItemStack held) {
        int layers = state.getValue(LAYERS);
        if (layers >= MAX_LAYERS) {
            // Slab snow is full. Place or grow a real vanilla snow layer in the cell
            // ABOVE directly (relying on SKIP/vanilla item placement proved unreliable).
            BlockPos above = pos.above();
            BlockState aboveState = level.getBlockState(above);

            if (aboveState.isAir()) {
                if (!level.isClientSide) {
                    level.setBlock(above, Blocks.SNOW.defaultBlockState(), Block.UPDATE_ALL);
                    level.playSound(null, above, SoundType.SNOW.getPlaceSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
                    if (!player.getAbilities().instabuild) held.shrink(1);
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
            if (aboveState.is(Blocks.SNOW)) {
                int snowLayers = aboveState.getValue(net.minecraft.world.level.block.SnowLayerBlock.LAYERS);
                if (snowLayers < 8) {
                    if (!level.isClientSide) {
                        level.setBlock(above, aboveState.setValue(
                                net.minecraft.world.level.block.SnowLayerBlock.LAYERS, snowLayers + 1),
                                Block.UPDATE_ALL);
                        level.playSound(null, above, SoundType.SNOW.getPlaceSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
                        if (!player.getAbilities().instabuild) held.shrink(1);
                    }
                    return ItemInteractionResult.sidedSuccess(level.isClientSide);
                }
            }
            // Above is occupied by something else, or snow is already at 8.
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (!level.isClientSide) {
            level.setBlock(pos, state.setValue(LAYERS, layers + 1), Block.UPDATE_ALL);
            level.playSound(null, pos, SoundType.SNOW.getPlaceSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
            if (!player.getAbilities().instabuild) {
                held.shrink(1);
            }
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

    // --- Melt + weather accumulation, both capped at MAX_LAYERS ---

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // Match vanilla SnowLayerBlock exactly: when block light exceeds 11, ALL the
        // snow melts at once (not layer-by-layer), with no drops. Biome warmth is NOT
        // a trigger here — vanilla snow melts purely from block light.
        if (level.getBrightness(LightLayer.BLOCK, pos) > 11) {
            revertToSlab(level, pos);
        }
    }

    // --- Weather forming (vanilla-accurate: snow forms at 1 layer only) ---
    //
    // Vanilla snowfall only ever creates a SINGLE snow layer; it never deepens from
    // weather. So weather's job here is: turn a bare bottom slab into this block at
    // 1 layer, storing the original slab. Deeper layers (2-4) come only from manual
    // placement (or worldgen later), never from snowfall.
    //
    // Called by MixinServerLevel (ServerLevel#tickIceAndSnow). Returns true if it
    // formed snow (and the caller should cancel vanilla's own snow placement).
    /**
     * Worldgen variant of formOnSlab: converts a just-placed bottom slab into a snow
     * slab (1 layer) during generation, using WorldGenLevel. Called by SlabFeature in
     * cold biomes so slabs generate already snow-capped (like vanilla ground snow).
     */
    public static boolean formOnSlabWorldgen(net.minecraft.world.level.WorldGenLevel level,
                                             BlockPos slabPos, BlockState slabState) {
        BlockState snowSlab = SnowSlabRegistry.SNOW_SLAB.get().defaultBlockState()
                .setValue(LAYERS, MIN_LAYERS);
        level.setBlock(slabPos, snowSlab, Block.UPDATE_ALL);
        if (level.getBlockEntity(slabPos) instanceof SnowSlabBlockEntity be) {
            be.setBottomSlab(slabState);
        }
        return true;
    }

    // --- Worldgen neighbor-snow helpers (shared by SlabFeature and the late
    //     freeze-feature pass) --------------------------------------------------

    /** A generated bottom slab eligible to be snow-capped: it's a slab, a bottom
     *  slab, not already our snow slab, with air directly above. */
    public static boolean isWorldgenSnowCapCandidate(net.minecraft.world.level.WorldGenLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof SlabBlock)) return false;
        if (state.is(SnowSlabRegistry.SNOW_SLAB.get())) return false;
        var slabTypeProp = net.minecraft.world.level.block.state.properties.BlockStateProperties.SLAB_TYPE;
        if (state.hasProperty(slabTypeProp)
                && state.getValue(slabTypeProp) != net.minecraft.world.level.block.state.properties.SlabType.BOTTOM) {
            return false;
        }
        return level.getBlockState(pos.above()).isAir();
    }

    /** True if any of the 8 horizontally-adjacent columns (4 cardinal + 4 diagonal),
     *  at the slab's Y, one above, or one below, contains a snowy FULL block. Slabs are
     *  ignored to avoid slab->slab feedback. */
    public static boolean hasSnowyFullBlockNeighbor(net.minecraft.world.level.WorldGenLevel level, BlockPos pos) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                if (dx == 0 && dz == 0) continue; // skip the slab's own column
                BlockPos side = pos.offset(dx, 0, dz);
                if (isSnowyFullBlock(level, side)
                        || isSnowyFullBlock(level, side.above())
                        || isSnowyFullBlock(level, side.below())) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isSnowyFullBlock(net.minecraft.world.level.WorldGenLevel level, BlockPos p) {
        BlockState state = level.getBlockState(p);
        if (state.getBlock() instanceof SlabBlock) return false;
        if (state.isAir()) return false;
        if (!state.isCollisionShapeFullBlock(net.minecraft.world.level.EmptyBlockGetter.INSTANCE, BlockPos.ZERO)) return false;
        if (state.is(Blocks.SNOW) || state.is(Blocks.SNOW_BLOCK)) return true;
        if (state.hasProperty(net.minecraft.world.level.block.state.properties.BlockStateProperties.SNOWY)
                && state.getValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.SNOWY)) {
            return true;
        }
        return level.getBlockState(p.above()).is(Blocks.SNOW);
    }

    public static boolean formOnSlab(ServerLevel level, BlockPos slabPos, BlockState slabState) {
        // Build our block at 1 layer and place it where the slab was.
        BlockState snowSlab = SnowSlabRegistry.SNOW_SLAB.get().defaultBlockState()
                .setValue(LAYERS, MIN_LAYERS);
        level.setBlock(slabPos, snowSlab, Block.UPDATE_ALL);

        // Store the original slab in the block entity. The BE's client sync triggers
        // a re-bake (see SnowSlabBlockEntity), so the model shows the stored slab
        // immediately instead of the placeholder.
        if (level.getBlockEntity(slabPos) instanceof SnowSlabBlockEntity be) {
            be.setBottomSlab(slabState);
        }
        return true;
    }

    // --- Shared eligibility (used by BOTH the heightmap mixin and the backstop) ---

    public static final TagKey<Block> ICE_SLABS = TagKey.create(
            Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("terrain_slabs", "ice_slabs"));
    public static final TagKey<Block> PATH_SLABS = TagKey.create(
            Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("terrain_slabs", "path_slabs"));

    /**
     * Single source of truth for "should this bottom slab receive snow right now?".
     * slabPos is the slab; snowPos is the air block above it (where snow lands).
     * Both the heightmap-driven mixin (MixinServerLevel) and the chunk-scan backstop
     * (MixinTickChunkSnowScan) call this, so they can never disagree.
     */
    public static boolean canFormSnowOn(ServerLevel level, BlockPos slabPos, BlockPos snowPos) {
        if (!level.isRaining()) return false;
        if (!level.canSeeSky(snowPos)) return false;

        BlockState slabState = level.getBlockState(slabPos);
        if (!(slabState.getBlock() instanceof SlabBlock)) return false;
        if (!slabState.is(BlockTags.SLABS)) return false;
        if (slabState.is(ICE_SLABS) || slabState.is(PATH_SLABS)) return false;
        if (!slabState.hasProperty(SlabBlock.TYPE) || slabState.getValue(SlabBlock.TYPE) != SlabType.BOTTOM) return false;

        Biome biome = level.getBiome(slabPos).value();
        if (biome.getPrecipitationAt(slabPos) != Biome.Precipitation.SNOW) return false;

        // The snow position must be empty air so we don't overwrite anything.
        if (!level.getBlockState(snowPos).isAir()) return false;
        return true;
    }

    /** Check eligibility at slabPos (snowPos = above) and form snow if eligible. */
    public static boolean tryFormAt(ServerLevel level, BlockPos slabPos) {
        BlockPos snowPos = slabPos.above();
        if (canFormSnowOn(level, slabPos, snowPos)) {
            return formOnSlab(level, slabPos, level.getBlockState(slabPos));
        }
        return false;
    }

    /**
     * Manual placement eligibility: like canFormSnowOn but WITHOUT the weather/sky
     * gates - a player putting snow on a slab by hand should work indoors, at night,
     * any biome. Still requires an eligible BOTTOM slab (not ice/path) with air above.
     */
    public static boolean canPlaceSnowManually(Level level, BlockPos slabPos) {
        BlockState slabState = level.getBlockState(slabPos);
        if (!(slabState.getBlock() instanceof SlabBlock)) return false;
        if (!slabState.is(BlockTags.SLABS)) return false;
        if (slabState.is(ICE_SLABS) || slabState.is(PATH_SLABS)) return false;
        if (!slabState.hasProperty(SlabBlock.TYPE) || slabState.getValue(SlabBlock.TYPE) != SlabType.BOTTOM) return false;
        if (!level.getBlockState(slabPos.above()).isAir()) return false;
        return true;
    }


    // --- Drops: bottom slab + snowballs (also covers normal breaking) ---

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide && !player.getAbilities().instabuild) {
            ItemStack tool = player.getMainHandItem();
            if (hasSilkTouch(level, tool)) {
                // Vanilla parity: silk touch preserves the snow as placeable layers
                // instead of breaking it down into snowballs.
                popResource(level, pos, new ItemStack(Items.SNOW, state.getValue(LAYERS)));
            } else {
                popResource(level, pos, new ItemStack(Items.SNOWBALL, state.getValue(LAYERS)));
            }
            // Run the stored slab's own loot table (tool-aware) rather than always
            // dropping the slab item, so e.g. a grass slab without Silk Touch drops
            // dirt just like its uncovered counterpart.
            Block.dropResources(getStoredSlab(level, pos), level, pos, null, player, tool);
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    private static boolean hasSilkTouch(Level level, ItemStack tool) {
        Holder<Enchantment> silkTouch = level.registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(Enchantments.SILK_TOUCH);
        return EnchantmentHelper.getItemEnchantmentLevel(silkTouch, tool) > 0;
    }

    // --- Helpers ---

    /**
     * Returns the stored slab item for middle-click pick-block AND for HUD mods
     * (Jade/WAILA/WTHIT), which read this method's result for the displayed name.
     * So the snow slab reports e.g. "Terrain Granite Slab" instead of the test name.
     */
    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        BlockState stored = getStoredSlab(level, pos);
        if (!stored.isAir()) {
            ItemStack stack = new ItemStack(stored.getBlock());
            if (!stack.isEmpty()) {
                return stack;
            }
        }
        return super.getCloneItemStack(level, pos, state);
    }

    private BlockState getStoredSlab(BlockGetter level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof SnowSlabBlockEntity be) {
            return be.getBottomSlab();
        }
        return Blocks.SMOOTH_STONE_SLAB.defaultBlockState();
    }
}

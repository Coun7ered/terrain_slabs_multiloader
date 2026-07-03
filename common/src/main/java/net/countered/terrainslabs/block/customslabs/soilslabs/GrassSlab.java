package net.countered.terrainslabs.block.customslabs.soilslabs;

import net.countered.terrainslabs.block.customslabs.specialslabs.CustomSlab;
import net.countered.terrainslabs.registries.ModBlocksRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.lighting.LightEngine;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;


public class GrassSlab extends CustomSlab implements BonemealableBlock {
    public static final BooleanProperty SNOWY;
    static {
        SNOWY = BlockStateProperties.SNOWY;
    }

    public GrassSlab(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(TYPE, SlabType.BOTTOM)
                .setValue(SNOWY, false)
                .setValue(WATERLOGGED, false)
                .setValue(GENERATED, false));
    }

    @Override
    protected void spawnDestroyParticles(Level level, Player player, BlockPos pos, BlockState state) {
        if (state.getValue(TYPE) == SlabType.DOUBLE) {
            super.spawnDestroyParticles(level, player, pos, baseFullBlock());
        }
        else if (state.getValue(TYPE) == SlabType.TOP) {
            super.spawnDestroyParticles(level, player, pos, baseSlabBlock().setValue(TYPE, SlabType.TOP));
        }
        else {
            super.spawnDestroyParticles(level, player, pos, baseSlabBlock().setValue(TYPE, SlabType.BOTTOM));
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (direction == Direction.UP) {
            state = state.setValue(SNOWY, isSnow(neighborState));
        }

        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return state;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockPos = context.getClickedPos();
        BlockState blockState = context.getLevel().getBlockState(blockPos);
        if (blockState.is(this)) {
            return blockState.setValue(TYPE, SlabType.DOUBLE).setValue(WATERLOGGED, false);
        }
        BlockState blockAbove = context.getLevel().getBlockState(blockPos.above());
        BlockState defaultState = this.defaultBlockState().setValue(SNOWY, isSnow(blockAbove));

        FluidState fluidState = context.getLevel().getFluidState(blockPos);
        BlockState blockState2 = defaultState.setValue(TYPE, SlabType.BOTTOM).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
        Direction direction = context.getClickedFace();
        return direction != Direction.DOWN && (direction == Direction.UP || !(context.getClickLocation().y - blockPos.getY() > 0.5))
                    ? blockState2
                    : blockState2.setValue(TYPE, SlabType.TOP);

    }

    private static boolean isSnow(BlockState state) {
        return state.is(BlockTags.SNOW);
    }

    /**
     * The full block this grass slab is "made of" -- used for break particles and
     * as the random-tick reversion material. Defaults to dirt (so the base grass
     * slab and lush grass behave like a grass block). Stone-based variants such
     * as overgrown dacite/stone override this to return their stone material so
     * they break with the correct particles and sound.
     */
    protected BlockState baseFullBlock() {
        return Blocks.DIRT.defaultBlockState();
    }

    /**
     * The plain slab this grass slab reverts to / breaks into, as a BOTTOM-typed
     * state (callers re-apply the correct {@link SlabType}). Defaults to the
     * base mod's dirt slab; stone-based variants override it.
     */
    protected BlockState baseSlabBlock() {
        return ModBlocksRegistry.DIRT_SLAB.get().defaultBlockState();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SNOWY, TYPE, WATERLOGGED, GENERATED);
    }

    private static boolean canBeGrass(BlockState state, LevelReader levelReader, BlockPos pos) {
        BlockPos blockPos = pos.above();
        BlockState blockState = levelReader.getBlockState(blockPos);
        if (blockState.is(Blocks.SNOW) && (Integer)blockState.getValue(SnowLayerBlock.LAYERS) == 1) {
            return true;
        } else if (blockState.getFluidState().getAmount() == 8) {
            return false;
        } else {
            int i = LightEngine.getLightBlockInto(levelReader, Blocks.GRASS_BLOCK.defaultBlockState(), pos, blockState, blockPos, Direction.UP, blockState.getLightBlock(levelReader, blockPos));
            return i < levelReader.getMaxLightLevel();
        }
    }

    private static boolean canPropagate(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos blockPos = pos.above();
        return canBeGrass(state, level, pos) && !level.getFluidState(blockPos).is(FluidTags.WATER);
    }

    // ---- Bonemeal support (mirrors vanilla GrassBlock) ----
    // Inherited by the BWG lush grass slab, so both grass slabs scatter the same
    // short grass / flowers as a real grass block when bonemealed. Vanilla's
    // GrassBlock delegates to the GRASS_BONEMEAL placed feature; we run that same
    // feature so the result is identical (and any plants that land on adjacent
    // slabs survive thanks to the bush/seagrass placement mixins).

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return level.getBlockState(pos.above()).isAir();
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        BlockPos above = pos.above();
        BlockState shortGrass = Blocks.SHORT_GRASS.defaultBlockState();

        label:
        for (int i = 0; i < 128; i++) {
            BlockPos target = above;

            for (int j = 0; j < i / 16; j++) {
                target = target.offset(random.nextInt(3) - 1,
                        (random.nextInt(3) - 1) * random.nextInt(3) / 2,
                        random.nextInt(3) - 1);

                // Snap to the air cell sitting on grass-type ground (slab OR full
                // grass block) within a small vertical window, bridging the
                // half-block surface-height difference between slabs and blocks so
                // scatter walks smoothly across a mix of both.
                BlockPos surfaceAir = findGrassSurfaceAir(level, target);
                if (surfaceAir == null) {
                    continue label;
                }
                target = surfaceAir;
            }

            BlockState atTarget = level.getBlockState(target);

            if (atTarget.is(shortGrass.getBlock()) && random.nextInt(10) == 0) {
                ((BonemealableBlock) shortGrass.getBlock()).performBonemeal(level, random, target, atTarget);
            }

            if (atTarget.isAir()) {
                BlockState toPlace;
                if (random.nextInt(8) == 0) {
                    java.util.List<net.minecraft.world.level.levelgen.feature.ConfiguredFeature<?, ?>> flowers =
                            level.getBiome(target).value().getGenerationSettings().getFlowerFeatures();
                    if (flowers.isEmpty()) {
                        continue;
                    }
                    BlockState resolved = resolveFlower(flowers.get(0), random, target);
                    if (resolved == null) {
                        continue;
                    }
                    toPlace = resolved;
                } else {
                    toPlace = shortGrass;
                }

                if (toPlace.canSurvive(level, target)) {
                    level.setBlock(target, toPlace, 3);
                    level.gameEvent(net.minecraft.world.level.gameevent.GameEvent.BLOCK_PLACE, target,
                            net.minecraft.world.level.gameevent.GameEvent.Context.of(toPlace));
                }
            }
        }
    }

    /**
     * Given an approximate column position, finds the air/replaceable cell that
     * sits directly on top of grass-type ground (a {@link GrassSlab} or a vanilla
     * {@link GrassBlock}) within a small vertical window. This bridges the
     * half-block surface-height difference between slabs and full grass blocks so
     * bonemeal scatter walks smoothly across a mix of both. Returns the air cell
     * to place into, or {@code null} if no grass surface is nearby.
     */
    private static BlockPos findGrassSurfaceAir(ServerLevel level, BlockPos approx) {
        // Probe likely ground positions to bridge the slab/full-block height step:
        // the surface may be directly under `approx`, level with it, or one lower.
        BlockPos[] groundCandidates = new BlockPos[] {
                approx.below(),        // surface directly under `approx`
                approx,                // `approx` is itself the ground (surface a cell higher)
                approx.below(2)        // ground a cell lower (slab below a block step)
        };
        for (BlockPos ground : groundCandidates) {
            Block gb = level.getBlockState(ground).getBlock();
            if (!(gb instanceof GrassSlab) && !(gb instanceof GrassBlock)) {
                continue;
            }
            BlockPos airPos = ground.above();
            BlockState airState = level.getBlockState(airPos);
            if (!airState.isCollisionShapeFullBlock(level, airPos)) {
                return airPos;
            }
        }
        return null;
    }

    private static BlockState resolveFlower(net.minecraft.world.level.levelgen.feature.ConfiguredFeature<?, ?> flower,
                                            RandomSource random, BlockPos pos) {
        try {
            if (flower.config() instanceof net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration patch) {
                var inner = patch.feature().value().feature().value().config();
                if (inner instanceof net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration simple) {
                    return simple.toPlace().getState(random, pos);
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    @Override
    public BonemealableBlock.Type getType() {
        return BonemealableBlock.Type.NEIGHBOR_SPREADER;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!canBeGrass(state, level, pos)) {
            if (state.getValue(TYPE) == SlabType.TOP) {
                level.setBlockAndUpdate(pos, baseSlabBlock().setValue(TYPE, SlabType.TOP));
            }
            else if (state.getValue(TYPE) == SlabType.DOUBLE) {
                level.setBlockAndUpdate(pos, baseSlabBlock().setValue(TYPE, SlabType.DOUBLE));
            }
            else if (state.getValue(TYPE) == SlabType.BOTTOM){
                level.setBlockAndUpdate(pos, baseSlabBlock().setValue(TYPE, SlabType.BOTTOM));
            }
        } else {
            if (level.getMaxLocalRawBrightness(pos.above()) >= 9) {
                BlockState blockState = this.defaultBlockState().setValue(TYPE, level.getBlockState(pos).getValue(TYPE));

                for (int i = 0; i < 4; i++) {
                    BlockPos blockPos = pos.offset(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
                    if (level.getBlockState(blockPos).is(ModBlocksRegistry.DIRT_SLAB.get()) && canPropagate(blockState, level, blockPos)) {
                        level.setBlockAndUpdate(blockPos, blockState.setValue(SNOWY, level.getBlockState(blockPos.above()).is(Blocks.SNOW)).setValue(TYPE, level.getBlockState(pos).getValue(TYPE)));
                    }
                }
            }
        }
    }
}

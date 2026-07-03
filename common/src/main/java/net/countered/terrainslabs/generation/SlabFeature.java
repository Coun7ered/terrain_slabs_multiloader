package net.countered.terrainslabs.generation;

import com.mojang.serialization.Codec;
import net.countered.terrainslabs.block.ModSlabsMap;
import net.countered.terrainslabs.block.customslabs.SoilSlabBase;
import net.countered.terrainslabs.block.customslabs.specialslabs.CustomSlab;
import net.countered.terrainslabs.platform.PlatformConfigHooks;
import net.countered.terrainslabs.registries.ModBlocksRegistry;
import net.countered.terrainslabs.snowslab.SnowSlabBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.*;

public class SlabFeature extends Feature<NoneFeatureConfiguration> {

    /** Ticks to wait before a generated gravity slab evaluates its support.
     *  Matches GravityAffectedSlab#getDelayAfterPlace(). */
    private static final int GRAVITY_SLAB_FALL_DELAY = 2;

    public SlabFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        if (PlatformConfigHooks.isSlabGenerationEnabled()) {
            WorldGenLevel level = context.level();
            BlockPos origin = context.origin();
            generateSlabs(level, origin);
            return true;
        }
        return false;
    }

    private void generateSlabs(WorldGenLevel level, BlockPos origin) {
        Set<BlockPos> botSlabPositions = new HashSet<>();
        Set<BlockPos> topSlabPositions = new HashSet<>();
        Set<BlockPos> extendedPositions = new HashSet<>();

        ChunkPos chunkPos = new ChunkPos(origin);
        int minY = level.getMinBuildHeight();
        int offsetXZ = PlatformConfigHooks.isCornerSlabsEnabled() ? 1 : 0;
        for (int x = -offsetXZ; x < 16 + offsetXZ ; x++) {
            for (int z = -offsetXZ; z < 16 + offsetXZ; z++) {
                int worldX = chunkPos.getMinBlockX() + x;
                int worldZ = chunkPos.getMinBlockZ() + z;
                int maxY = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, worldX, worldZ);
                for (int y = maxY; y >= minY; y--) {
                    BlockPos currentPos = new BlockPos(worldX, y, worldZ);
                    if (shouldPlaceBottomSlab(level, currentPos, y == maxY-1, false)) {
                        botSlabPositions.add(currentPos);
                    } else if (shouldPlaceTopSlab(level, currentPos)) {
                        topSlabPositions.add(currentPos);
                    }
                }
            }
        }
        placeBottomSlabs(level, botSlabPositions);
        placeTopSlabs(level, topSlabPositions);
        if (PlatformConfigHooks.getSlabRunLength() > 1) {
            generateExtended(level, botSlabPositions, extendedPositions);
        }
        else if (PlatformConfigHooks.isCornerSlabsEnabled()) {
            calculateCornerPositions(level, botSlabPositions, extendedPositions);
            placeBottomSlabs(level, extendedPositions);
        }
    }

    private void placeBottomSlabs(WorldGenLevel level, Set<BlockPos> slabPositions) {
        for (BlockPos pos : slabPositions) {
            placeBottomSlab(level, pos);
        }
    }

    private void placeTopSlabs(WorldGenLevel level, Set<BlockPos> topSlabPositions) {
        for (BlockPos pos : topSlabPositions) {
            placeTopSlab(level, pos);
        }
    }

    private void generateExtended(WorldGenLevel level, Set<BlockPos> botSlabPositions, Set<BlockPos> extendedPositions) {
        for (int i = 1; i < PlatformConfigHooks.getSlabRunLength(); i++) {
            extendedPositions.clear();
            calculateExtendedPositions(level, botSlabPositions, extendedPositions);
            placeBottomSlabs(level, extendedPositions);
            botSlabPositions = Set.copyOf(extendedPositions);
        }
    }

    private void calculateExtendedPositions(WorldGenLevel level, Set<BlockPos> botSlabPositions, Set<BlockPos> extendedPositions) {
        for (BlockPos pos : botSlabPositions) {
            for (Direction direction : Direction.values()) {
                BlockPos extendedPos = pos.relative(direction);
                if (shouldPlaceBottomSlab(level, extendedPos, false, true)) {
                    extendedPositions.add(extendedPos);
                }
            }
        }
    }

    private void calculateCornerPositions(WorldGenLevel level, Set<BlockPos> botSlabPositions, Set<BlockPos> cornerPositions) {
        int[][] diagonals = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        for (BlockPos currentPos : botSlabPositions) {
            for (int[] d : diagonals) {
                int nx = currentPos.getX() + d[0];
                int nz = currentPos.getZ() + d[1];
                int y = currentPos.getY();

                if (!botSlabPositions.contains(new BlockPos(nx, y, nz))) continue;

                BlockPos corner1 = new BlockPos(currentPos.getX(), y, nz);
                BlockPos corner2 = new BlockPos(nx, y, currentPos.getZ());

                if (shouldPlaceBottomSlab(level, corner1, false, true)) cornerPositions.add(corner1);
                if (shouldPlaceBottomSlab(level, corner2, false, true)) cornerPositions.add(corner2);
            }
        }
    }

    /**
     * Determines if a slab should be placed at the given position based on world conditions.
     */
    private boolean shouldPlaceBottomSlab(WorldGenLevel level, BlockPos currentPos, boolean isMaxY, boolean neighbourCanBeSlab) {
        if (!isPosUpDownValid(level, currentPos)) return false;

        // fix for slabs replacing ice in ice biomes
        Biome biome = level.getBiome(currentPos).value();
        if (isMaxY && biome.shouldFreeze(level, currentPos, false)) return false;

        if (!validSurroundingBottom(level, currentPos, neighbourCanBeSlab)) return false;

        return true;
    }

    private static boolean isPosUpDownValid(WorldGenLevel level, BlockPos currentPos) {
        BlockState currentBlockState = level.getBlockState(currentPos);
        // Don't replace powder snow (it has an empty collision shape, so it would
        // otherwise slip past the collision check below).
        if (currentBlockState.is(Blocks.POWDER_SNOW)) return false;
        if (!currentBlockState.getCollisionShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO).isEmpty()
                && !(currentBlockState.getBlock() instanceof SlabBlock)) return false;
        BlockState blockAboveState = level.getBlockState(currentPos.above());
        // Don't place a slab directly under powder snow.
        if (blockAboveState.is(Blocks.POWDER_SNOW)) return false;
        if (!blockAboveState.getCollisionShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO).isEmpty()) return false;
        BlockState blockBelowState = level.getBlockState(currentPos.below());
        if (ModSlabsMap.getSlabForBlock(blockBelowState.getBlock()) == null) return false;
        return true;
    }


    private boolean validSurroundingBottom(WorldGenLevel level, BlockPos currentPos, boolean neighbourCanBeSlab) {
        boolean validNeighbors = false;

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos neighborPos = currentPos.relative(direction);
            BlockPos oppositePos = currentPos.relative(direction.getOpposite());
            BlockPos belowOppositePos = oppositePos.below();
            BlockState neighborState = level.getBlockState(neighborPos);
            BlockState oppositeState = level.getBlockState(belowOppositePos);
            BlockState belowOppositeState = level.getBlockState(belowOppositePos);

            if (neighborState.is(Blocks.LAVA)) return false;

            if (((!neighborState.getCollisionShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO).isEmpty() && !(neighborState.getBlock() instanceof SlabBlock))
                    || ((neighborState.getBlock() instanceof SlabBlock) && neighbourCanBeSlab))
                    && !(oppositeState.getBlock() instanceof SlabBlock)
                    && !belowOppositeState.getCollisionShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO).isEmpty() && !(belowOppositeState.getBlock() instanceof SlabBlock)
                    && (level.getBlockState(neighborPos.above()).getCollisionShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO).isEmpty()))
            {
                validNeighbors = true;
            }
        }
        return validNeighbors;
    }

    private void placeBottomSlab(WorldGenLevel level, BlockPos pos) {
        BlockPos blockBelowPos = pos.below();
        BlockPos blockAbovePos = pos.above();
        BlockState blockAboveState = level.getBlockState(blockAbovePos);
        BlockState currentBlockState = level.getBlockState(pos);
        BlockState blockBelowState = level.getBlockState(blockBelowPos);

        // fix for slabs over already placed slabs across chunk boundaries
        if (currentBlockState.getBlock() instanceof SlabBlock) return;

        // Retrieve the slab type based on the block below the current position
        Block slab = ModSlabsMap.getSlabForBlock(blockBelowState.getBlock());
        if (slab == null) return;

        BlockState slabState = slab.defaultBlockState();

        // fix for floating vegetation, due to sometimes generating into neighboring chunks before slabs
        if (blockAboveState.getBlock() instanceof DoublePlantBlock) {
            setBlockState(level, blockAbovePos, Blocks.AIR.defaultBlockState());
            if (!blockAboveState.getFluidState().isEmpty()) {
                setBlockState(level, blockAbovePos, Blocks.WATER.defaultBlockState());
            }
        }

        // Soil slabs convert the block below before placing. Use the slab's own
        // base material (dirt by default, stone for overgrown-stone/dacite etc.)
        if (ModSlabsMap.isSoilSlab(slabState.getBlock())) {
            BlockState belowState = slabState.getBlock() instanceof SoilSlabBase soil
                    ? soil.baseFullBlock()
                    : Blocks.DIRT.defaultBlockState();
            setBlockState(level, blockBelowPos, belowState);
        }
        if (slabState.is(ModBlocksRegistry.WARPED_NYLIUM_SLAB.get()) || slabState.is(ModBlocksRegistry.CRIMSON_NYLIUM_SLAB.get())) {
            setBlockState(level,blockBelowPos, Blocks.NETHERRACK.defaultBlockState());
        }
        slabState = updateBottomWaterloggedState(currentBlockState, blockAboveState, slabState);
        BlockState placedSlab = slabState.setValue(CustomSlab.GENERATED, true);
        setBlockState(level, pos, placedSlab);

        // In cold biomes, generate the slab already snow-capped (1 layer), mirroring
        // vanilla ground snow at worldgen. Only for a sky-exposed bottom slab with air
        // directly above. Weather can still bring bare slabs to 1 layer later; manual
        // placement stacks beyond that.
        if (shouldSnowCapAtGen(level, pos, blockAboveState)) {
            SnowSlabBlock.formOnSlabWorldgen(level, pos, placedSlab);
        }
    }

    private boolean shouldSnowCapAtGen(WorldGenLevel level, BlockPos pos, BlockState blockAboveState) {
        // Must have air directly above to hold snow, and be in a cold biome.
        if (!blockAboveState.isAir()) return false;
        Biome biome = level.getBiome(pos).value();
        if (!biome.coldEnoughToSnow(pos.above())) return false;

        // Primary: sky-exposed spots snow like vanilla ground snow.
        if (level.canSeeSky(pos.above())) return true;

        // Fill-in: even if not sky-exposed, snow-cap when an adjacent FULL BLOCK
        // (same Y, above, or below) is already snowy. Shared helper on SnowSlabBlock;
        // slabs are ignored there to avoid slab->slab feedback. (A more complete
        // fill-in also runs LATE, after vanilla ground snow, via
        // MixinSnowAndFreezeFeature.)
        return SnowSlabBlock.hasSnowyFullBlockNeighbor(level, pos);
    }

    private BlockState updateBottomWaterloggedState(BlockState currentBlockState, BlockState blockAboveState, BlockState slabState) {
        if (slabState.hasProperty(BlockStateProperties.WATERLOGGED)) {
            if (currentBlockState.is(Blocks.WATER) || blockAboveState.is(Blocks.WATER) || currentBlockState.getFluidState().is(FluidTags.WATER))
            {
                return slabState.setValue(BlockStateProperties.WATERLOGGED, true);
            }
        }
        return slabState;
    }

    private boolean shouldPlaceTopSlab(WorldGenLevel level, BlockPos currentPos) {
        BlockPos blockAbovePos = currentPos.above();
        BlockPos blockBelowPos = currentPos.below();
        BlockState currentBlockState = level.getBlockState(currentPos);

        if (!currentBlockState.isCollisionShapeFullBlock(EmptyBlockGetter.INSTANCE, BlockPos.ZERO)) return false;

        BlockState blockBelowState = level.getBlockState(blockBelowPos);
        if(blockBelowState.isCollisionShapeFullBlock(EmptyBlockGetter.INSTANCE, BlockPos.ZERO)) return false;

        BlockState blockAboveState = level.getBlockState(blockAbovePos);
        if (ModSlabsMap.getSlabForBlock(blockAboveState.getBlock()) == null) return false;

        if (!validSurroundingTop(level, currentPos)) return false;

        return true;
    }

    private boolean validSurroundingTop(WorldGenLevel level, BlockPos currentPos) {
        boolean validNeighbors = false;
        boolean hasWaterNeighbor = false;
        boolean hasAirNeighbor = false;

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos neighborPos = currentPos.relative(direction);
            BlockPos oppositePos = currentPos.relative(direction.getOpposite());
            BlockPos aboveOppositePos = oppositePos.above();
            BlockState neighborState = level.getBlockState(neighborPos);
            BlockState oppositeState = level.getBlockState(oppositePos);
            BlockState aboveOppositeState = level.getBlockState(aboveOppositePos);

            if (neighborState.is(Blocks.GLOW_LICHEN) || neighborState.is(Blocks.LAVA)) {
                return false;
            } else if (neighborState.is(Blocks.WATER)) {
                hasWaterNeighbor = true;
            } else if (neighborState.is(Blocks.AIR)) {
                hasAirNeighbor = true;
            }

            // Check neighboring blocks to ensure at least one horizontal neighbor is air or water
            if ((neighborState.isCollisionShapeFullBlock(EmptyBlockGetter.INSTANCE, BlockPos.ZERO) || neighborState.getBlock() instanceof SlabBlock) &&
                    aboveOppositeState.isCollisionShapeFullBlock(EmptyBlockGetter.INSTANCE, BlockPos.ZERO) &&
                    !oppositeState.isCollisionShapeFullBlock(EmptyBlockGetter.INSTANCE, BlockPos.ZERO) && !(oppositeState.getBlock() instanceof SlabBlock) &&
                    !level.getBlockState(neighborPos.below()).isCollisionShapeFullBlock(EmptyBlockGetter.INSTANCE, BlockPos.ZERO)
            ) {
                validNeighbors = true;
            }
        }
        return validNeighbors && (!(hasWaterNeighbor && hasAirNeighbor));
    }

    private void placeTopSlab(WorldGenLevel level, BlockPos pos) {
        Boolean waterlogged = isTopStateWaterlogged(level, pos);
        BlockState blockAboveState = level.getBlockState(pos.above());

        // Retrieve the slab type based on the block below the current position
        BlockState slabState = Objects.requireNonNull(ModSlabsMap.getSlabForBlock(blockAboveState.getBlock())).defaultBlockState();

        if (slabState.getBlock().equals(Blocks.AIR)) {
            return;
        }
        if (ModSlabsMap.isSoilSlab(slabState.getBlock())) {
            slabState = slabState.getBlock() instanceof SoilSlabBase soil
                    ? soil.baseSlabBlock()
                    : ModBlocksRegistry.DIRT_SLAB.get().defaultBlockState();
        }
        if (slabState.is(ModBlocksRegistry.WARPED_NYLIUM_SLAB.get()) || slabState.is(ModBlocksRegistry.CRIMSON_NYLIUM_SLAB.get())) {
            slabState = ModBlocksRegistry.NETHERRACK_SLAB.get().defaultBlockState();
        }
        setBlockState(level, pos, slabState.setValue(CustomSlab.GENERATED, true).setValue(BlockStateProperties.SLAB_TYPE, SlabType.TOP).setValue(BlockStateProperties.WATERLOGGED, waterlogged));
    }

    private boolean isTopStateWaterlogged(LevelAccessor levelAccessor, BlockPos currentPos) {
        if (!levelAccessor.getBlockState(currentPos.below()).is(Blocks.WATER)) {
            return false;
        }
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            // Check if the neighbor contains water to set the waterlogged property
            if (levelAccessor.getBlockState(currentPos.relative(direction)).is(Blocks.WATER)) {
                return true;
            }
        }
        return false;
    }

    private void setBlockState(LevelAccessor world, BlockPos pos, BlockState state) {
        world.setBlock(pos, state, 3);
        // Gravity-affected slabs (sand, gravel, ...) don't receive an onPlace
        // callback during world-gen, so they never evaluate their support and
        // can hang in mid-air when generated on an overhang. Schedule a tick so
        // unsupported ones fall immediately, exactly as they would if disturbed.
        if (state.getBlock() instanceof Fallable) {
            world.scheduleTick(pos, state.getBlock(), GRAVITY_SLAB_FALL_DELAY);
        }
    }
}


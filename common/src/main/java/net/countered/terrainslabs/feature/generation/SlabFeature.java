package net.countered.terrainslabs.feature.generation;

import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.countered.terrainslabs.block.ModSlabsMap;
import net.countered.terrainslabs.block.customslabs.CustomSlab;
import net.countered.terrainslabs.config.TSConfig;
import net.countered.terrainslabs.registries.ModBlocksRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;

public class SlabFeature implements Feature {
    public static final MapCodec<SlabFeature> CODEC = MapCodec.unit(SlabFeature::new);

    private static final int[][] POSITIVE_DIAGONALS = {{1, 1}, {1, -1}};

    @Override
    public MapCodec<SlabFeature> codec() {
        return CODEC;
    }

    @Override
    public boolean place(WorldGenLevel level, ChunkGenerator generator, RandomSource random, BlockPos origin) {
        if (TSConfig.isSlabGenerationEnabled()) {
            generateSlabs(level, origin);
            return true;
        }
        return false;
    }

    private void generateSlabs(WorldGenLevel level, BlockPos origin) {
        LongSet botSlabPositions = new LongOpenHashSet();
        LongSet topSlabPositions = new LongOpenHashSet();
        LongSet extendedPositions = new LongOpenHashSet();

        BlockPos.MutableBlockPos currentPos = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos scratchPos = new BlockPos.MutableBlockPos();

        ChunkPos chunkPos = ChunkPos.containing(origin);
        int minBlockX = chunkPos.getMinBlockX();
        int minBlockZ = chunkPos.getMinBlockZ();
        int minY = level.getMinY();
        boolean cornerSlabsEnabled = TSConfig.isCornerSlabsEnabled();
        int offsetXZ = cornerSlabsEnabled ? 1 : 0;

        ChunkAccess lastChunk = null;
        int lastChunkX = Integer.MIN_VALUE;
        int lastChunkZ = Integer.MIN_VALUE;

        for (int x = -offsetXZ; x < 16 + offsetXZ; x++) {
            int worldX = minBlockX + x;
            int chunkX = worldX >> 4;
            for (int z = -offsetXZ; z < 16 + offsetXZ; z++) {
                int worldZ = minBlockZ + z;
                int chunkZ = worldZ >> 4;

                if (chunkX != lastChunkX || chunkZ != lastChunkZ) {
                    lastChunk = level.getChunk(chunkX, chunkZ);
                    lastChunkX = chunkX;
                    lastChunkZ = chunkZ;
                }

                int maxY = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, worldX, worldZ);
                if (maxY < minY) continue;

                BlockState stateAbove = lastChunk.getBlockState(currentPos.set(worldX, maxY + 1, worldZ));
                BlockState stateCurrent = lastChunk.getBlockState(currentPos.set(worldX, maxY, worldZ));

                for (int y = maxY; y >= minY; y--) {
                    BlockState stateBelow = lastChunk.getBlockState(currentPos.set(worldX, y - 1, worldZ));
                    currentPos.setY(y);

                    if (shouldPlaceBottomSlab(level, currentPos, scratchPos, stateAbove, stateCurrent, stateBelow, y == maxY - 1, false)) {
                        botSlabPositions.add(currentPos.asLong());
                    } else if (!stateCurrent.isAir() && shouldPlaceTopSlab(level, currentPos, scratchPos, stateAbove, stateCurrent, stateBelow)) {
                        topSlabPositions.add(currentPos.asLong());
                    }

                    stateAbove = stateCurrent;
                    stateCurrent = stateBelow;
                }
            }
        }

        placeBottomSlabs(level, botSlabPositions, currentPos);
        placeTopSlabs(level, topSlabPositions, currentPos);

        int slabRunLength = TSConfig.getSlabRunLength();
        if (slabRunLength > 1) {
            generateExtended(level, slabRunLength, botSlabPositions, extendedPositions, currentPos, scratchPos);
        } else if (cornerSlabsEnabled) {
            calculateCornerPositions(level, botSlabPositions, extendedPositions, currentPos, scratchPos);
            placeBottomSlabs(level, extendedPositions, currentPos);
        }
    }

    private void placeBottomSlabs(WorldGenLevel level, LongSet slabPositions, BlockPos.MutableBlockPos scratchPos) {
        LongIterator iterator = slabPositions.iterator();
        while (iterator.hasNext()) {
            scratchPos.set(iterator.nextLong());
            placeBottomSlab(level, scratchPos);
        }
    }

    private void placeTopSlabs(WorldGenLevel level, LongSet topSlabPositions, BlockPos.MutableBlockPos scratchPos) {
        LongIterator iterator = topSlabPositions.iterator();
        while (iterator.hasNext()) {
            scratchPos.set(iterator.nextLong());
            placeTopSlab(level, scratchPos);
        }
    }

    private void generateExtended(
            WorldGenLevel level,
            int slabRunLength,
            LongSet botSlabPositions,
            LongSet extendedPositions,
            BlockPos.MutableBlockPos candidatePos,
            BlockPos.MutableBlockPos scratchPos
    ) {
        for (int i = 1; i < slabRunLength; i++) {
            extendedPositions.clear();
            calculateExtendedPositions(level, botSlabPositions, extendedPositions, candidatePos, scratchPos);
            if (extendedPositions.isEmpty()) {
                break;
            }
            placeBottomSlabs(level, extendedPositions, candidatePos);
            LongSet temp = botSlabPositions;
            botSlabPositions = extendedPositions;
            extendedPositions = temp;
        }
    }

    private void calculateExtendedPositions(
            WorldGenLevel level,
            LongSet botSlabPositions,
            LongSet extendedPositions,
            BlockPos.MutableBlockPos candidatePos,
            BlockPos.MutableBlockPos scratchPos
    ) {
        LongIterator iterator = botSlabPositions.iterator();
        while (iterator.hasNext()) {
            long posLong = iterator.nextLong();
            int x = BlockPos.getX(posLong);
            int y = BlockPos.getY(posLong);
            int z = BlockPos.getZ(posLong);

            for (Direction direction : Direction.Plane.HORIZONTAL) {
                int ex = x + direction.getStepX();
                int ez = z + direction.getStepZ();
                long extendedLong = BlockPos.asLong(ex, y, ez);
                if (extendedPositions.contains(extendedLong)) {
                    continue;
                }
                candidatePos.set(ex, y, ez);
                if (shouldPlaceBottomSlab(level, candidatePos, scratchPos, false, true)) {
                    extendedPositions.add(extendedLong);
                }
            }
        }
    }

    private void calculateCornerPositions(
            WorldGenLevel level,
            LongSet botSlabPositions,
            LongSet cornerPositions,
            BlockPos.MutableBlockPos candidatePos,
            BlockPos.MutableBlockPos scratchPos
    ) {
        LongIterator iterator = botSlabPositions.iterator();
        while (iterator.hasNext()) {
            long posLong = iterator.nextLong();
            int x = BlockPos.getX(posLong);
            int y = BlockPos.getY(posLong);
            int z = BlockPos.getZ(posLong);

            for (int[] d : POSITIVE_DIAGONALS) {
                int nx = x + d[0];
                int nz = z + d[1];

                if (!botSlabPositions.contains(BlockPos.asLong(nx, y, nz))) continue;

                long corner1Long = BlockPos.asLong(x, y, nz);
                if (!cornerPositions.contains(corner1Long)) {
                    candidatePos.set(x, y, nz);
                    if (shouldPlaceBottomSlab(level, candidatePos, scratchPos, false, true)) {
                        cornerPositions.add(corner1Long);
                    }
                }

                long corner2Long = BlockPos.asLong(nx, y, z);
                if (!cornerPositions.contains(corner2Long)) {
                    candidatePos.set(nx, y, z);
                    if (shouldPlaceBottomSlab(level, candidatePos, scratchPos, false, true)) {
                        cornerPositions.add(corner2Long);
                    }
                }
            }
        }
    }

    /**
     * Determines if a slab should be placed at the given position based on world conditions.
     */
    private boolean shouldPlaceBottomSlab(
            WorldGenLevel level,
            BlockPos currentPos,
            BlockPos.MutableBlockPos scratchPos,
            boolean isMaxY,
            boolean neighbourCanBeSlab
    ) {
        int x = currentPos.getX();
        int y = currentPos.getY();
        int z = currentPos.getZ();
        BlockState stateCurrent = level.getBlockState(currentPos);
        BlockState stateAbove = level.getBlockState(scratchPos.set(x, y + 1, z));
        BlockState stateBelow = level.getBlockState(scratchPos.set(x, y - 1, z));
        return shouldPlaceBottomSlab(level, currentPos, scratchPos, stateAbove, stateCurrent, stateBelow, isMaxY, neighbourCanBeSlab);
    }

    private boolean shouldPlaceBottomSlab(
            WorldGenLevel level,
            BlockPos currentPos,
            BlockPos.MutableBlockPos scratchPos,
            BlockState stateAbove,
            BlockState stateCurrent,
            BlockState stateBelow,
            boolean isMaxY,
            boolean neighbourCanBeSlab
    ) {
        if (!isPosUpDownValid(stateAbove, stateCurrent, stateBelow)) return false;

        if (!validSurroundingBottom(level, currentPos, scratchPos, neighbourCanBeSlab)) return false;

        // fix for slabs replacing ice in ice biomes
        if (isMaxY && level.getBiome(currentPos).value().shouldFreeze(level, currentPos, false)) return false;

        return true;
    }

    private static boolean isPosUpDownValid(BlockState stateAbove, BlockState stateCurrent, BlockState stateBelow) {
        if (!stateCurrent.isAir()) {
            Block currentBlock = stateCurrent.getBlock();
            if (currentBlock instanceof PowderSnowBlock) return false;
            if (!(currentBlock instanceof SlabBlock)
                    && !stateCurrent.getCollisionShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO).isEmpty()) {
                return false;
            }
        }
        if (!isCollisionShapeEmpty(stateAbove)) return false;
        return ModSlabsMap.getSlabForBlock(stateBelow.getBlock()) != null;
    }

    private static boolean isCollisionShapeEmpty(BlockState state) {
        return state.isAir() || state.getCollisionShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO).isEmpty();
    }

    private boolean validSurroundingBottom(
            WorldGenLevel level,
            BlockPos currentPos,
            BlockPos.MutableBlockPos scratchPos,
            boolean neighbourCanBeSlab
    ) {
        boolean validNeighbors = false;
        int x = currentPos.getX();
        int y = currentPos.getY();
        int z = currentPos.getZ();

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            int dx = direction.getStepX();
            int dz = direction.getStepZ();
            BlockState neighborState = level.getBlockState(scratchPos.set(x + dx, y, z + dz));

            if (neighborState.is(Blocks.LAVA)) return false;
            if (validNeighbors) continue;

            Block neighborBlock = neighborState.getBlock();
            boolean validNeighbor = (neighborBlock instanceof SlabBlock)
                    ? neighbourCanBeSlab
                    : (!neighborState.isAir() && !neighborState.getCollisionShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO).isEmpty());
            if (!validNeighbor) continue;

            BlockState belowOppositeState = level.getBlockState(scratchPos.set(x - dx, y - 1, z - dz));
            if (belowOppositeState.getBlock() instanceof SlabBlock || isCollisionShapeEmpty(belowOppositeState)) {
                continue;
            }

            BlockState neighborAboveState = level.getBlockState(scratchPos.set(x + dx, y + 1, z + dz));
            if (isCollisionShapeEmpty(neighborAboveState)) {
                validNeighbors = true;
            }
        }
        return validNeighbors;
    }

    private void placeBottomSlab(WorldGenLevel level, BlockPos pos) {
        BlockState currentBlockState = level.getBlockState(pos);

        // fix for slabs over already placed slabs across chunk boundaries
        if (currentBlockState.getBlock() instanceof SlabBlock) return;

        BlockPos blockBelowPos = pos.below();
        BlockState blockBelowState = level.getBlockState(blockBelowPos);

        // Retrieve the slab type based on the block below the current position
        Block slab = ModSlabsMap.getSlabForBlock(blockBelowState.getBlock());
        if (slab == null) return;

        BlockPos blockAbovePos = pos.above();
        BlockState blockAboveState = level.getBlockState(blockAbovePos);
        BlockState slabState = slab.defaultBlockState();

        // fix for floating vegetation, due to sometimes generating into neighboring chunks before slabs
        if (blockAboveState.getBlock() instanceof DoublePlantBlock) {
            if (!blockAboveState.getFluidState().isEmpty()) {
                setBlockState(level, blockAbovePos, Blocks.WATER.defaultBlockState());
            } else {
                setBlockState(level, blockAbovePos, Blocks.AIR.defaultBlockState());
            }
        }

        // Handle grass slab special case by converting grass to dirt before placing the slab
        if (ModSlabsMap.SOIL_SLAB_BLOCKS.contains(slab)) {
            setBlockState(level, blockBelowPos, Blocks.DIRT.defaultBlockState());
        } else if (slabState.is(ModBlocksRegistry.WARPED_NYLIUM_SLAB.get()) || slabState.is(ModBlocksRegistry.CRIMSON_NYLIUM_SLAB.get())) {
            setBlockState(level, blockBelowPos, Blocks.NETHERRACK.defaultBlockState());
        }
        slabState = updateBottomWaterloggedState(currentBlockState, blockAboveState, slabState);
        setBlockState(level, pos.immutable(), CustomSlab.markGenerated(slabState));
    }

    private BlockState updateBottomWaterloggedState(BlockState currentBlockState, BlockState blockAboveState, BlockState slabState) {
        if (slabState.hasProperty(BlockStateProperties.WATERLOGGED)) {
            if (currentBlockState.is(Blocks.WATER) || blockAboveState.is(Blocks.WATER) || currentBlockState.getFluidState().is(FluidTags.WATER)) {
                return slabState.setValue(BlockStateProperties.WATERLOGGED, true);
            }
        }
        return slabState;
    }

    private boolean shouldPlaceTopSlab(
            WorldGenLevel level,
            BlockPos currentPos,
            BlockPos.MutableBlockPos scratchPos,
            BlockState stateAbove,
            BlockState stateCurrent,
            BlockState stateBelow
    ) {
        if (!stateBelow.isAir() && stateBelow.isCollisionShapeFullBlock(EmptyBlockGetter.INSTANCE, BlockPos.ZERO)) return false;
        if (!stateCurrent.isCollisionShapeFullBlock(EmptyBlockGetter.INSTANCE, BlockPos.ZERO)) return false;
        if (ModSlabsMap.getSlabForBlock(stateAbove.getBlock()) == null) return false;

        return validSurroundingTop(level, currentPos, scratchPos);
    }

    private boolean validSurroundingTop(WorldGenLevel level, BlockPos currentPos, BlockPos.MutableBlockPos scratchPos) {
        int x = currentPos.getX();
        int y = currentPos.getY();
        int z = currentPos.getZ();

        BlockState northState = level.getBlockState(scratchPos.set(x, y, z - 1));
        if (northState.is(Blocks.GLOW_LICHEN) || northState.is(Blocks.LAVA)) return false;

        BlockState southState = level.getBlockState(scratchPos.set(x, y, z + 1));
        if (southState.is(Blocks.GLOW_LICHEN) || southState.is(Blocks.LAVA)) return false;

        BlockState westState = level.getBlockState(scratchPos.set(x - 1, y, z));
        if (westState.is(Blocks.GLOW_LICHEN) || westState.is(Blocks.LAVA)) return false;

        BlockState eastState = level.getBlockState(scratchPos.set(x + 1, y, z));
        if (eastState.is(Blocks.GLOW_LICHEN) || eastState.is(Blocks.LAVA)) return false;

        boolean hasWaterNeighbor = northState.is(Blocks.WATER) || southState.is(Blocks.WATER) || westState.is(Blocks.WATER) || eastState.is(Blocks.WATER);
        boolean hasAirNeighbor = northState.is(Blocks.AIR) || southState.is(Blocks.AIR) || westState.is(Blocks.AIR) || eastState.is(Blocks.AIR);
        if (hasWaterNeighbor && hasAirNeighbor) {
            return false;
        }

        return isValidTopDirection(level, scratchPos, x, y, z, 0, -1, northState, southState)
                || isValidTopDirection(level, scratchPos, x, y, z, 0, 1, southState, northState)
                || isValidTopDirection(level, scratchPos, x, y, z, -1, 0, westState, eastState)
                || isValidTopDirection(level, scratchPos, x, y, z, 1, 0, eastState, westState);
    }

    private static boolean isValidTopDirection(
            WorldGenLevel level,
            BlockPos.MutableBlockPos scratchPos,
            int x,
            int y,
            int z,
            int dx,
            int dz,
            BlockState neighborState,
            BlockState oppositeState
    ) {
        if (oppositeState.isCollisionShapeFullBlock(EmptyBlockGetter.INSTANCE, BlockPos.ZERO)
                || oppositeState.getBlock() instanceof SlabBlock) {
            return false;
        }
        if (!neighborState.isCollisionShapeFullBlock(EmptyBlockGetter.INSTANCE, BlockPos.ZERO)
                && !(neighborState.getBlock() instanceof SlabBlock)) {
            return false;
        }
        BlockState aboveOppositeState = level.getBlockState(scratchPos.set(x - dx, y + 1, z - dz));
        if (!aboveOppositeState.isCollisionShapeFullBlock(EmptyBlockGetter.INSTANCE, BlockPos.ZERO)) {
            return false;
        }
        BlockState belowNeighborState = level.getBlockState(scratchPos.set(x + dx, y - 1, z + dz));
        return !belowNeighborState.isCollisionShapeFullBlock(EmptyBlockGetter.INSTANCE, BlockPos.ZERO);
    }

    private void placeTopSlab(WorldGenLevel level, BlockPos pos) {
        BlockState blockAboveState = level.getBlockState(pos.above());

        Block slab = ModSlabsMap.getSlabForBlock(blockAboveState.getBlock());
        if (slab == null || slab == Blocks.AIR) return;

        boolean waterlogged = isTopStateWaterlogged(level, pos);
        BlockState slabState;

        if (ModSlabsMap.SOIL_SLAB_BLOCKS.contains(slab)) {
            slabState = ModBlocksRegistry.DIRT_SLAB.get().defaultBlockState();
        } else if (slab == ModBlocksRegistry.WARPED_NYLIUM_SLAB.get() || slab == ModBlocksRegistry.CRIMSON_NYLIUM_SLAB.get()) {
            slabState = ModBlocksRegistry.NETHERRACK_SLAB.get().defaultBlockState();
        } else {
            slabState = slab.defaultBlockState();
        }

        slabState = CustomSlab.markGenerated(slabState).setValue(BlockStateProperties.SLAB_TYPE, SlabType.TOP);
        if (slabState.hasProperty(BlockStateProperties.WATERLOGGED)) {
            slabState = slabState.setValue(BlockStateProperties.WATERLOGGED, waterlogged);
        }
        setBlockState(level, pos.immutable(), slabState);
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
    }
}

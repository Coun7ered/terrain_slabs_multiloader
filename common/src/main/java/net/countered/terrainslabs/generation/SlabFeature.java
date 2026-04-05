package net.countered.terrainslabs.generation;

import com.mojang.serialization.Codec;
import net.countered.platform.PlatformConfigHooks;
import net.countered.terrainslabs.registries.ModBlocksRegistry;
import net.countered.terrainslabs.block.ModSlabsMap;
import net.countered.terrainslabs.block.customslabs.specialslabs.CustomSlab;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SlabFeature extends Feature<NoneFeatureConfiguration> {

    public SlabFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        if (PlatformConfigHooks.isSlabGenerationEnabled()) {
            storeSlabPositions(context);
            return true;
        }
        return false;
    }

    private void storeSlabPositions(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        LevelAccessor worldAccess = context.level();
        BlockPos origin = context.origin();
        ChunkPos chunkPos = new ChunkPos(origin);
        BlockPos highestBlock = findHighestChunkPos(worldAccess, chunkPos);
        List<BlockPos> extendedPositionsGlobal = new ArrayList<>();
        List<BlockPos> botSlabPositions = new ArrayList<>();
        List<BlockPos> topSlabPositions = new ArrayList<>();

        for (int y = worldAccess.getMinBuildHeight(); y < highestBlock.getY()+1; y++) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    BlockPos currentPos = new BlockPos(chunkPos.getMinBlockX() + x, y, chunkPos.getMinBlockZ() + z);
                    if (shouldPlaceBottomSlab(worldAccess, currentPos)) {
                        botSlabPositions.add(currentPos);
                    }
                    else if (shouldPlaceTopSlab(worldAccess, currentPos)) {
                        topSlabPositions.add(currentPos);
                    }
                }
            }
        }
        botSlabPositions.addAll(extendedPositionsGlobal);
        placeSlabs(worldAccess, botSlabPositions, topSlabPositions);
    }

    private BlockPos findHighestChunkPos(LevelAccessor worldAccess, ChunkPos chunkPos) {
        BlockPos.MutableBlockPos highestChunkPos = new BlockPos.MutableBlockPos(0,0,0);
        BlockPos.MutableBlockPos testPos = new BlockPos.MutableBlockPos(0,0,0);
        // Loop through x and z within the chunk boundaries
        for (int x = chunkPos.getMinBlockX(); x <= chunkPos.getMaxBlockX(); x++) {
            for (int z = chunkPos.getMinBlockZ(); z <= chunkPos.getMaxBlockZ(); z++) {
                testPos.set(x, 0, z);
                // Get the height at this position
                BlockPos topPosition = worldAccess.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, testPos);
                // Check and update highest block
                if (highestChunkPos.getY() < topPosition.getY()) {
                    highestChunkPos.set(topPosition);
                }
            }
        }
        return highestChunkPos;
    }

    private void placeSlabs(LevelAccessor world, List<BlockPos> botSlabPositions, List<BlockPos> topSlabPositions) {
        for (BlockPos pos : botSlabPositions) placeBottomSlab(world, pos);
        for (BlockPos pos : topSlabPositions) placeTopSlab(world, pos);
    }

    /**
     * Determines if a slab should be placed at the given position based on world conditions.
     */
    private boolean shouldPlaceBottomSlab(LevelAccessor world, BlockPos currentPos) {
        BlockPos blockBelowPos = currentPos.below();
        BlockPos blockAbovePos = currentPos.above();
        BlockState blockBelowState = world.getBlockState(blockBelowPos);
        BlockState blockAboveState = world.getBlockState(blockAbovePos);
        BlockState currentBlockState = world.getBlockState(currentPos);

        if ((currentBlockState.isCollisionShapeFullBlock(world, currentPos) && !currentBlockState.is(Blocks.SNOW) && currentBlockState.canBeReplaced())
                || ModSlabsMap.getSlabForBlock(blockBelowState.getBlock()) == null
                || (!blockAboveState.is(Blocks.AIR) && !blockAboveState.is(Blocks.WATER) && !blockAboveState.is(Blocks.CAVE_AIR) && !blockAboveState.is(Blocks.VOID_AIR)))
        {
            return false;
        }
        return validSurroundingBottom(world, currentPos);
    }

    private boolean shouldPlaceTopSlab(LevelAccessor world, BlockPos currentPos) {
        BlockPos blockBelowPos = currentPos.below();
        BlockPos blockAbovePos = currentPos.above();
        BlockState blockBelowState = world.getBlockState(blockBelowPos);
        BlockState blockAboveState = world.getBlockState(blockAbovePos);
        BlockState currentBlockState = world.getBlockState(currentPos);

        if (!currentBlockState.isCollisionShapeFullBlock(world, currentPos)
                || !(blockBelowState.is(Blocks.AIR) || blockBelowState.is(Blocks.WATER) || blockBelowState.is(Blocks.CAVE_AIR) || blockBelowState.is(Blocks.VOID_AIR))
                || ModSlabsMap.getSlabForBlock(blockAboveState.getBlock()) == null)
        {
            return false;
        }
        return validSurroundingTop(world, currentPos);
    }

    private boolean validSurroundingTop(LevelAccessor world, BlockPos currentPos) {
        boolean topOfCeiling = false;
        boolean validNeighbors = false;
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos neighborPos = currentPos.relative(direction);
            BlockPos oppositePos = currentPos.relative(direction.getOpposite());
            BlockPos belowOppositePos = oppositePos.below();
            BlockState neighborState = world.getBlockState(neighborPos);
            BlockState oppositeState = world.getBlockState(oppositePos);
            BlockState belowOppositeState = world.getBlockState(belowOppositePos);

            if (neighborState.is(Blocks.GLOW_LICHEN) || neighborState.is(Blocks.LAVA)) {
                return false;
            }
            boolean isNeighborStateNotOpaque = !neighborState.isCollisionShapeFullBlock(world, neighborPos);
            boolean isOppositeStateOpaque = oppositeState.isCollisionShapeFullBlock(world, oppositePos);
            boolean isBelowOppositeStateNotOpaque = !belowOppositeState.isCollisionShapeFullBlock(world, belowOppositePos);

            if (isNeighborStateNotOpaque && isOppositeStateOpaque && isBelowOppositeStateNotOpaque) {
                topOfCeiling = true;
            }
            // Check neighboring blocks to ensure at least one horizontal neighbor is air or water
            if (neighborState.is(Blocks.AIR) || neighborState.is(Blocks.WATER) || neighborState.is(Blocks.CAVE_AIR) || neighborState.is(Blocks.VOID_AIR)) {
                validNeighbors = true;
            }
        }
        return topOfCeiling && validNeighbors;
    }

    private boolean validSurroundingBottom(LevelAccessor world, BlockPos currentPos) {
        boolean bottomOfMountain = false;
        boolean validNeighbors = false;

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos neighborPos = currentPos.relative(direction);
            BlockPos belowNeighborPos = neighborPos.below();
            BlockPos oppositePos = currentPos.relative(direction.getOpposite());
            BlockState neighborState = world.getBlockState(neighborPos);
            BlockState belowNeighborState = world.getBlockState(belowNeighborPos);
            BlockState oppositeState = world.getBlockState(oppositePos);

            if (neighborState.is(Blocks.LAVA)) return false;

            boolean isNeighborBelowOpaque = belowNeighborState.isCollisionShapeFullBlock(world, belowNeighborPos);
            boolean isOppositeDirOpaque = oppositeState.isCollisionShapeFullBlock(world, oppositePos);
            boolean isBelowNoSlab =
                    !(belowNeighborState.getBlock() instanceof SlabBlock);
            boolean isOppositeDirNoSlab =
                    !(oppositeState.getBlock() instanceof SlabBlock);
            boolean isNeighborBelowNoSnow =
                    !belowNeighborState.is(Blocks.SNOW);
            boolean isOppositeDirNoSnow =
                    !oppositeState.is(Blocks.SNOW);

            if (isNeighborBelowOpaque && isOppositeDirOpaque &&
                    isBelowNoSlab && isOppositeDirNoSlab &&
                    isNeighborBelowNoSnow && isOppositeDirNoSnow) {
                bottomOfMountain = true;
            }

            if (neighborState.isCollisionShapeFullBlock(world, neighborPos) && ModSlabsMap.getSlabForBlock(neighborState.getBlock()) != null && !neighborState.is(Blocks.SNOW)
                    && (!world.getBlockState(neighborPos.above()).isCollisionShapeFullBlock(world, neighborPos.above()) || world.getBlockState(neighborPos.above()).is(Blocks.SNOW))) {
                validNeighbors = true;
            }
        }
        if (validNeighbors && bottomOfMountain) {
            return true;
        }
        return false;
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

    private static final Set<Block> doubleTallPlants = new HashSet<>();
    static {
        doubleTallPlants.add(Blocks.TALL_GRASS);
        doubleTallPlants.add(Blocks.LARGE_FERN);
        doubleTallPlants.add(Blocks.TALL_SEAGRASS);
    }

    private void placeBottomSlab(LevelAccessor world, BlockPos pos) {
        BlockPos blockBelowPos = pos.below();
        BlockPos blockAbovePos = pos.above();
        BlockState blockAboveState = world.getBlockState(blockAbovePos);
        BlockState currentBlockState = world.getBlockState(pos);
        BlockState blockBelowState = world.getBlockState(blockBelowPos);

        if (!(currentBlockState.is(Blocks.AIR) || currentBlockState.is(Blocks.WATER) || currentBlockState.is(Blocks.CAVE_AIR) || currentBlockState.is(Blocks.VOID_AIR)  || currentBlockState.is(Blocks.LAVA))
                && !doubleTallPlants.contains(currentBlockState.getBlock())
                && !currentBlockState.is(Blocks.SNOW)
                && !currentBlockState.is(Blocks.SNOW)
                && !currentBlockState.is(Blocks.PINK_PETALS)
                && !currentBlockState.is(BlockTags.SMALL_FLOWERS))
        {
            return;
        }
        // Retrieve the slab type based on the block below the current position
        BlockState slabState = ModSlabsMap.getSlabForBlock(blockBelowState.getBlock()).defaultBlockState();

        if (slabState.getBlock().equals(Blocks.AIR)) {
            return;
        }
        // remove double tall plants
        if (doubleTallPlants.contains(currentBlockState.getBlock())) {
            if (currentBlockState.is(Blocks.TALL_SEAGRASS)) {
                setBlockState(world, blockAbovePos, Blocks.WATER.defaultBlockState());
                blockAboveState = Blocks.WATER.defaultBlockState();
            }
            else {
                setBlockState(world, blockAbovePos, Blocks.AIR.defaultBlockState());
            }
        }
        // Handle grass slab special case by converting grass to dirt before placing the slab
        if (ModSlabsMap.SOIL_SLAB_BLOCKS.contains(slabState.getBlock())) {
            setBlockState(world, blockBelowPos, Blocks.DIRT.defaultBlockState());
        }
        if (slabState.is(ModBlocksRegistry.WARPED_NYLIUM_SLAB.get()) || slabState.is(ModBlocksRegistry.CRIMSON_NYLIUM_SLAB.get())) {
            setBlockState(world,blockBelowPos, Blocks.NETHERRACK.defaultBlockState());
        }
        slabState = updateBottomWaterloggedState(currentBlockState, blockAboveState, slabState);
        setBlockState(world, pos,  slabState.setValue(CustomSlab.GENERATED, true));
    }

    private void placeTopSlab(LevelAccessor world, BlockPos pos) {
        Boolean waterlogged = isTopStateWaterlogged(world, pos);
        BlockState blockAboveState = world.getBlockState(pos.above());

        // Retrieve the slab type based on the block below the current position
        BlockState slabState = ModSlabsMap.getSlabForBlock(blockAboveState.getBlock()).defaultBlockState();

        if (slabState.getBlock().equals(Blocks.AIR)) {
            return;
        }
        if (ModSlabsMap.SOIL_SLAB_BLOCKS.contains(slabState.getBlock())) {
            slabState = ModBlocksRegistry.DIRT_SLAB.get().defaultBlockState();
        }
        if (slabState.is(ModBlocksRegistry.WARPED_NYLIUM_SLAB.get()) || slabState.is(ModBlocksRegistry.CRIMSON_NYLIUM_SLAB.get())) {
            slabState = ModBlocksRegistry.NETHERRACK_SLAB.get().defaultBlockState();
        }
        setBlockState(world, pos, slabState.setValue(CustomSlab.GENERATED, true).setValue(BlockStateProperties.SLAB_TYPE, SlabType.TOP).setValue(BlockStateProperties.WATERLOGGED, waterlogged));
    }

    private BlockState updateBottomWaterloggedState(BlockState currentBlockState, BlockState blockAboveState, BlockState slabState) {
        if (slabState.hasProperty(BlockStateProperties.WATERLOGGED)) {
            if (currentBlockState.is(Blocks.WATER) || blockAboveState.is(Blocks.WATER) || currentBlockState.is(Blocks.SEAGRASS)) {
                return slabState.setValue(BlockStateProperties.WATERLOGGED, true);
            }
        }
        return slabState;
    }

    private void setBlockState(LevelAccessor world, BlockPos pos, BlockState state) {
        world.setBlock(pos, state, 2);
    }
}


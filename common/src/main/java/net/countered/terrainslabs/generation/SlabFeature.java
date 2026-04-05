package net.countered.terrainslabs.generation;

import com.mojang.serialization.Codec;
import net.countered.platform.PlatformConfigHooks;
import net.countered.terrainslabs.block.ModSlabsMap;
import net.countered.terrainslabs.block.customslabs.specialslabs.CustomSlab;
import net.countered.terrainslabs.registries.ModBlocksRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.Objects;

public class SlabFeature extends Feature<NoneFeatureConfiguration> {

    private LevelAccessor level;
    private BlockPos origin;

    public SlabFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        if (PlatformConfigHooks.isSlabGenerationEnabled()) {
            this.level = context.level();
            this.origin = context.origin();
            generateSlabs();
            return true;
        }
        return false;
    }

    private void generateSlabs() {
        ChunkPos chunkPos = new ChunkPos(origin);
        int minY = level.getMinBuildHeight();

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int worldX = chunkPos.getMinBlockX() + x;
                int worldZ = chunkPos.getMinBlockZ() + z;
                int maxY = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, worldX, worldZ);

                for (int y = maxY; y >= minY; y--) {
                    BlockPos currentPos = new BlockPos(worldX, y, worldZ);
                    if (shouldPlaceBottomSlab(currentPos)) {
                        placeBottomSlab(currentPos);
                    } else if (shouldPlaceTopSlab(currentPos)) {
                        placeTopSlab(currentPos);
                    }
                }
            }
        }
    }

    /**
     * Determines if a slab should be placed at the given position based on world conditions.
     */
    private boolean shouldPlaceBottomSlab(BlockPos currentPos) {

        BlockState currentBlockState = level.getBlockState(currentPos);
        if (currentBlockState.isCollisionShapeFullBlock(level, currentPos)) return false;

        BlockState blockAboveState = level.getBlockState(currentPos.above());
        if (blockAboveState.isCollisionShapeFullBlock(level, currentPos.above())) return false;

        BlockState blockBelowState = level.getBlockState(currentPos.below());
        if (ModSlabsMap.getSlabForBlock(blockBelowState.getBlock()) == null) return false;

        if (!validSurroundingBottom(currentPos)) return false;

        return true;
    }


    private boolean validSurroundingBottom(BlockPos currentPos) {
        boolean validNeighbors = false;

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos neighborPos = currentPos.relative(direction);
            BlockState neighborState = level.getBlockState(neighborPos);
            BlockPos oppositePos = currentPos.relative(direction.getOpposite());
            BlockState oppositeState = level.getBlockState(oppositePos);
            BlockPos belowOppositePos = oppositePos.below();
            BlockState belowOppositeState = level.getBlockState(oppositePos.below());

            if (neighborState.is(Blocks.LAVA)) return false;

            if (neighborState.isCollisionShapeFullBlock(level, neighborPos)
                    && belowOppositeState.isCollisionShapeFullBlock(level, belowOppositePos)
                    && !oppositeState.isCollisionShapeFullBlock(level, oppositePos)
                    && ModSlabsMap.getSlabForBlock(neighborState.getBlock()) != null
                    && (!level.getBlockState(neighborPos.above()).isCollisionShapeFullBlock(level, neighborPos.above())))
            {
                validNeighbors = true;
            }
        }
        return validNeighbors;
    }

    private void placeBottomSlab(BlockPos pos) {
        BlockPos blockBelowPos = pos.below();
        BlockPos blockAbovePos = pos.above();
        BlockState blockAboveState = level.getBlockState(blockAbovePos);
        BlockState currentBlockState = level.getBlockState(pos);
        BlockState blockBelowState = level.getBlockState(blockBelowPos);

        // Retrieve the slab type based on the block below the current position
        BlockState slabState = Objects.requireNonNull(ModSlabsMap.getSlabForBlock(blockBelowState.getBlock())).defaultBlockState();

        // Handle grass slab special case by converting grass to dirt before placing the slab
        if (ModSlabsMap.SOIL_SLAB_BLOCKS.contains(slabState.getBlock())) {
            setBlockState(level, blockBelowPos, Blocks.DIRT.defaultBlockState());
        }
        if (slabState.is(ModBlocksRegistry.WARPED_NYLIUM_SLAB.get()) || slabState.is(ModBlocksRegistry.CRIMSON_NYLIUM_SLAB.get())) {
            setBlockState(level,blockBelowPos, Blocks.NETHERRACK.defaultBlockState());
        }
        slabState = updateBottomWaterloggedState(currentBlockState, blockAboveState, slabState);
        setBlockState(level, pos,  slabState.setValue(CustomSlab.GENERATED, true));
    }

    private boolean shouldPlaceTopSlab(BlockPos currentPos) {
        BlockPos blockBelowPos = currentPos.below();
        BlockPos blockAbovePos = currentPos.above();
        BlockState blockBelowState = level.getBlockState(blockBelowPos);
        BlockState blockAboveState = level.getBlockState(blockAbovePos);
        BlockState currentBlockState = level.getBlockState(currentPos);

        if (!currentBlockState.isCollisionShapeFullBlock(level, currentPos)
                || !(blockBelowState.is(Blocks.AIR) || blockBelowState.is(Blocks.WATER) || blockBelowState.is(Blocks.CAVE_AIR) || blockBelowState.is(Blocks.VOID_AIR))
                || ModSlabsMap.getSlabForBlock(blockAboveState.getBlock()) == null)
        {
            return false;
        }
        return validSurroundingTop(level, currentPos);
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

    private void placeTopSlab(BlockPos pos) {
        Boolean waterlogged = isTopStateWaterlogged(level, pos);
        BlockState blockAboveState = level.getBlockState(pos.above());

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
        setBlockState(level, pos, slabState.setValue(CustomSlab.GENERATED, true).setValue(BlockStateProperties.SLAB_TYPE, SlabType.TOP).setValue(BlockStateProperties.WATERLOGGED, waterlogged));
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
        world.setBlock(pos, state, 3);
    }
}


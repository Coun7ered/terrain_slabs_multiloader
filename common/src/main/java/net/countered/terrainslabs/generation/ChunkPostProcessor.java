package net.countered.terrainslabs.generation;

import net.countered.terrainslabs.block.ModSlabsMap;
import net.countered.terrainslabs.platform.PlatformChunkPersistence;
import net.countered.terrainslabs.platform.PlatformConfigHooks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ChunkPostProcessor {

    private final Level level;
    private final LevelChunk chunk;
    private final ChunkPos cp;
    private final LevelChunk[] neighbors;

    public ChunkPostProcessor(Level level, LevelChunk chunk) {
        this.level = level;
        this.chunk = chunk;
        this.cp = chunk.getPos();
        this.neighbors = new LevelChunk[]{
                level.getChunkSource().getChunkNow(cp.x + 1, cp.z),     // [0] +x
                level.getChunkSource().getChunkNow(cp.x - 1, cp.z),     // [1] -x
                level.getChunkSource().getChunkNow(cp.x, cp.z + 1),     // [2] +z
                level.getChunkSource().getChunkNow(cp.x, cp.z - 1),     // [3] -z
                level.getChunkSource().getChunkNow(cp.x + 1, cp.z + 1), // [4] +x+z
                level.getChunkSource().getChunkNow(cp.x + 1, cp.z - 1), // [5] +x-z
                level.getChunkSource().getChunkNow(cp.x - 1, cp.z + 1), // [6] -x+z
                level.getChunkSource().getChunkNow(cp.x - 1, cp.z - 1), // [7] -x-z
        };
    }

    public void process() {
        if (PlatformConfigHooks.getSlabRunLength() > 1) {
            generateSlabRunLength();
        }
        // Only if no slab run length is set
        else if (PlatformConfigHooks.isCornerSlabsEnabled()) {
            generateCornerSlabs();
        }
    }

    private void generateSlabRunLength() {
        for (int i = 1; i < PlatformConfigHooks.getSlabRunLength(); i++) {
            Set<BlockPos> botSlabPositions = new HashSet<>(PlatformChunkPersistence.getBotSlabPositions(chunk));
            Map<BlockPos, BlockState> toPlace = new HashMap<>();
            for (BlockPos pos : botSlabPositions) {
                for (Direction direction : Direction.values()) {
                    BlockPos offsetPos = pos.relative(direction);
                    if (!shouldPlaceBottomSlab(offsetPos)) continue;
                    Block placeSlab = ModSlabsMap.getSlabForBlock(getBlockStateAnywhere(offsetPos.below()).getBlock());
                    if (placeSlab == null) continue;
                    toPlace.put(offsetPos, placeSlab.defaultBlockState());
                }
            }
            placeBlocksInChunk(toPlace);
            PlatformChunkPersistence.setBotSlabPositions(chunk, new ArrayList<>(toPlace.keySet()));
        }
    }

    private void generateCornerSlabs() {
        Map<BlockPos, BlockState> toPlace = new HashMap<>();
        Set<BlockPos> botSlabPositions = new HashSet<>(PlatformChunkPersistence.getBotSlabPositions(chunk));

        for (BlockPos pos : botSlabPositions) {
            BlockState state = chunk.getBlockState(pos);
            if (!(state.getBlock() instanceof SlabBlock)) continue;

            int[][] diagonals = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

            for (int[] d : diagonals) {
                int nx = pos.getX() + d[0];
                int nz = pos.getZ() + d[1];
                int y = pos.getY();

                if (!botSlabPositions.contains(new BlockPos(nx, y, nz))) continue;

                BlockPos corner1 = new BlockPos(pos.getX(), y, nz);
                BlockPos corner2 = new BlockPos(nx, y, pos.getZ());

                for (BlockPos corner : new BlockPos[]{corner1, corner2}) {
                    if (!shouldPlaceBottomSlab(corner)) continue;
                    Block placeSlab = ModSlabsMap.getSlabForBlock(getBlockStateAnywhere(corner.below()).getBlock());
                    if (placeSlab == null) continue;
                    toPlace.put(corner, placeSlab.defaultBlockState());
                }
            }
        }
        placeBlocksInChunk(toPlace);
    }

    private void placeBlocksInChunk(Map<BlockPos, BlockState> toPlace) {
        for (Map.Entry<BlockPos, BlockState> entry : toPlace.entrySet()) {
            BlockPos placePos = entry.getKey();
            ChunkPos targetCp = new ChunkPos(placePos);

            if (targetCp.equals(cp)) {
                chunk.setBlockState(placePos, entry.getValue(), false);
            } else {
                LevelChunk target = getNeighborChunk(neighbors, targetCp, cp);
                if (target != null) {
                    target.setBlockState(placePos, entry.getValue(), false);
                }
            }
        }
        // clear positions after placement
        PlatformChunkPersistence.setBotSlabPositions(chunk, new ArrayList<>());
    }

    private LevelChunk getNeighborChunk(LevelChunk[] neighbors, ChunkPos target, ChunkPos origin) {
        int dx = target.x - origin.x;
        int dz = target.z - origin.z;
        if (dx ==  1 && dz ==  0) return neighbors[0];
        if (dx == -1 && dz ==  0) return neighbors[1];
        if (dx ==  0 && dz ==  1) return neighbors[2];
        if (dx ==  0 && dz == -1) return neighbors[3];
        if (dx ==  1 && dz ==  1) return neighbors[4];
        if (dx ==  1 && dz == -1) return neighbors[5];
        if (dx == -1 && dz ==  1) return neighbors[6];
        if (dx == -1 && dz == -1) return neighbors[7];
        return null;
    }

    @Nullable
    private BlockState getBlockStateAnywhere(BlockPos pos) {
        return this.getBlockStateAnywhere(pos.getX(), pos.getY(), pos.getZ());
    }

    @Nullable
    private BlockState getBlockStateAnywhere(int x, int y, int z) {
        if (x >= cp.getMinBlockX() && x <= cp.getMaxBlockX()
                && z >= cp.getMinBlockZ() && z <= cp.getMaxBlockZ()) {
            return chunk.getBlockState(new BlockPos(x, y, z));
        }

        if (x > cp.getMaxBlockX() && z > cp.getMaxBlockZ()) return neighbors[4] == null ? null : neighbors[4].getBlockState(new BlockPos(x, y, z));
        if (x > cp.getMaxBlockX() && z < cp.getMinBlockZ()) return neighbors[5] == null ? null : neighbors[5].getBlockState(new BlockPos(x, y, z));
        if (x < cp.getMinBlockX() && z > cp.getMaxBlockZ()) return neighbors[6] == null ? null : neighbors[6].getBlockState(new BlockPos(x, y, z));
        if (x < cp.getMinBlockX() && z < cp.getMinBlockZ()) return neighbors[7] == null ? null : neighbors[7].getBlockState(new BlockPos(x, y, z));

        if (x > cp.getMaxBlockX()) return neighbors[0] == null ? null : neighbors[0].getBlockState(new BlockPos(x, y, z));
        if (x < cp.getMinBlockX()) return neighbors[1] == null ? null : neighbors[1].getBlockState(new BlockPos(x, y, z));
        if (z > cp.getMaxBlockZ()) return neighbors[2] == null ? null : neighbors[2].getBlockState(new BlockPos(x, y, z));
        if (z < cp.getMinBlockZ()) return neighbors[3] == null ? null : neighbors[3].getBlockState(new BlockPos(x, y, z));

        return null;
    }

    private boolean shouldPlaceBottomSlab(BlockPos currentPos) {
        BlockState currentBlockState = getBlockStateAnywhere(currentPos.getX(), currentPos.getY(), currentPos.getZ());
        if (currentBlockState == null) return false;
        if (!currentBlockState.getCollisionShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO).isEmpty()) return false;
        BlockState blockAboveState = getBlockStateAnywhere(currentPos.getX(), currentPos.getY()+1, currentPos.getZ());
        if (blockAboveState.isCollisionShapeFullBlock(EmptyBlockGetter.INSTANCE, BlockPos.ZERO)) return false;
        BlockState blockBelowState = getBlockStateAnywhere(currentPos.getX(), currentPos.getY()-1, currentPos.getZ());;
        if (ModSlabsMap.getSlabForBlock(blockBelowState.getBlock()) == null) return false;
        if (!validSurroundingBottom(currentPos)) return false;

        return true;
    }

    private boolean validSurroundingBottom(BlockPos currentPos) {
        boolean validNeighbors = false;

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos neighborPos = currentPos.relative(direction);
            BlockPos oppositePos = currentPos.relative(direction.getOpposite());
            BlockPos belowOppositePos = oppositePos.below();
            BlockState neighborState = getBlockStateAnywhere(neighborPos);
            BlockState oppositeState = getBlockStateAnywhere(oppositePos);
            BlockState belowOppositeState = getBlockStateAnywhere(belowOppositePos);

            if (neighborState == null || oppositeState == null ||  belowOppositeState == null) continue;

            if (neighborState.is(Blocks.LAVA)) return false;

            // Neighbor must be slab
            if (!neighborState.getCollisionShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO).isEmpty()
                    && !belowOppositeState.getCollisionShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO).isEmpty() && !(belowOppositeState.getBlock() instanceof SlabBlock)
                    && !oppositeState.isCollisionShapeFullBlock(EmptyBlockGetter.INSTANCE, BlockPos.ZERO)
                    && (!getBlockStateAnywhere(neighborPos.above()).isCollisionShapeFullBlock(EmptyBlockGetter.INSTANCE, BlockPos.ZERO)))
            {
                validNeighbors = true;
            }
        }
        return validNeighbors;
    }
}

package net.countered.terrainslabs.generation;

import net.countered.terrainslabs.platform.PlatformConfigHooks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.HashMap;
import java.util.Map;

public class ChunkPostProcessor {

    private final Level level;
    private final LevelChunk chunk;

    public ChunkPostProcessor(Level level, LevelChunk chunk) {
        this.level = level;
        this.chunk = chunk;
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

    }

    private void generateCornerSlabs() {
        ChunkPos cp = chunk.getPos();

        LevelChunk[] neighbors = {
                level.getChunkSource().getChunkNow(cp.x + 1, cp.z),     // [0] +x
                level.getChunkSource().getChunkNow(cp.x - 1, cp.z),     // [1] -x
                level.getChunkSource().getChunkNow(cp.x, cp.z + 1),     // [2] +z
                level.getChunkSource().getChunkNow(cp.x, cp.z - 1),     // [3] -z
                level.getChunkSource().getChunkNow(cp.x + 1, cp.z + 1), // [4] +x+z
                level.getChunkSource().getChunkNow(cp.x + 1, cp.z - 1), // [5] +x-z
                level.getChunkSource().getChunkNow(cp.x - 1, cp.z + 1), // [6] -x+z
                level.getChunkSource().getChunkNow(cp.x - 1, cp.z - 1), // [7] -x-z
        };

        Map<BlockPos, BlockState> toPlace = new HashMap<>();

        for (int x = cp.getMinBlockX(); x <= cp.getMaxBlockX(); x++) {
            for (int z = cp.getMinBlockZ(); z <= cp.getMaxBlockZ(); z++) {
                int y = chunk.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x & 15, z & 15);
                BlockPos pos = new BlockPos(x, y - 1, z);

                BlockState state = chunk.getBlockState(pos);
                if (!(state.getBlock() instanceof SlabBlock)) continue;

                int[][] diagonals = {{1,1},{1,-1},{-1,1},{-1,-1}};

                for (int[] d : diagonals) {
                    int nx = x + d[0];
                    int nz = z + d[1];

                    BlockState neighborState = getBlockStateAnywhere(chunk, neighbors, nx, y - 1, nz, cp);
                    if (neighborState == null || !(neighborState.getBlock() instanceof SlabBlock)) continue;

                    BlockPos corner1 = new BlockPos(x,  y - 1, nz);
                    BlockPos corner2 = new BlockPos(nx, y - 1, z);

                    for (BlockPos corner : new BlockPos[]{corner1, corner2}) {
                        BlockState existing = getBlockStateAnywhere(chunk, neighbors, corner.getX(), corner.getY(), corner.getZ(), cp);
                        if (existing != null && existing.isAir()) {
                            toPlace.put(corner, state);
                        }
                    }
                }
            }
        }
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

    private BlockState getBlockStateAnywhere(LevelChunk chunk, LevelChunk[] neighbors, int x, int y, int z, ChunkPos cp) {
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
}

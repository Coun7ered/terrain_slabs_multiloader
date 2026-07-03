package net.countered.terrainslabs.mixin.snowslab;

import net.countered.terrainslabs.snowslab.SnowSlabBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Snow forming on slabs, mirroring VANILLA's own mechanism exactly.
 *
 * Vanilla forms snow in ServerLevel#tickChunk: once per chunk per tick (gated by a
 * 1-in-16 roll while raining), it picks a random column, takes the precipitation
 * heightmap top of that column, and places snow there if conditions hold. It does
 * NOT use randomTickSpeed (that's only snow MELT). So snow formation is naturally
 * gradual - the same pace for slabs as for full blocks.
 *
 * We replicate that exact path here (TAIL of tickChunk) instead of hooking the
 * private tickIceAndSnow (which can't be remapped/refmapped - it's private). Because
 * we use the same heightmap-top-of-a-random-column approach as vanilla, slabs on
 * real terrain accumulate snow at the identical rate vanilla snow does on blocks.
 *
 * Note: like vanilla, this only sees the heightmap top of each column. Floating
 * blocks the heightmap doesn't track won't get snow - which is exactly how vanilla
 * snow behaves too.
 */
@Mixin(ServerLevel.class)
public abstract class MixinTickChunkSnowScan {

    @Inject(method = "tickChunk(Lnet/minecraft/world/level/chunk/LevelChunk;I)V", at = @At("TAIL"))
    private void formSnowOnSlabsLikeVanilla(LevelChunk chunk, int randomTickSpeed, CallbackInfo ci) {
        ServerLevel level = (ServerLevel) (Object) this;
        if (!level.isRaining()) return;

        // Same cadence as vanilla's snow/ice tick: ~1 column per chunk per 16 ticks.
        if (level.random.nextInt(16) != 0) return;

        ChunkPos cp = chunk.getPos();
        // Random column within the chunk (same helper vanilla uses).
        BlockPos randomColumn = level.getBlockRandomPos(cp.getMinBlockX(), 0, cp.getMinBlockZ(), 15);

        // Precipitation heightmap top of that column = where snow would land.
        BlockPos snowPos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, randomColumn);
        BlockPos slabPos = snowPos.below();

        // Cheap pre-check, then the shared eligibility + form.
        BlockState below = level.getBlockState(slabPos);
        if (!(below.getBlock() instanceof SlabBlock)) return;

        if (SnowSlabBlock.canFormSnowOn(level, slabPos, snowPos)) {
            SnowSlabBlock.formOnSlab(level, slabPos, below);
        }
    }
}

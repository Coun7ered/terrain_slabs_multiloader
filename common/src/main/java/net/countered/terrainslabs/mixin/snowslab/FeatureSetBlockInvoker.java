package net.countered.terrainslabs.mixin.snowslab;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/** Access to Feature's protected setBlock so the freeze-feature redirect can call the
 *  original behavior for non-skipped positions. */
@Mixin(Feature.class)
public interface FeatureSetBlockInvoker {
    @Invoker("setBlock")
    void terrain_slabs$setBlock(LevelWriter level, BlockPos pos, BlockState state);
}

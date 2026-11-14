package net.countered.terrainslabs.mixin.generation;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.feature.DeltaFeature;
import net.minecraft.world.level.levelgen.feature.configurations.DeltaFeatureConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DeltaFeature.class)
public class DeltaFeatureMixin {

    @Inject(method = "isClear", at = @At("HEAD"), cancellable = true)
    private static void disableSlabRim(LevelAccessor level, BlockPos pos, DeltaFeatureConfiguration config,
                                       CallbackInfoReturnable<Boolean> cir) {

        for (Direction d : Direction.values()) {
            if (d == Direction.UP) continue;
            if (level.getBlockState(pos.relative(d)).is(BlockTags.SLABS)) {
                cir.setReturnValue(false);
                return;
            }
        }
    }
}

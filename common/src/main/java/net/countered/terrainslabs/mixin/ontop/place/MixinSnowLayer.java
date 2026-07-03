package net.countered.terrainslabs.mixin.ontop.place;

import net.countered.terrainslabs.platform.PlatformConfigHooks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SnowLayerBlock.class)
public class MixinSnowLayer {

    // Ice-family slabs that should never hold snow, matching vanilla ice/packed
    // ice. Addons (e.g. BWG black ice) add their own slabs to this tag.
    private static final TagKey<Block> ICE_SLABS = TagKey.create(
            Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("terrain_slabs", "ice_slabs"));

    // Path slabs never hold snow, matching vanilla dirt path. Addons add their
    // own path slabs (e.g. lush/sandy dirt path) to this tag.
    private static final TagKey<Block> PATH_SLABS = TagKey.create(
            Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("terrain_slabs", "path_slabs"));

    @Inject(method = "canSurvive", at = @At("HEAD"), cancellable = true)
    private void disableSnowOnSlabs(BlockState state, LevelReader level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        BlockState blockState = level.getBlockState(pos.below());

        // Ice-type slabs never hold snow, regardless of the snow-on-slabs config.
        if (blockState.is(ICE_SLABS)) {
            cir.setReturnValue(false);
            return;
        }

        // Path slabs never hold snow, matching vanilla dirt path.
        if (blockState.is(PATH_SLABS)) {
            cir.setReturnValue(false);
            return;
        }

        if (blockState.is(BlockTags.SLABS) && !PlatformConfigHooks.isSnowOnSlabsEnabled()) cir.setReturnValue(false);
    }
}

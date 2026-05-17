package net.countered.terrainslabs.mixin.ontop.render;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( BlockBehaviour.BlockStateBase.class )
public class MixinBlockStateBaseOcclusion {

    /**
     * fix for snow on slab face culling
     */
    @Inject(method = "getFaceOcclusionShape", at = @At("RETURN"), cancellable = true)
    private void terrain_slabs$reduceOcclusion(BlockGetter level, BlockPos pos, Direction direction, CallbackInfoReturnable<VoxelShape> cir) {
        BlockState state = (BlockState) (Object) this;
        if (state.getBlock() instanceof SnowLayerBlock) {
            if (direction.getAxis().isHorizontal()) {
                cir.setReturnValue(Shapes.empty());
            }
        }
    }
}

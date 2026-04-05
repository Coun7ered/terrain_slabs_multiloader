package net.countered.terrainslabs.mixin.ontop;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class MixinBlockStateBase {

    /**
     * Mixin for shifting down the visual texture of blocks on slabs
     */
    @Inject(method = "getOffset", at = @At("RETURN"), cancellable = true)
    private void terrain_slabs$getOffset(BlockGetter level, BlockPos pos, CallbackInfoReturnable<Vec3> cir) {
        BlockState state = (BlockState) (Object) this;

        if (!(state.getBlock() instanceof BushBlock || state.getBlock() instanceof SnowLayerBlock)) {
            return;
        }

        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);

        if (belowState.is(BlockTags.SLABS)) {
            if (belowState.hasProperty(SlabBlock.TYPE) && belowState.getValue(SlabBlock.TYPE) == SlabType.BOTTOM) {
                Vec3 currentOffset = cir.getReturnValue();
                cir.setReturnValue(new Vec3(currentOffset.x, -0.5, currentOffset.z));
            }
        }
    }

    /**
     * Mixin for shifting down the collision shape of blocks on slabs, but only if the offset wasn't already applied by the class itself
     */
    @Inject(method = "getShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;",
            at = @At("RETURN"),
            cancellable = true)
    private void terrain_slabs$smartShapeOffset(BlockGetter level, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
        BlockState state = (BlockState) (Object) this;

        if (!(state.getBlock() instanceof BushBlock || state.getBlock() instanceof SnowLayerBlock)) {
            return;
        }
        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);

        if (belowState.is(BlockTags.SLABS)) {
            if (belowState.hasProperty(SlabBlock.TYPE) && belowState.getValue(SlabBlock.TYPE) == SlabType.BOTTOM) {
                Vec3 offset = state.getOffset(level, pos);
                // fix for flowers moving their shape themselves
                if (offset.y < 0) {
                    VoxelShape currentShape = cir.getReturnValue();
                    if (currentShape.min(Direction.Axis.Y) >= 0) {
                        cir.setReturnValue(currentShape.move(offset.x, offset.y, offset.z));
                    }
                }
            }
        }
    }

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
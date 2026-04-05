package net.countered.terrainslabs.mixin.ontop;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
//TODO only for on slabs & fix offset to be like vanilla
    @Inject(method = "getOffset", at = @At("RETURN"), cancellable = true)
    private void terrain_slabs$getOffset(BlockGetter level, BlockPos pos, CallbackInfoReturnable<Vec3> cir) {
        BlockState state = (BlockState) (Object) this;

        // Nutze BushBlock (TallGrassBlock erbt davon, also reicht BushBlock)
        if (state.getBlock() instanceof BushBlock || state.getBlock() instanceof SnowLayerBlock) {
            BlockState belowState = level.getBlockState(pos.below());

            if (belowState.getBlock() instanceof SlabBlock && belowState.getValue(SlabBlock.TYPE) == SlabType.BOTTOM) {
                Vec3 currentOffset = cir.getReturnValue();
                // Wir setzen den Y-Offset fest auf -0.5 relativ zum Standard
                cir.setReturnValue(new Vec3(currentOffset.x, -0.5, currentOffset.z));
            }
        }
    }

    @Inject(method = "getShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;",
            at = @At("RETURN"),
            cancellable = true)
    private void terrain_slabs$smartShapeOffset(BlockGetter level, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
        BlockState state = (BlockState) (Object) this;

        if (state.getBlock() instanceof BushBlock || state.getBlock() instanceof SnowLayerBlock) {
            Vec3 offset = state.getOffset(level, pos);

            // Wir handeln nur, wenn ein negativer Y-Offset vorliegt (unser Slab-Shift)
            if (offset.y < 0) {
                VoxelShape currentShape = cir.getReturnValue();

                // LOGIK: Wenn der Shape noch bei Y=0 (oder höher) beginnt,
                // dann wurde der Offset noch NICHT angewendet (wie bei Gras).
                // Wenn er schon < 0 ist, hat die Klasse es selbst gemacht (wie deine Blumen).
                if (currentShape.min(Direction.Axis.Y) >= 0) {
                    cir.setReturnValue(currentShape.move(offset.x, offset.y, offset.z));
                }
            }
        }
    }

    // fix for snow on slab face culling
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
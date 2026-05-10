package net.countered.terrainslabs.mixin.ontop;

import net.countered.terrainslabs.block.interfaces.IOffsetState;
import net.countered.terrainslabs.util.MixinHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class MixinBlockStateBase {


    //==========//
    // Updating //
    //==========//


    @Shadow
    public abstract Block getBlock();

    /**
     *
     */
    @Inject( method = "updateShape", at = @At("TAIL") )
    private void terrain_slabs$updateOffsetOnUpdate(
            Direction direction, BlockState neighborState, LevelAccessor level,
            BlockPos pos, BlockPos neighborPos,
            CallbackInfoReturnable<BlockState> cir
    ) {
        //noinspection ConstantValue
        if ( direction != Direction.DOWN || direction != Direction.UP ) {
            return;
        }

        IOffsetState newState = (IOffsetState) level.getBlockState( pos );
        if ( !newState.terrain_slabs$hasOffsetState() ) {
            return;
        }

        if ( MixinHelper.checkOnTopState( level, pos, newState.asState() ) != newState.terrain_slabs$getOffset() ) {
            level.setBlock( pos, newState.terrain_slabs$getOppositeState(), Block.UPDATE_ALL );
        }
    }

    // TODO: This should be handled by "getStateForPlacement" in Block, but this is easy for now
    @Inject( method = "onPlace", at = @At("TAIL") )
    private void terrain_slabs$updateOffsetOnPlace(
            Level level, BlockPos pos, BlockState oldState, boolean movedByPiston, CallbackInfo ci
    ) {
        IOffsetState newState = (IOffsetState) level.getBlockState( pos );
        if ( !newState.terrain_slabs$hasOffsetState() ) {
            return;
        }

        if ( MixinHelper.checkOnTopState( level, pos, newState.asState() ) != newState.terrain_slabs$getOffset() ) {
            level.setBlock( pos, newState.terrain_slabs$getOppositeState(), Block.UPDATE_ALL );
        }
    }


    //===========//
    // Rendering //
    //===========//


    /**
     * Mixin for shifting down the visual texture of blocks on slabs
     */
    @Inject(method = "getOffset", at = @At("RETURN"), cancellable = true)
    private void terrain_slabs$getOffset(BlockGetter level, BlockPos pos, CallbackInfoReturnable<Vec3> cir) {
        IOffsetState thisState = (IOffsetState) this;
        if ( !thisState.terrain_slabs$getOffset() ) return;

        Vec3 currentOffset = cir.getReturnValue();
        cir.setReturnValue(new Vec3(currentOffset.x, -0.5, currentOffset.z));

    }

    /**
     * Mixin for shifting down the collision shape of blocks on slabs, but only if the offset wasn't already applied by the class itself
     */
    @Inject(method = "getShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;",
            at = @At("RETURN"),
            cancellable = true)
    private void terrain_slabs$smartShapeOffset(BlockGetter level, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
        IOffsetState thisState = (IOffsetState) this;
        if ( !thisState.terrain_slabs$getOffset() ) return;

        Vec3 offset = thisState.asState().getOffset(level, pos);
        // TODO: Make this more robust for XYZ offset type
        // fix for flowers moving their shape themselves
        if (offset.y < 0) {
            VoxelShape currentShape = cir.getReturnValue();
            if (currentShape.min(Direction.Axis.Y) >= 0) {
                cir.setReturnValue(currentShape.move(offset.x, offset.y, offset.z));
            }
        }
    }


    //===========//
    // Occlusion //
    //===========//


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
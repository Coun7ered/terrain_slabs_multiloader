package net.countered.terrainslabs.mixin.ontop.state;

import net.countered.terrainslabs.block.interfaces.IOffsetState;
import net.countered.terrainslabs.util.MixinHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public class MixinBlockStateBase {


    //========//
    // Update //
    //========//


    /**
     * Offset state on update as a final step
     */
    @Inject( method = "updateShape", at = @At("TAIL") )
    private void terrain_slabs$updateOffset(
            Direction direction, BlockState neighborState, LevelAccessor level,
            BlockPos pos, BlockPos neighborPos,
            CallbackInfoReturnable<BlockState> cir
    ) {
        if ( direction != Direction.DOWN && direction != Direction.UP ) {
            return;
        }

        terrain_slabs$checkAndSwitch( level, pos );
    }

    @Inject( method = "neighborChanged", at = @At("TAIL") )
    private void terrain_slabs$updateOffsetForNeighbor(
            Level level, BlockPos pos, Block neighborBlock,
            BlockPos neighborPos, boolean movedByPiston, CallbackInfo ci
    ) {
        terrain_slabs$checkAndSwitch( level, pos );
    }

    @Inject( method = "onPlace", at = @At("TAIL") )
    private void terrain_slabs$updateOffsetOnPlace(
            Level level, BlockPos pos, BlockState oldState,
            boolean movedByPiston, CallbackInfo ci
    ) {
        terrain_slabs$checkAndSwitch( level, pos );
    }

    @Unique
    private void terrain_slabs$checkAndSwitch( LevelAccessor level, BlockPos pos ) {
        IOffsetState newState = (IOffsetState) level.getBlockState( pos );
        if ( MixinHelper.shouldBeOnTopState( level, pos, (BlockState) newState ) != newState.terrain_slabs$isOffset() ) {
            level.setBlock( pos, newState.terrain_slabs$getOppositeState(), Block.UPDATE_ALL );
        }
    }


    //========//
    // Render //
    //========//


    /**
     * Mixin for shifting down the visual texture of blocks on slabs
     */
    @Inject(method = "getOffset", at = @At("RETURN"), cancellable = true)
    private void terrain_slabs$getOffset(BlockGetter level, BlockPos pos, CallbackInfoReturnable<Vec3> cir) {
        if ( !((IOffsetState) this ).terrain_slabs$isOffset() ) return;

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
        if ( !((IOffsetState) this ).terrain_slabs$isOffset() ) return;

        Vec3 offset = ( (BlockState) (Object) this ).getOffset(level, pos);
        // TODO: Make this more robust for XYZ offset type
        // fix for flowers moving their shape themselves
        if (offset.y < 0) {
            VoxelShape currentShape = cir.getReturnValue();
            if (currentShape.min(Direction.Axis.Y) >= 0) {
                cir.setReturnValue(currentShape.move(offset.x, offset.y, offset.z));
            }
        }
    }
}
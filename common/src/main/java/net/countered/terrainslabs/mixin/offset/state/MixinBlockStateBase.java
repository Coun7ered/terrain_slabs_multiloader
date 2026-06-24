package net.countered.terrainslabs.mixin.offset.state;

import net.countered.terrainslabs.block.interfaces.IOffsetState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
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

@SuppressWarnings("DataFlowIssue")
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
            BlockPos pos, BlockPos neighborPos, CallbackInfoReturnable<BlockState> cir
    ) {
        BlockState state = (BlockState) (Object) this;
        BlockState correctState = IOffsetState.getCorrectState( level, pos, state );
        if ( !state.equals( correctState ) ) {
            level.setBlock( pos, correctState, Block.UPDATE_ALL );
        }
    }

    // TODO: Find a better catchall that works for the client.
    // Fallback method, should only be used by server. Probably should replace this.
    @Inject( method = "onPlace", at = @At("TAIL") )
    private void terrain_slabs$updateOffsetOnPlace(
            Level level, BlockPos pos, BlockState oldState,
            boolean movedByPiston, CallbackInfo ci
    ) {
        BlockState newState = level.getBlockState( pos );
        BlockState correctState = IOffsetState.getCorrectState( level, pos, newState );
        if ( !newState.equals( correctState ) ) {
            level.setBlock( pos, correctState, Block.UPDATE_ALL );
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
        if ( !((IOffsetState) this ).terrain_slabs$isOffset() ) {
            return;
        }

        Vec3 currentOffset = cir.getReturnValue();
        double offset = ((IOffsetState) this ).terrain_slabs$isOffsetAbove() ? -0.5 : 0.5;
        cir.setReturnValue(new Vec3(currentOffset.x, offset, currentOffset.z));
    }

    /**
     * Mixin for shifting down the collision shape of blocks on slabs, but only if the offset wasn't already applied by the class itself
     */
    @Inject(method = "getShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;",
            at = @At("RETURN"),
            cancellable = true)
    private void terrain_slabs$smartShapeOffset(BlockGetter level, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
        IOffsetState thisState = (IOffsetState) this;
        if ( !thisState.terrain_slabs$isOffset() ) {
            return;
        }

        // fix for flowers moving their shape themselves
        Vec3 offset = ( (BlockState) (Object) this ).getOffset(level, pos);
        VoxelShape currentShape = cir.getReturnValue();
        if ( thisState.terrain_slabs$isOffsetAbove() ) {
            if ( currentShape.min(Direction.Axis.Y) >= -terrain_slabs$THRESHHOLD ) {
                cir.setReturnValue(currentShape.move(offset.x, offset.y, offset.z));
            }
        } else {
            if ( currentShape.max(Direction.Axis.Y) <= 1 + terrain_slabs$THRESHHOLD ) {
                cir.setReturnValue(currentShape.move(offset.x, offset.y, offset.z));
            }
        }
    }

    /**
     * This is the maximum value. If 3 axis offsets can exceed this, a new method is needed.
     */
    @Unique
    private static final double terrain_slabs$THRESHHOLD = 0.25;
}
package net.countered.terrainslabs.mixin.offset.place;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.countered.terrainslabs.api.IConditionalOffset;
import net.countered.terrainslabs.api.IFacedOffsetable;
import net.countered.terrainslabs.block.interfaces.IOffsetState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin( BlockItem.class )
public class MixinBlockItem {

    /**
     * Handles placement of blocks early to avoid 99% of client jittering
     */
    @WrapOperation( method = "place", at = @At( value = "INVOKE", target = "Lnet/minecraft/world/item/BlockItem;getPlacementState(Lnet/minecraft/world/item/context/BlockPlaceContext;)Lnet/minecraft/world/level/block/state/BlockState;" ))
    private BlockState terrain_slabs$offsetStateForPlacement(
            BlockItem instance, BlockPlaceContext context, Operation<BlockState> original
    ) {
        BlockState state = original.call( instance, context );
        return state == null ? null : terrain_slabs$getCorrectPlacementState( context, state );
    }

    /**
     * Handles placement of floating offsetable types which depend on placed face and do not normally change later
     */
    @Unique
    private BlockState terrain_slabs$getCorrectPlacementState( BlockPlaceContext context, BlockState state ) {
        Block block = state.getBlock();
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        if ( !(block instanceof IFacedOffsetable) ) {
            // Prevent weird overlapping
            if ( ((IOffsetState) level.getBlockState( pos.below() )).terrain_slabs$isOffsetBelow()
                    || ((IOffsetState) level.getBlockState( pos.above() )).terrain_slabs$isOffsetAbove()
            ) {
                return null;
            }

            IOffsetState.getCorrectState( level, pos, state );
        }

        if ( context.getClickedFace() == Direction.UP ) {
            if ( IOffsetState.canGenerateOntop( level, pos, state ) ) {
                return ((IOffsetState) state).terrain_slabs$getOntopState( level, pos, state );
            }
            if ( IOffsetState.onbottomStateEnabled( state ) && IConditionalOffset.allowOnbottom( level, pos, state  )
                    && ((IOffsetState) level.getBlockState( pos.below() ) ).terrain_slabs$isOffsetBelow()
            ) {
                return ((IOffsetState) state).terrain_slabs$getOnbottomState( level, pos, state );
            }
        } else if ( context.getClickedFace() == Direction.DOWN ) {
            if ( IOffsetState.canGenerateOnbottom( level, pos, state ) ) {
                return ((IOffsetState) state).terrain_slabs$getOnbottomState( level, pos, state );
            }
            if ( IOffsetState.ontopStateEnabled( state ) && IConditionalOffset.allowOntop( level, pos, state )
                    && ((IOffsetState) level.getBlockState( pos.above() ) ).terrain_slabs$isOffsetAbove()
            ) {
                return ((IOffsetState) state).terrain_slabs$getOntopState( level, pos, state );
            }
        }

        return state;
    }
}

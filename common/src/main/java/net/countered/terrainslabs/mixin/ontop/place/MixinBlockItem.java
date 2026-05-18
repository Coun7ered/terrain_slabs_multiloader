package net.countered.terrainslabs.mixin.ontop.place;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.countered.terrainslabs.block.interfaces.IOffsetState;
import net.countered.terrainslabs.util.MixinHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin( BlockItem.class )
public class MixinBlockItem {

    @WrapOperation( method = "place", at = @At( value = "INVOKE", target = "Lnet/minecraft/world/item/BlockItem;getPlacementState(Lnet/minecraft/world/item/context/BlockPlaceContext;)Lnet/minecraft/world/level/block/state/BlockState;" ))
    private BlockState terrain_slabs$offsetStateForPlacement(
            BlockItem instance, BlockPlaceContext context, Operation<BlockState> original
    ) {
        BlockState state = original.call( instance, context );
        if ( state == null || !((IOffsetState) state ).terrain_slabs$hasOffsetState() ) {
            return state;
        }

        BlockPos placePos = context.getClickedPos();
        BlockState stateAtOffset = context.getLevel().getBlockState( placePos.below() );
        if ( MixinHelper.notBottomSlab( stateAtOffset ) ) {
            return state;
        }

        return ((IOffsetState) state ).terrain_slabs$getOppositeState();
    }
}

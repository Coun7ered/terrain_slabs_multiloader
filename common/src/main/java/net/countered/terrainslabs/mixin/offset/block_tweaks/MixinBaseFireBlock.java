package net.countered.terrainslabs.mixin.offset.block_tweaks;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.countered.terrainslabs.api.IConditionalOffset;
import net.countered.terrainslabs.block.OffsetProperty;
import net.countered.terrainslabs.block.interfaces.IOffsetState;
import net.countered.terrainslabs.block.interfaces.ISlabCopy;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin( BaseFireBlock.class )
public class MixinBaseFireBlock implements IConditionalOffset {

    /**
     * Check if fire is supported from below. otherwise no offset
     */
    @Override
    public <L extends BlockGetter> boolean terrain_slabs$couldPlaceOntop(L level, BlockPos pos, BlockState state) {
        return state.is( Blocks.SOUL_FIRE ) || (
                ( !state.hasProperty( PipeBlock.NORTH ) || !state.getValue(PipeBlock.NORTH) )
                && ( !state.hasProperty( PipeBlock.EAST ) || !state.getValue(PipeBlock.EAST) )
                && ( !state.hasProperty( PipeBlock.SOUTH ) || !state.getValue(PipeBlock.SOUTH) )
                && ( !state.hasProperty( PipeBlock.WEST ) || !state.getValue(PipeBlock.WEST) )
                && ( !state.hasProperty( PipeBlock.UP ) || !state.getValue(PipeBlock.UP) )
        );
    }

    @Override
    public <L extends BlockGetter> boolean terrain_slabs$couldPlaceOnbottom(L level, BlockPos pos, BlockState state) {
        return false;
    }

    /**
     * When calling for the state below a block, pretends it's the matching full block when relevant.
     */
    @WrapOperation( method = "getState", at = @At( value = "INVOKE",
            target = "Lnet/minecraft/world/level/BlockGetter;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"
    ) )
    private static BlockState terrain_slabs$convertBlockState(
            BlockGetter instance, BlockPos offPos, Operation<BlockState> original,
            BlockGetter reader, BlockPos pos
    ) {
        BlockState stateAtOffset = original.call( instance, offPos );
        if ( ISlabCopy.notBottomSlab( stateAtOffset ) ) {
            return stateAtOffset;
        }

        return ISlabCopy.getOriginState( stateAtOffset );
    }

    @WrapOperation( method = "getState", at = {
            @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;defaultBlockState()Lnet/minecraft/world/level/block/state/BlockState;"),
            @At( value = "INVOKE", target = "Lnet/minecraft/world/level/block/FireBlock;getStateForPlacement(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;" )
    } )
    private static BlockState terrain_slabs$offsetState(
            Block instance, Operation<BlockState> original,
            BlockGetter reader, BlockPos pos
    ) {
        BlockState originState = original.call( instance );
        return IOffsetState.shouldBeOntopState( reader, pos, originState )
                ? originState.setValue( ((IOffsetState) originState).terrain_slabs$getOffsetProperty(), OffsetProperty.OffsetType.ONTOP )
                : originState;
    }
}

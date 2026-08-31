package net.countered.terrainslabs.mixin.offset.block_tweaks;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.countered.terrainslabs.api.IConditionalOffset;
import net.countered.terrainslabs.block.interfaces.ISlabCopy;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin( VineBlock.class )
public class MixinVineBlock implements IConditionalOffset {

    @Shadow
    @Final
    public static BooleanProperty UP;

    /**
     * When calling for the state below a block, pretends it's the matching full block when relevant.
     */
    @WrapOperation( method = "isAcceptableNeighbour", at = @At( value = "INVOKE",
            target = "Lnet/minecraft/world/level/BlockGetter;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"
    ) )
    private static BlockState terrain_slabs$convertBlockState(
            BlockGetter instance, BlockPos pos, Operation<BlockState> original,
            BlockGetter blockReader, BlockPos neighborPos, Direction attachedFace
    ) {
        BlockState stateAtOffset = original.call( instance, pos );
        if ( attachedFace.getAxis() != Direction.Axis.Y
                || ISlabCopy.notTopSlab( stateAtOffset )
        ) {
            return stateAtOffset;
        }

        return ISlabCopy.getOriginState( stateAtOffset );
    }

    @Override
    public <L extends BlockGetter> boolean couldBeOntop(L level, BlockPos pos, BlockState state) {
        return false;
    }

    @Override
    public <L extends BlockGetter> boolean couldBeOnbottom(L level, BlockPos pos, BlockState state) {
        return state.getValue( UP ) || level.getBlockState( pos.above() ).is( (Block)(Object) this );
    }
}

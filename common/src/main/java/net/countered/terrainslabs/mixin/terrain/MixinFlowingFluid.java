package net.countered.terrainslabs.mixin.terrain;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( FlowingFluid.class )
public abstract class MixinFlowingFluid {

    @Unique
    private static final BooleanProperty GENERATED = BooleanProperty.create("generated");
    @Unique
    private static final BooleanProperty WATERLOGGED = BooleanProperty.create("waterlogged");

    /**
     * Fluid considers trying to flow into generated slabs
     */
    @Inject( method = "canHoldFluid", at = @At( "HEAD" ), cancellable = true )
    private void terrain_slabs$onCanHoldFluid(
            BlockGetter level, BlockPos pos, BlockState state,
            Fluid fluid, CallbackInfoReturnable<Boolean> cir
    ) {
        if ( state.getBlock() instanceof SlabBlock && state.getProperties().contains( GENERATED )
                && state.getValue( GENERATED ) && state.getValue( SlabBlock.TYPE ).equals( SlabType.BOTTOM )
                && state.getValue( WATERLOGGED ).equals( false )
        ) {
            cir.setReturnValue( true );
            cir.cancel();
        }
    }

    /**
     * Fluid flows into generated slabs if the fluid level is high enough
     * @param level world
     * @param pos pos
     * @param blockState blockState flowing into
     * @param direction flow direction
     * @param fluidState fluidState
     * @param ci context
     */
    @Inject( method = "spreadTo", at = @At( "HEAD" ), cancellable = true )
    void terrain_slabs$onSpreadTo(
            LevelAccessor level, BlockPos pos, BlockState blockState,
            Direction direction, FluidState fluidState, CallbackInfo ci
    ) {
        if ( !blockState.getProperties().contains( GENERATED ) || !blockState.getValue( GENERATED ) ) {
            return;
        }
        if ( direction == Direction.DOWN || fluidState.getAmount() >= 4 ) {
            level.destroyBlock( pos, false );
            level.setBlock( pos, fluidState.createLegacyBlock(), Block.UPDATE_ALL );
            ci.cancel();
        }
    }
}

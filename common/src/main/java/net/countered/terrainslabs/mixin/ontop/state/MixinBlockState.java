package net.countered.terrainslabs.mixin.ontop.state;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import net.countered.terrainslabs.block.interfaces.IOffsetState;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Create unique blockstates for offset blocks
 */
@Mixin( BlockState.class )
public abstract class MixinBlockState implements IOffsetState, Cloneable {
    @Unique
    protected boolean terrain_slabs$isOffset = false;
    @Unique
    protected BlockState terrain_slabs$oppositeState = null;

    @Override
    public boolean terrain_slabs$getOffset() {
        return terrain_slabs$isOffset;
    }
    @Override
    public boolean terrain_slabs$hasOffsetState() {
        return terrain_slabs$oppositeState != null;
    }
    @Override
    public BlockState terrain_slabs$getOppositeState() {
        return terrain_slabs$oppositeState;
    }

    @SuppressWarnings("SameParameterValue")
    @Unique
    protected MixinBlockState terrain_slabs$setOffset( boolean bool ) {
        terrain_slabs$isOffset = bool;
        return this;
    }
    @Unique
    protected MixinBlockState terrain_slabs$setOppositeState( BlockState state ) {
        terrain_slabs$oppositeState = state;
        return this;
    }

    @Override
    public MixinBlockState clone() {
        try {
            return (MixinBlockState) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    // TODO: Restrict which blocks get states (tag will not work for this...)
    @SuppressWarnings("DataFlowIssue")
    @Inject( method = "<init>", at = @At("RETURN"))
    private void terrain_slabs$offsetAppender(
            Block block, @SuppressWarnings("rawtypes") ImmutableMap immutableMap,
            @SuppressWarnings("rawtypes") MapCodec mapCodec, CallbackInfo ci
    ) {
        MixinBlockState newState = this;
//        if ( !MixinHelper.terrain_slabs$isStateValidOnTop( newState ) ) {
//            return;
//        }

        newState.terrain_slabs$setOppositeState( (BlockState) (Object) newState.clone()
                .terrain_slabs$setOffset( true )
                .terrain_slabs$setOppositeState( (BlockState) (Object) newState )
        );
    }
}

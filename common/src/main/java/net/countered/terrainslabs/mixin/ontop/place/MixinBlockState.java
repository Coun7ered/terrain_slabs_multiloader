package net.countered.terrainslabs.mixin.ontop.place;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import net.countered.terrainslabs.block.interfaces.IOffsetState;
import net.countered.terrainslabs.util.MixinHelper;
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
    private boolean terrain_slabs$isOffset = false;
    @Unique
    private BlockState terrain_slabs$oppositeState = null;

    @SuppressWarnings("SameParameterValue")
    @Unique
    private MixinBlockState setTerrain_slabs$isOffset( boolean bool ) {
        terrain_slabs$isOffset = bool;
        return this;
    }
    @Unique
    private MixinBlockState setTerrain_slabs$oppositeState(BlockState state ) {
        terrain_slabs$oppositeState = state;
        return this;
    }

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

    @SuppressWarnings("DataFlowIssue")
    @Inject( method = "<init>", at = @At("TAIL"))
    private void terrain_slabs$offsetAppender(
            Block block, @SuppressWarnings("rawtypes") ImmutableMap immutableMap,
            @SuppressWarnings("rawtypes") MapCodec mapCodec, CallbackInfo ci
    ) {
        BlockState newState = (BlockState) (Object) this;
        if ( !MixinHelper.terrain_slabs$isStateValidOnTop( newState ) ) {
            return;
        }

        terrain_slabs$oppositeState = (BlockState) (Object) this.clone().setTerrain_slabs$isOffset( true ).setTerrain_slabs$oppositeState( newState );
    }

    @Override
    public MixinBlockState clone() {
        try {
            return (MixinBlockState) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}

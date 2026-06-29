package net.countered.terrainslabs.mixin.offset.state;

import net.countered.terrainslabs.api.ICustomOffsetConversion;
import net.countered.terrainslabs.block.OffsetProperty;
import net.countered.terrainslabs.block.interfaces.IOffsetState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Include helpful methods in blockstates by implementing IOffsetState
 */
@Mixin( BlockState.class )
public class MixinBlockState implements IOffsetState {

    @Override
    public boolean terrain_slabs$isOffsetAbove() {
        BlockState state = ((BlockState) (Object) this );
        Property<?> offsetProperty = this.terrain_slabs$getOffsetProperty();
        return offsetProperty != null && state.getValue( offsetProperty ) == OffsetProperty.OffsetType.ONTOP;
    }
    @Override
    public boolean terrain_slabs$isOffsetBelow() {
        BlockState state = ((BlockState) (Object) this );
        Property<?> offsetProperty = this.terrain_slabs$getOffsetProperty();
        return offsetProperty != null && state.getValue( offsetProperty ) == OffsetProperty.OffsetType.ONBOTTOM;
    }
    @Override
    public boolean terrain_slabs$isOffset() {
        BlockState state = ((BlockState) (Object) this );
        Property<?> offsetProperty = this.terrain_slabs$getOffsetProperty();
        return offsetProperty != null && state.getValue( offsetProperty ) != OffsetProperty.OffsetType.NONE;
    }

    @Override
    public boolean terrain_slabs$hasOntopState() {
        BlockState state = ((BlockState) (Object) this );
        return state.hasProperty( OffsetProperty.ONTOP) || state.hasProperty( OffsetProperty.ALL );
    }
    @Override
    public boolean terrain_slabs$hasOnbottomState() {
        BlockState state = ((BlockState) (Object) this );
        return state.hasProperty( OffsetProperty.ONBOTTOM) || state.hasProperty( OffsetProperty.ALL );
    }
    @Override
    public boolean terrain_slabs$hasOffsetState() {
        return this.terrain_slabs$getOffsetProperty() != null;
    }

    @Override
    public EnumProperty<OffsetProperty.OffsetType> terrain_slabs$getOffsetProperty() {
        BlockState state = ((BlockState) (Object) this );
        return OffsetProperty.getPropertyOf( state );
    }

    @Override
    public BlockState terrain_slabs$getNormalState() {
        BlockState state = ((BlockState) (Object) this );
        if ( this.terrain_slabs$isOffset() ) {
            return state.setValue( this.terrain_slabs$getOffsetProperty(), OffsetProperty.OffsetType.NONE );
        }

        return state;
    }

    @Override
    public <L extends LevelAccessor> BlockState terrain_slabs$getOntopState(L level, BlockPos pos, BlockState initialState ) {
        BlockState state = ((BlockState) (Object) this );
        if ( this.terrain_slabs$getOffsetProperty() == OffsetProperty.ONBOTTOM ) {
            return null;
        }

        if ( this instanceof ICustomOffsetConversion conversion ) {
            state = conversion.onSetOntop( level, pos, state );
        }

        return state.setValue( this.terrain_slabs$getOffsetProperty(), OffsetProperty.OffsetType.ONTOP );
    }
    @Override
    public <L extends LevelAccessor> BlockState terrain_slabs$getOnbottomState( L level, BlockPos pos, BlockState initialState ) {
        BlockState state = ((BlockState) (Object) this );
        if ( this.terrain_slabs$getOffsetProperty() == OffsetProperty.ONTOP) {
            return null;
        }

        if ( this instanceof ICustomOffsetConversion conversion ) {
            state = conversion.onSetOnbottom( level, pos, state );
        }

        return state.setValue( this.terrain_slabs$getOffsetProperty(), OffsetProperty.OffsetType.ONBOTTOM );
    }
}

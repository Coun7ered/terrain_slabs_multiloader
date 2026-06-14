package net.countered.terrainslabs.mixin.offset.state;

import net.countered.terrainslabs.block.OffsetProperty;
import net.countered.terrainslabs.block.interfaces.IOffsetState;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Include helpful methods in blockstates
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
        if ( state.hasProperty( OffsetProperty.ONTOP) || state.hasProperty( OffsetProperty.ALL ) ) {
            return true;
        }
        return false;
    }
    @Override
    public boolean terrain_slabs$hasOnbottomState() {
        BlockState state = ((BlockState) (Object) this );
        if ( state.hasProperty( OffsetProperty.ONBOTTOM) || state.hasProperty( OffsetProperty.ALL ) ) {
            return true;
        }
        return false;
    }
    @Override
    public boolean terrain_slabs$hasOffsetState() {
        return this.terrain_slabs$getOffsetProperty() != null;
    }

    @Override
    public EnumProperty<OffsetProperty.OffsetType> terrain_slabs$getOffsetProperty() {
        BlockState state = ((BlockState) (Object) this );
        if ( state.hasProperty( OffsetProperty.ONTOP) ) {
            return OffsetProperty.ONTOP;
        }
        if ( state.hasProperty( OffsetProperty.ONBOTTOM) ) {
            return OffsetProperty.ONBOTTOM;
        }
        if ( state.hasProperty( OffsetProperty.ALL ) ) {
            return OffsetProperty.ALL;
        }

        return null;
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
    public BlockState terrain_slabs$getOntopState() {
        BlockState state = ((BlockState) (Object) this );
        if ( this.terrain_slabs$getOffsetProperty() != OffsetProperty.ONBOTTOM) {
            return state.setValue( this.terrain_slabs$getOffsetProperty(), OffsetProperty.OffsetType.ONTOP );
        }

        return null;
    }
    @Override
    public BlockState terrain_slabs$getOnbottomState() {
        BlockState state = ((BlockState) (Object) this );
        if ( this.terrain_slabs$getOffsetProperty() != OffsetProperty.ONTOP) {
            return state.setValue( this.terrain_slabs$getOffsetProperty(), OffsetProperty.OffsetType.ONBOTTOM );
        }

        return null;
    }

//    @Override
//    public BlockState terrain_slabs$getOppositeOntopState() {
//        BlockState state = ((BlockState) (Object) this );
//        EnumProperty<?> property = this.terrain_slabs$getOffsetProperty();
//        if ( this.terrain_slabs$isOffsetAbove() ) {
//            return state.setValue( this.terrain_slabs$getOffsetProperty(), OffsetProperty.OffsetType.NONE );
//        }
//
//        if ( property == OffsetProperty.ONTOP || property == OffsetProperty.ALL ) {
//            return state.setValue( this.terrain_slabs$getOffsetProperty(), OffsetProperty.OffsetType.ONTOP );
//        }
//
//        return null;
//    }
}

package net.countered.terrainslabs.mixin.ontop.state;

import net.countered.terrainslabs.block.OffsetProperty;
import net.countered.terrainslabs.block.interfaces.IOffsetState;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Include helpful methods
 */
@Mixin( BlockState.class )
public class MixinBlockState implements IOffsetState {

    @Override
    public boolean terrain_slabs$isOffset() {
        BlockState state = ((BlockState) (Object) this );
        return this.terrain_slabs$hasOffsetState() ?
                state.getValue( this.terrain_slabs$getOffsetProperty() ) != OffsetProperty.OffsetType.NONE
                : false;
    }

    @Override
    public boolean terrain_slabs$hasOffsetState() {
        return this.terrain_slabs$getOffsetProperty() != null;
    };

    @Override
    public EnumProperty<OffsetProperty.OffsetType> terrain_slabs$getOffsetProperty() {
        BlockState state = ((BlockState) (Object) this );
        if ( state.hasProperty( OffsetProperty.ONTOP) ) {
            return OffsetProperty.ONTOP;
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
    public BlockState terrain_slabs$getOffsetState() {
        BlockState state = ((BlockState) (Object) this );
        if ( this.terrain_slabs$getOffsetProperty() == OffsetProperty.ONTOP) {
            return state.setValue( this.terrain_slabs$getOffsetProperty(), OffsetProperty.OffsetType.ONTOP );
        }

        return null;
    }

    @Override
    public BlockState terrain_slabs$getOppositeState() {
        BlockState state = ((BlockState) (Object) this );
        if ( this.terrain_slabs$isOffset() ) {
            return state.setValue( this.terrain_slabs$getOffsetProperty(), OffsetProperty.OffsetType.NONE );
        }

        if ( this.terrain_slabs$getOffsetProperty() == OffsetProperty.ONTOP) {
            return state.setValue( this.terrain_slabs$getOffsetProperty(), OffsetProperty.OffsetType.ONTOP );
        }

        return null;
    }
}

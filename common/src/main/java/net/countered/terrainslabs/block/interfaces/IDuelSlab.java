package net.countered.terrainslabs.block.interfaces;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A fragile slab which reverts to a "duel" state when covered (particularly grass)
 */
public interface IDuelSlab extends ISlabCopy {

    static BlockState getDuelState( BlockState state ) {
        if ( !( state.getBlock() instanceof IDuelSlab ) ) {
            throw new IllegalArgumentException( "Cannot get duel state for BlockState of block not extending IDuelSlab" );
        }
        return (( IDuelSlab ) state.getBlock() ).getDuelBlock().withPropertiesOf( state );
    }

    ISlabCopy getDuel();

    default Block getDuelBlock() {
        return getDuel().getOriginBlock();
    }

}
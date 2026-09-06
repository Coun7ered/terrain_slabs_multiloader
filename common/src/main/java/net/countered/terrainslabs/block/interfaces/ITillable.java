package net.countered.terrainslabs.block.interfaces;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Like duel slab, but for farmland.
 */
public interface ITillable extends ISlabCopy {

    static BlockState getTilledState( BlockState state ) {
        if ( !( state.getBlock() instanceof ITillable ) ) {
            throw new IllegalArgumentException( "Cannot get tilled state for BlockState of block not extending ITillable" );
        }
        return (( ITillable ) state.getBlock() ).getTilledBlock().withPropertiesOf( state );
    }

    ISlabCopy getTilled();

    default Block getTilledBlock() {
        return getTilled().getOriginBlock();
    }
}

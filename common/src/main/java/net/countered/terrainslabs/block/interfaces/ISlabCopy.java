package net.countered.terrainslabs.block.interfaces;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public interface ISlabCopy {

    static BlockState getOriginState( BlockState state ) {
        if ( !( state.getBlock() instanceof ISlabCopy ) ) {
            throw new IllegalArgumentException( "Cannot get origin state for BlockState of Block not extending ISlabCopy" );
        }
        return (( ISlabCopy ) state.getBlock() ).getOriginBlock().withPropertiesOf( state );
    }

    Block getOriginBlock();

    default Block getBlock() {
        return ( Block ) this;
    }

}

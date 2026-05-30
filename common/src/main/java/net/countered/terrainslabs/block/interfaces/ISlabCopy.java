package net.countered.terrainslabs.block.interfaces;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public interface ISlabCopy {

    static BlockState getOriginState( BlockState state ) {
        if ( !( state.getBlock() instanceof ISlabCopy ) ) {
            throw new IllegalArgumentException( "Cannot get origin state for BlockState of Block not extending ISlabCopy" );
        }
        return (( ISlabCopy ) state.getBlock() ).getOriginBlock().withPropertiesOf( state );
    }

    default boolean matches( ISlabCopy slab ) {
        if ( this.getOriginBlock() == slab.getOriginBlock() ) {
            return true;
        }

        if ( this instanceof IDuelSlab duelSlab ) {
            return duelSlab.getDuelBlock() == slab.getOriginBlock();
        }

        if ( slab instanceof IDuelSlab duelSlab ) {
            return duelSlab.getDuelBlock() == this.getOriginBlock();
        }

        return false;
    }

    Block getOriginBlock();

    default Item getOriginItem() {
        return this.getOriginBlock().asItem();
    }

    default Block getBlock() {
        return ( Block ) this;
    }

}

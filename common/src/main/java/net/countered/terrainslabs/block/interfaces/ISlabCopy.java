package net.countered.terrainslabs.block.interfaces;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;

public interface ISlabCopy {

    static BlockState getOriginState( BlockState state ) {
        if ( !( state.getBlock() instanceof ISlabCopy ) ) {
            throw new IllegalArgumentException( "Cannot get origin state for BlockState of Block not extending ISlabCopy" );
        }
        return (( ISlabCopy ) state.getBlock() ).getOriginBlock().withPropertiesOf( state );
    }

    static boolean notBottomSlab(BlockState state) {
        return !( state.getBlock() instanceof ISlabCopy) || state.getValue( SlabBlock.TYPE ) != SlabType.BOTTOM;
    }
    static boolean notTopSlab(BlockState state) {
        return !( state.getBlock() instanceof ISlabCopy) || state.getValue( SlabBlock.TYPE ) != SlabType.TOP;
    }

    static boolean isBottomSlab(BlockState state) {
        return state.getBlock() instanceof ISlabCopy && state.getValue( SlabBlock.TYPE ) == SlabType.BOTTOM;
    }
    static boolean isTopSlab(BlockState state) {
        return state.getBlock() instanceof ISlabCopy && state.getValue( SlabBlock.TYPE ) == SlabType.TOP;
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

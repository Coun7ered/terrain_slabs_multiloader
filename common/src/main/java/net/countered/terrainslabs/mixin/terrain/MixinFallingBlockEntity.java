package net.countered.terrainslabs.mixin.terrain;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.countered.terrainslabs.block.customslabs.specialslabs.CustomSlab;
import net.countered.terrainslabs.block.interfaces.IDuelSlab;
import net.countered.terrainslabs.block.interfaces.ISlabCopy;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin( FallingBlockEntity.class )
public abstract class MixinFallingBlockEntity {

    /**
     * This method allows the correct item to be dropped by falling slabs and landing on slabs
     * @param instance falling block entity which is being removed
     * @param itemLike object that will convert to the item dropped by default
     * @return Item entity that will be spawned
     */
    @WrapOperation( method = "tick", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/item/FallingBlockEntity;spawnAtLocation(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/entity/item/ItemEntity;")
    )
    private ItemEntity terrain_slabs$dropItemProxy(FallingBlockEntity instance, ItemLike itemLike, Operation<ItemEntity> original) {
        // block copies have their own listener to handle this.
        BlockState fallingBlockState = instance.getBlockState();
        Level level = instance.level();
        BlockPos belowPos = instance.blockPosition();
        BlockState belowState = level.getBlockState( belowPos );

        if ( fallingBlockState.getBlock() instanceof ISlabCopy slabCopy
                && belowState.getBlock() instanceof ISlabCopy copy2
                && slabCopy.matches( copy2 )
        ) {
            return original.call( instance, Blocks.AIR );
        }

        if ( !terrain_slabs$canPlaceOn( level, belowPos, belowState ) ) {
            return original.call( instance, itemLike );
        }

        Block belowBlock = belowState.getBlock() instanceof IDuelSlab duelSlab
                ? duelSlab.getDuelBlock()
                : ( (ISlabCopy) belowState.getBlock() ).getOriginBlock();


        level.setBlockAndUpdate( belowPos, belowBlock.withPropertiesOf( belowState ) );
        level.setBlockAndUpdate( belowPos.above(), fallingBlockState );
        return original.call( instance, Blocks.AIR );
    }

    @Unique
    private static boolean terrain_slabs$canPlaceOn(Level level, BlockPos belowPos, BlockState belowState ) {
        BlockState currentStateAtPos = level.getBlockState( belowPos.above() );
        if ( !( currentStateAtPos.is( BlockTags.REPLACEABLE ) || currentStateAtPos.isAir()
                || currentStateAtPos.is( Blocks.WATER ) )
        ) {
            return false;
        }

        return belowState.getProperties().contains(CustomSlab.GENERATED) && belowState.getValue(CustomSlab.GENERATED);
    }
}

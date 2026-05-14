package net.countered.terrainslabs.mixin.ontop.place;

import net.countered.terrainslabs.block.customslabs.specialslabs.CustomSlab;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;

@Mixin(FallingBlockEntity.class)
public abstract class MixinFallingBlock extends Entity {
    private MixinFallingBlock(){
        super(null, null);
    }
    
    /**
     * Modifiyes the blockstate and block pos so that if it lands on a custom slab it updates the slab and makes it land on the block above
     * @param blockState
     * @param blockPos
     * @return
     */
    @ModifyVariable(method = "tick", at = @At("STORE"))
    private BlockState modifyIfLandingOnCustomSlab(BlockState blockState, @Local LocalRef<BlockPos> blockPos){
        if(blockState.getBlock() instanceof CustomSlab){
            if(blockState.getValue(CustomSlab.GENERATED)){
                this.level().setBlock(blockPos.get(), ((CustomSlab)blockState.getBlock()).getEquivlentFullBlock(blockState), 3);
                blockPos.set(blockPos.get().above());
                return this.level().getBlockState(blockPos.get());
            }else{
                return blockState;
            }
        }
        return blockState;
    }
}

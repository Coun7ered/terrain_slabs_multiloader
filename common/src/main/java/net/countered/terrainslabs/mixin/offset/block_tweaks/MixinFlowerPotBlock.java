package net.countered.terrainslabs.mixin.offset.block_tweaks;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.countered.terrainslabs.block.interfaces.IOffsetState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin( FlowerPotBlock.class )
public class MixinFlowerPotBlock {

    @WrapOperation( method = "use", at = @At( value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;defaultBlockState()Lnet/minecraft/world/level/block/state/BlockState;" ) )
    private BlockState terrain_slabs$offsetState(
            Block instance, Operation<BlockState> original,
            BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit
    ) {
        BlockState originState = original.call( instance );
        return IOffsetState.shouldBeOntopState( level, pos, originState )
                ? ((IOffsetState) originState).terrain_slabs$getOntopState( level, pos, originState ) : originState;
    }
}














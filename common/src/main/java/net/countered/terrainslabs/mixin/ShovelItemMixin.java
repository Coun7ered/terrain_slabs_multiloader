/*
package net.countered.terrainslabs.mixin;


import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.block.SlabBlock.TYPE;
import static net.minecraft.block.SlabBlock.WATERLOGGED;

@Mixin(ShovelItem.class)
public abstract class ShovelItemMixin {

    @Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
    private void useOnCustomSlab(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);

        if (context.getSide() == Direction.DOWN) {
            return;
        }

        BlockState pathState = ShovelItemAccessor.getPathStates().get(blockState.getBlock());

        // Check if it's a slab block registered for path conversion
        if (pathState != null && blockState.getBlock() instanceof SlabBlock && world.getBlockState(blockPos.up()).isAir()) {
            PlayerEntity playerEntity = context.getPlayer();
            SlabType slabType = blockState.get(TYPE);
            pathState = pathState.with(TYPE, slabType).with(WATERLOGGED, blockState.get(WATERLOGGED));

            world.playSound(playerEntity, blockPos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);
            if (!world.isClient) {
                world.setBlockState(blockPos, pathState, 11);
                world.emitGameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Emitter.of(playerEntity, pathState));
                if (playerEntity != null) {
                    context.getStack().damage(1, playerEntity, (p) -> {
                        p.sendToolBreakStatus(context.getHand());
                    });
                }
            }

            cir.setReturnValue(ActionResult.SUCCESS);
        }
    }
}

 */

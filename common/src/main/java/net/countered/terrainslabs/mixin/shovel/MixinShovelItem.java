package net.countered.terrainslabs.mixin.shovel;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShovelItem.class)
public abstract class MixinShovelItem {

    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    private void useOnCustomSlab(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        BlockState blockState = level.getBlockState(blockPos);
        if (context.getClickedFace() == Direction.DOWN) {
            return;
        }
        Player player = context.getPlayer();
        BlockState pathState = ShovelItemAccessor.getFlattenables().get(blockState.getBlock());
        // Check if it's a slab block registered for path conversion
        if (pathState != null && blockState.getBlock() instanceof SlabBlock && level.getBlockState(blockPos.above()).isAir()) {
            SlabType slabType = blockState.getValue(BlockStateProperties.SLAB_TYPE);
            pathState = pathState.setValue(BlockStateProperties.SLAB_TYPE, slabType).setValue(BlockStateProperties.WATERLOGGED, blockState.getValue(BlockStateProperties.WATERLOGGED));
            level.playSound(player, blockPos, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0F, 1.0F);
            if (!level.isClientSide) {
                level.setBlock(blockPos, pathState, 11);
                level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(player, pathState));
                if (player != null) {
                    context.getItemInHand().hurtAndBreak(1, player, (p) -> {
                        p.broadcastBreakEvent(context.getHand());
                    });
                }
            }

            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }
}

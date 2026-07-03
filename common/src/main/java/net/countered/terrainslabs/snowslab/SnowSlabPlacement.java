package net.countered.terrainslabs.snowslab;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.InteractionEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.sounds.SoundSource;

/**
 * Lets a player convert a bare bottom slab into a snow slab by using a snow item on
 * it (snowball or snow block), mirroring how snow forms from weather but on demand
 * and without the weather/sky gates.
 *
 * Registered from common init: SnowSlabPlacement.register();
 */
public final class SnowSlabPlacement {

    private SnowSlabPlacement() {}

    public static void register() {
        InteractionEvent.RIGHT_CLICK_BLOCK.register(SnowSlabPlacement::onRightClick);
    }

    private static EventResult onRightClick(Player player, InteractionHand hand, BlockPos pos,
                                            net.minecraft.core.Direction face) {
        Level level = player.level();
        ItemStack held = player.getItemInHand(hand);

        // Only the snow LAYER item triggers this (vanilla-consistent).
        if (!held.is(Items.SNOW)) return EventResult.pass();

        // The clicked block must be an eligible bare bottom slab.
        if (!SnowSlabBlock.canPlaceSnowManually(level, pos)) return EventResult.pass();

        if (!level.isClientSide && level instanceof ServerLevel server) {
            SnowSlabBlock.formOnSlab(server, pos, level.getBlockState(pos));
            level.playSound(null, pos, SoundType.SNOW.getPlaceSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
            if (!player.getAbilities().instabuild) {
                held.shrink(1);
            }
        }
        // Consume the interaction so vanilla doesn't ALSO place a snow layer on top.
        return EventResult.interruptTrue();
    }
}

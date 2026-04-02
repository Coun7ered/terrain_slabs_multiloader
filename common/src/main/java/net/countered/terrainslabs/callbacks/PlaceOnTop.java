package net.countered.terrainslabs.callbacks;


import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.InteractionEvent;
import net.countered.terrainslabs.data.ontop.PlaceableItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;

public class PlaceOnTop {

    public static void registerPlaceOnTopCallback() {
        InteractionEvent.RIGHT_CLICK_BLOCK.register((player, hand, hitResult, direction) -> {
            if (player.level().isClientSide) {
                return EventResult.pass();
            }

            ItemStack item = player.getItemInHand(hand);
            if (item.isEmpty()) {
                return EventResult.pass();
            }

            BlockPos clickedPos = hitResult;
            BlockState clickedState = player.level().getBlockState(clickedPos);

            if (!(clickedState.getBlock() instanceof SlabBlock)) {
                return EventResult.pass();
            }

            if (!clickedState.getValue(BlockStateProperties.SLAB_TYPE).equals(SlabType.BOTTOM)) {
                return EventResult.pass();
            }

            BlockPos placePos = clickedPos.above();
            BlockState upState = player.level().getBlockState(placePos);

            if (!PlaceableItemRegistry.isRegistered(item.getItem())) {
                return EventResult.pass();
            }

            PlaceableItemRegistry.PlaceableItemData data = PlaceableItemRegistry.get(item.getItem());

            switch (data.type()) {
                case SNOW -> {
                    if ((upState.isAir() || upState.getBlock() == data.onTopBlock())
                            && (upState.getBlock() instanceof SnowLayerBlock || upState.isAir())) {

                        int currentLayers = upState.getBlock() instanceof SnowLayerBlock
                                ? upState.getValue(SnowLayerBlock.LAYERS)
                                : 0;

                        if (currentLayers < SnowLayerBlock.MAX_HEIGHT) {
                            player.level().setBlock(placePos, data.getBlock().defaultBlockState()
                                    .setValue(SnowLayerBlock.LAYERS, currentLayers + 1), 3);

                            player.level().playSound(null, placePos, SoundEvents.SNOW_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                            if (!player.isCreative()) {
                                item.shrink(1);
                            }
                            return EventResult.interruptTrue();
                        }
                    }
                }
                case AQUATIC -> {
                    if (upState.is(Blocks.WATER)) {
                        player.level().setBlock(placePos, data.getBlock().defaultBlockState(), 3);
                        player.level().playSound(null, placePos, SoundEvents.WET_GRASS_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);

                        if (!player.isCreative()) {
                            item.shrink(1);
                        }
                        return EventResult.interruptTrue();
                    }
                }
                case VEGETATION -> {
                    if (upState.isAir()) {
                        player.level().setBlock(placePos, data.getBlock().defaultBlockState(), 3);
                        player.level().playSound(null, placePos, SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);

                        if (!player.isCreative()) {
                            item.shrink(1);
                        }
                        return EventResult.interruptTrue();
                    }
                }
            }

            return EventResult.pass();
        });
    }
}

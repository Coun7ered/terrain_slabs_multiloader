package net.countered.terrainslabs.fabric.mixin.client;

import net.countered.terrainslabs.snowslab.SnowSlabBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Region-aware pick-block (middle-click) for the snow slab (Fabric client):
 *   - Aiming at the SNOW portion -> pick a snow layer item (Items.SNOW).
 *   - Aiming at the BASE SLAB portion -> pick the stored slab (the normal
 *     getCloneItemStack result).
 *
 * We redirect the getCloneItemStack call inside Minecraft#pickBlock and, when the
 * crosshair is on our block's snow region, substitute the snow item. Everything else
 * (other blocks, the slab region) passes through unchanged.
 */
@Mixin(Minecraft.class)
public abstract class MixinMinecraftPickBlock {

    @Redirect(
        method = "pickBlock",
        at = @At(value = "INVOKE",
                 target = "Lnet/minecraft/world/level/block/Block;getCloneItemStack(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/world/item/ItemStack;"))
    private ItemStack pickByRegion(Block block, LevelReader level, BlockPos pos, BlockState state) {
        ItemStack original = block.getCloneItemStack(level, pos, state);

        if (block instanceof SnowSlabBlock) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.hitResult instanceof BlockHitResult bhr
                    && bhr.getType() == HitResult.Type.BLOCK
                    && bhr.getBlockPos().equals(pos)
                    && mc.player != null) {
                // Reuse the shared region check (snow region -> snow item).
                if (SnowSlabBlock.isAimingAtSnowRegion(mc.player.level(), pos, state, mc.player)) {
                    return new ItemStack(Items.SNOW);
                }
            }
        }
        return original;
    }
}

package net.countered.terrainslabs.mixin.ontop.place;

import net.countered.terrainslabs.block.customslabs.soilslabs.GrassSlab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Vanilla {@link GrassBlock#performBonemeal} scatters short grass and flowers in
 * a radius, but only continues its outward walk over positions whose block below
 * {@code is(this)} — i.e. the vanilla grass block itself. That excludes our grass
 * slabs, so bonemealing a real grass block never spreads vegetation onto adjacent
 * grass slabs.
 *
 * <p>This redirect augments that single check: when the scatter loop asks whether
 * the block below a candidate position is the grass block, we also answer true if
 * it is one of our {@link GrassSlab}s (the base grass slab or the BWG lush grass
 * slab). The other {@code is(...)} call in the method compares against short grass,
 * not the grass block, so guarding on the redirected block keeps this scoped to
 * the ground check only.
 */
@Mixin(GrassBlock.class)
public abstract class MixinGrassBlock {

    @Redirect(
            method = "performBonemeal",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"))
    private boolean terrain_slabs$grassOrGrassSlab(BlockState testedState, Block compareTo) {
        // Vanilla result first: preserves both the grass-ground check and the
        // unrelated short-grass comparison exactly as-is.
        if (testedState.is(compareTo)) {
            return true;
        }
        // Broaden ONLY the ground check (the comparison whose target is the grass
        // block performing the bonemeal); the other call compares against short
        // grass, which is not a GrassBlock, so it is unaffected.
        if (compareTo instanceof GrassBlock) {
            return testedState.getBlock() instanceof GrassSlab;
        }
        return false;
    }
}

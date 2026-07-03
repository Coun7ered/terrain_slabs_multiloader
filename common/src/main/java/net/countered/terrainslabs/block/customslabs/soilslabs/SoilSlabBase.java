package net.countered.terrainslabs.block.customslabs;

import net.minecraft.world.level.block.state.BlockState;

/**
 * Implemented by soil-type slabs (grass, podzol, mycelium, path, and addon
 * variants such as overgrown stone / podzol dacite) so that world generation
 * and break logic can ask the slab what material it actually sits on, instead
 * of assuming dirt.
 *
 * <p>Defaults for all built-in soil slabs are dirt, preserving vanilla
 * behaviour. Addon slabs whose underlying material is something else (e.g.
 * stone for overgrown stone) override these to return that block.
 */
public interface SoilSlabBase {

    /**
     * The full block placed directly beneath this slab when it generates, and
     * the block whose particles/sound are used when it breaks. Defaults to dirt
     * for vanilla soil slabs.
     */
    BlockState baseFullBlock();

    /**
     * The slab used for the "top" generated variant and as the reversion target.
     * Defaults to the mod's dirt slab for vanilla soil slabs.
     */
    BlockState baseSlabBlock();
}

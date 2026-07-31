package net.countered.terrainslabs.block.customslabs.soilslabs;

import net.minecraft.world.level.block.state.BlockState;

/**
 * Implemented by soil-type slabs (grass, podzol, and addon variants) to tell
 * the world-gen {@code SlabFeature} which base material they sit on.
 *
 * <p>Slabs registered through {@code ModSlabsMap.registerSoilSlab} that do not
 * implement this interface fall back to vanilla dirt / the dirt slab.
 */
public interface ISoilSlab {

    /**
     * The full block placed beneath this slab at world-gen time (e.g. lush
     * dirt under a lush grass slab, dacite under an overgrown dacite slab).
     */
    BlockState baseFullBlock();

    /**
     * The plain slab used when world-gen generates this slab's top variant,
     * and that the slab reverts to when it loses its surface layer.
     */
    BlockState baseSlabBlock();
}

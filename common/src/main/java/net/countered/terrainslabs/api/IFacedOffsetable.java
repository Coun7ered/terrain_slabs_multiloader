package net.countered.terrainslabs.api;

/**
 * Labeling interface for things that only offset when placed on a slab face. Particularly for offsetables that can float.
 * <p>
 * Prevents unsightly updates.
 * <p>
 * Note: on this branch the placement-time offset engine does not exist yet, so this
 * marker has no effect; it is reserved so implementations stay source-compatible
 * with the 1.20.1 API.
 */
@SuppressWarnings("SpellCheckingInspection")
public interface IFacedOffsetable {}

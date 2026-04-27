package net.countered.terrainslabs.platform.neoforge;

import eu.midnightdust.lib.config.MidnightConfig;


public class PlatformConfigHooksImpl extends MidnightConfig {
    public static final String GENERATION = "generation";
    public static final String ON_SLAB = "on_slab";
    public static final String RENDERING = "rendering";

    @Entry(category = GENERATION)
    public static boolean enableSlabGeneration = true;

    public static boolean isSlabGenerationEnabled() {
        return enableSlabGeneration;
    }

    @Comment(category = GENERATION, centered = true) public static Comment experimental;

    @Entry(category = GENERATION)
    public static boolean enableCornerSlabs = true;

    public static boolean isCornerSlabsEnabled() {
        return enableCornerSlabs;
    }

    @Entry(category = GENERATION, min = 1, max = 3)
    public static int slabRunLength = 1;

    public static int getSlabRunLength() {
        return slabRunLength;
    }

    @Entry(category = ON_SLAB)
    public static boolean enableVegetationOnSlabs = true;

    public static boolean isVegetationOnSlabsEnabled() {
        return enableVegetationOnSlabs;
    }

    @Entry(category = ON_SLAB)
    public static boolean enableSnowOnSlabs = true;

    public static boolean isSnowOnSlabsEnabled() {
        return enableSnowOnSlabs;
    }

    @Entry(category = RENDERING, isSlider = true, min = 0, max = 1f)
    public static float adjustSlabAo = 0.5f;

    public static float getAoStrength() {
        return adjustSlabAo;
    }
}

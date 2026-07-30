package net.countered.terrainslabs.platform.neoforge;

import eu.midnightdust.lib.config.MidnightConfig;

public class PlatformConfigHooksImpl extends MidnightConfig {

    public static final String GENERATION = "generation";

    @Entry(category = GENERATION)
    public static boolean enableSlabGeneration = true;
    public static boolean isSlabGenerationEnabled() {
        return enableSlabGeneration;
    }

    @Entry(category = GENERATION)
    public static boolean enableVegetationOnSlabs = true;
    public static boolean isVegetationOnSlabsEnabled() {
        return enableVegetationOnSlabs;
    }

    @Entry(category = GENERATION)
    public static boolean enableSnowOnSlabs = true;
    public static boolean isSnowOnSlabsEnabled() {
        return enableSnowOnSlabs;
    }

    @Entry(category = GENERATION)
    public static boolean enableCornerSlabs = false;
    public static boolean isCornerSlabsEnabled() {
        return enableCornerSlabs;
    }

    @Entry(category = GENERATION, isSlider = true, min = 1, max = 8)
    public static int slabRunLength = 2;
    public static int getSlabRunLength() {
        return slabRunLength;
    }
}

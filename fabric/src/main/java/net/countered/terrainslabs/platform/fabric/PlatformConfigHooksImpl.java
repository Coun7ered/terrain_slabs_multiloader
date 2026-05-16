package net.countered.terrainslabs.platform.fabric;

import eu.midnightdust.lib.config.MidnightConfig;

public class PlatformConfigHooksImpl extends MidnightConfig {

    public static final String GENERATION = "generation";
    public static final String RENDERING = "rendering";

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
    public static boolean enableExperimentalFeatures = false;

    @Comment(category = GENERATION, name = "Experimental Features:")
    public static Comment needsExperimentalEnabled;

    @Condition(requiredOption = "enableExperimentalFeatures", visibleButLocked = true)
    @Entry(category = GENERATION)
    public static boolean enableCornerSlabs = false;
    public static boolean isCornerSlabsEnabled() {
        return enableCornerSlabs;
    }

    @Condition(requiredOption = "enableExperimentalFeatures", visibleButLocked = true)
    @Entry(category = GENERATION, isSlider = true, min = 1, max = 8)
    public static int slabRunLength = 1;
    public static int getSlabRunLength() {
        return slabRunLength;
    }

    @Entry(category = RENDERING, isSlider = true, min = 0, max = 1f)
    public static float adjustSlabAo = 0.5f;
    public static float getAoStrength() {
        return adjustSlabAo;
    }
}

package net.countered.platform.forge;

import eu.midnightdust.lib.config.MidnightConfig;

public class PlatformConfigHooksImpl extends MidnightConfig {

    public static final String GENERATION = "generation";

    @Entry(category = GENERATION)
    public static boolean enableSlabGeneration = true;

    public static boolean isSlabGenerationEnabled() {
        return enableSlabGeneration;
    }
}

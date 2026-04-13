package net.countered.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class PlatformConfigHooks {

    @ExpectPlatform
    public static boolean isSlabGenerationEnabled() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isVegetationOnSlabsEnabled() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isSnowOnSlabsEnabled() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static float getAoStrength() {
        throw new AssertionError();
    }
}

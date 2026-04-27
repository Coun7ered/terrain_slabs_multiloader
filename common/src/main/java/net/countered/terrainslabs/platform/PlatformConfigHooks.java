package net.countered.terrainslabs.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class PlatformConfigHooks {

    @ExpectPlatform
    public static boolean isSlabGenerationEnabled() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getSlabRunLength() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isCornerSlabsEnabled() {
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

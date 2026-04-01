package net.countered.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class PlatformConfigHooks {

    @ExpectPlatform
    public static boolean isSlabGenerationEnabled() {
        throw new AssertionError();
    }
}

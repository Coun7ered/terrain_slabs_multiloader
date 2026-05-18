package net.countered.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.level.block.Block;

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

    @ExpectPlatform
    public static boolean includeOntop( Block b )  {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean excludeOnTop( Block b )  {
        throw new AssertionError();
    }
}

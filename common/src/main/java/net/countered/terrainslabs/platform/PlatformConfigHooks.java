package net.countered.terrainslabs.platform;

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
    public static boolean doFluidsDestroyGeneration() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean canFireBlocksOffset() { throw new AssertionError(); }

    @ExpectPlatform
    public static float getAoStrength() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean includeOntop( Block b )  {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean excludeOntop(Block b )  {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean includeOnbottom( Block b )  {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean excludeOnbottom( Block b )  {
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
}

package net.countered.terrainslabs.platform.fabric;

import com.google.common.collect.Lists;
import eu.midnightdust.lib.config.MidnightConfig;
import net.countered.terrainslabs.util.BlockChecker;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class PlatformConfigHooksImpl extends MidnightConfig {

    public static final String GENERATION = "generation";
    public static final String RENDERING = "rendering";
    public static final String PLACEMENT = "placement";

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
    public static boolean fluidsDestroyGeneration = true;
    public static boolean doFluidsDestroyGeneration() {
        return fluidsDestroyGeneration;
    }

    @Entry(category = GENERATION)
    public static boolean fireBlocksOffset = false;
    public static boolean canFireBlocksOffset() {
        return fireBlocksOffset;
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

    @Entry( category = PLACEMENT )
    public static List<String> ontopIncludeBlocks = Lists.newArrayList();
    private static BlockChecker getOntopIncludeBlocksHash;
    public static boolean includeOntop( Block b ) {
        if ( ontopIncludeBlocks.isEmpty() ) {
            return false;
        }
        if ( getOntopIncludeBlocksHash == null ) {
            getOntopIncludeBlocksHash = new BlockChecker( ontopIncludeBlocks );
        }

        return getOntopIncludeBlocksHash.contains( b );
    }

    @Entry( category = PLACEMENT )
    public static List<String> onbottomIncludeBlocks = Lists.newArrayList();
    private static BlockChecker getOnbottomIncludeBlocksHash;
    public static boolean includeOnbottom( Block b ) {
        if ( onbottomIncludeBlocks.isEmpty() ) {
            return false;
        }
        if ( getOnbottomIncludeBlocksHash == null ) {
            getOnbottomIncludeBlocksHash = new BlockChecker( onbottomIncludeBlocks );
        }

        return getOnbottomIncludeBlocksHash.contains( b );
    }

    @Entry( category = PLACEMENT )
    public static List<String> ontopExcludeBlocks = Lists.newArrayList();
    private static BlockChecker getOntopExcludeBlocksHash;
    public static boolean excludeOntop(Block b ) {
        if ( ontopExcludeBlocks.isEmpty() ) {
            return false;
        }
        if ( getOntopExcludeBlocksHash == null ) {
            getOntopExcludeBlocksHash = new BlockChecker( ontopExcludeBlocks );
        }

        return getOntopExcludeBlocksHash.contains( b );
    }

    @Entry( category = PLACEMENT )
    public static List<String> onbottomExcludeBlocks = Lists.newArrayList();
    private static BlockChecker getOnbottomExcludeBlocksHash;
    public static boolean excludeOnbottom( Block b ) {
        if ( onbottomExcludeBlocks.isEmpty() ) {
            return false;
        }
        if ( getOnbottomExcludeBlocksHash == null ) {
            getOnbottomExcludeBlocksHash = new BlockChecker( onbottomExcludeBlocks );
        }

        return getOnbottomExcludeBlocksHash.contains( b );
    }
}

package net.countered.platform.fabric;

import com.google.common.collect.Lists;
import eu.midnightdust.lib.config.MidnightConfig;
import net.countered.terrainslabs.util.BlockChecker;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.List;

/**
 * TODO: Permanent todo: if modifying fields labeled @Entry, corresponding fields must be updated:
 * net.countered.terrainslabs.MixinApplier.TerrainSlabsMixinPlugin.ConfigReader
 */
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

    @Entry(category = RENDERING, isSlider = true, min = 0, max = 1f)
    public static float adjustSlabAo = 0.5f;

    public static float getAoStrength() {
        return adjustSlabAo;
    }

    @Entry( category = PLACEMENT )
    public static List<ResourceLocation> ontopIncludeBlocks = Lists.newArrayList();

    private static BlockChecker ontopIncludeBlocksHash;
    public static boolean includeOntop( Block b ) {
        if ( ontopIncludeBlocksHash == null ) {
            ontopIncludeBlocksHash = new BlockChecker( ontopIncludeBlocks );
        }

        return ontopIncludeBlocksHash.contains( b );
    }

    @Entry( category = PLACEMENT )
    public static List<ResourceLocation> ontopExcludeBlocks = Lists.newArrayList();

    private static BlockChecker getOntopExcludeBlocksHash;
    public static boolean excludeOnTop( Block b ) {
        if ( getOntopExcludeBlocksHash == null ) {
            getOntopExcludeBlocksHash = new BlockChecker( ontopExcludeBlocks );
        }

        return getOntopExcludeBlocksHash.contains( b );
    }
}

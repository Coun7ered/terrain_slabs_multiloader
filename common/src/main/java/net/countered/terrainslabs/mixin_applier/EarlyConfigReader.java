package net.countered.terrainslabs.mixin_applier;

import com.google.gson.*;
import dev.architectury.platform.Platform;
import net.countered.terrainslabs.TerrainSlabs;
import net.countered.terrainslabs.util.BlockChecker;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Configs need to be read early in Fabric without midnight lib for some mixin functionality.
 */
public final class EarlyConfigReader {
    private static final Path CONFIG_PATH = Platform.getConfigFolder().resolve( TerrainSlabs.MOD_ID + ".json" );
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static final ConfigFormat CTS_CONFIGS = loadConfigs();
    public static final BlockChecker ONTOP_INCLUDE = new BlockChecker( CTS_CONFIGS.ontopIncludeBlocks );
    public static final BlockChecker ONTOP_EXCLUDE = new BlockChecker( CTS_CONFIGS.ontopExcludeBlocks );
    public static final BlockChecker ONBOTTOM_INCLUDE = new BlockChecker( CTS_CONFIGS.onbottomIncludeBlocks );
    public static final BlockChecker ONBOTTOM_EXCLUDE = new BlockChecker( CTS_CONFIGS.onbottomExcludeBlocks );

    private static ConfigFormat loadConfigs() {
        try {
            ConfigFormat config = new ConfigFormat(gson.fromJson(Files.newBufferedReader( CONFIG_PATH ), ConfigFormat.class ));
            Files.write( CONFIG_PATH, gson.toJson( config, ConfigFormat.class ).getBytes() );

            return config;
        } catch ( Exception e ) {
            Logger.getAnonymousLogger().info( "Countered's Terrain Slabs unable to read configs early: {}" + e );

            // Gives default values. Midnight can handle writing a new config file.
            return new ConfigFormat();
        }
    }

    public record ConfigFormat(
            boolean enableSlabGeneration, boolean enableVegetationOnSlabs,
            boolean enableSnowOnSlabs, boolean fluidsDestroyGeneration, boolean fireBlocksOffset,
            boolean enableExperimentalFeatures, boolean enableCornerSlabs, int slabRunLength, float adjustSlabAo,
            List<String> ontopIncludeBlocks, List<String> ontopExcludeBlocks,
            List<String> onbottomIncludeBlocks, List<String> onbottomExcludeBlocks
    ) {
        // Default Config
        public ConfigFormat() {
            this(
                    true, true, true, true, false, false, false, 1, 0.5f,
                    new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()
            );
        }

        // Repair Config
        public ConfigFormat( ConfigFormat format ) {
            this(
                    format.enableSlabGeneration,
                    format.enableVegetationOnSlabs,
                    format.enableSnowOnSlabs,
                    format.fluidsDestroyGeneration,
                    format.fireBlocksOffset,
                    format.enableExperimentalFeatures,
                    format.enableCornerSlabs,
                    format.slabRunLength > 0 ? format.slabRunLength : 1,
                    format.adjustSlabAo > 0.0f && format.adjustSlabAo <= 1.0f ? format.adjustSlabAo : 0.5f,
                    format.ontopIncludeBlocks != null ? format.ontopIncludeBlocks : new ArrayList<>(),
                    format.ontopExcludeBlocks != null ? format.ontopExcludeBlocks : new ArrayList<>(),
                    format.onbottomIncludeBlocks != null ? format.onbottomIncludeBlocks : new ArrayList<>(),
                    format.onbottomExcludeBlocks != null ? format.onbottomExcludeBlocks : new ArrayList<>()
            );
        }
    }
}

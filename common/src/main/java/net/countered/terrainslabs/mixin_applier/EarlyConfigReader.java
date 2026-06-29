package net.countered.terrainslabs.mixin_applier;

import com.google.gson.*;
import dev.architectury.platform.Platform;
import net.countered.terrainslabs.TerrainSlabs;
import net.countered.terrainslabs.util.BlockChecker;
import net.minecraft.resources.ResourceLocation;

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
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter( ResourceLocation.class, new ResourceLocation.Serializer() )
            .create();

    public static final ConfigFormat CTS_CONFIGS = loadConfigs();
    public static final BlockChecker ONTOP_INCLUDE = new BlockChecker( CTS_CONFIGS.ontopIncludeBlocks );
    public static final BlockChecker ONTOP_EXCLUDE = new BlockChecker( CTS_CONFIGS.ontopExcludeBlocks );
    public static final BlockChecker ONBOTTOM_INCLUDE = new BlockChecker( CTS_CONFIGS.onbottomIncludeBlocks );
    public static final BlockChecker ONBOTTOM_EXCLUDE = new BlockChecker( CTS_CONFIGS.onbottomExcludeBlocks );

    private static ConfigFormat loadConfigs() {
        try {
            return gson.fromJson(Files.newBufferedReader( CONFIG_PATH ), ConfigFormat.class );
        } catch ( Exception e ) {
            Logger.getAnonymousLogger().info( "Countered's Terrain Slabs unable to read configs early: {}" + e );

            // Gives default values
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
        public ConfigFormat() {
            this(
                    true, true, true, true, false, false, false, 1, 0.5f,
                    new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()
            );
        }
    }
}

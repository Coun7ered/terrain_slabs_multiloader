package net.countered.terrainslabs.mixin_applier;

import com.google.gson.*;
import dev.architectury.platform.Platform;
import net.countered.terrainslabs.TerrainSlabs;
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
    private static final Path filePath = Platform.getConfigFolder().resolve( TerrainSlabs.MOD_ID + ".json" );
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter( ResourceLocation.class,
                    new ResourceLocation.Serializer()
            ).create();

    public static final ConfigFormat CTS_CONFIGS = load();

    private static ConfigFormat load() {
        try {
            return gson.fromJson(Files.newBufferedReader( filePath ), ConfigFormat.class );
        } catch ( Exception e ) {
            Logger.getAnonymousLogger().info( "Countered's Terrain Slabs unable to read configs early: {}" + e );

            // Gives default values
            return new ConfigFormat();
        }
    }

    public record ConfigFormat(
            boolean enableSlabGeneration, boolean enableVegetationOnSlabs, boolean enableSnowOnSlabs,
            float adjustSlabAo, List<String> ontopIncludeBlocks, List<String> ontopExcludeBlocks
    ) {
        public ConfigFormat() {
            this(
                    true, true, true, 0.5f,
                    new ArrayList<>(), new ArrayList<>()
            );
        }
    }
}

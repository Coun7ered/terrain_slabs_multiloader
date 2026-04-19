package net.countered.terrainslabs.neoforge;

import eu.midnightdust.lib.config.MidnightConfig;
import net.countered.platform.neoforge.PlatformConfigHooksImpl;
import net.countered.terrainslabs.TerrainSlabs;
import net.countered.terrainslabs.neoforge.feature.ModFeatures;
import net.countered.terrainslabs.registries.FlattenableBlockRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(TerrainSlabs.MOD_ID)
public final class TerrainSlabsNeoForge {
    public TerrainSlabsForge(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(TerrainSlabs.MOD_ID, modEventBus);

        ModFeatures.FEATURES.register(modEventBus);
        MidnightConfig.init(TerrainSlabs.MOD_ID, PlatformConfigHooksImpl.class);

        // Run our common setup.
        TerrainSlabs.init();

        modEventBus.addListener(this::setup);
    }

    private void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            FlattenableBlockRegistry.apply();
        });
    }
}
package net.countered.terrainslabs.neoforge;

import eu.midnightdust.lib.config.MidnightConfig;
import net.countered.terrainslabs.TerrainSlabs;
import net.countered.terrainslabs.neoforge.client.NeoForgeBuiltinPacks;
import net.countered.terrainslabs.neoforge.client.TerrainSlabsNeoForgeClient;
import net.countered.terrainslabs.neoforge.feature.ModFeatures;
import net.countered.terrainslabs.platform.neoforge.PlatformConfigHooksImpl;
import net.countered.terrainslabs.registries.FlattenableBlockRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;

@Mod(TerrainSlabs.MOD_ID)
public final class TerrainSlabsNeoForge {

    public TerrainSlabsNeoForge(IEventBus modBus) {
        MidnightConfig.init(TerrainSlabs.MOD_ID, PlatformConfigHooksImpl.class);
        ModFeatures.FEATURES.register(modBus);

        modBus.addListener(this::setupServer);
        modBus.addListener(this::setupClient);
        modBus.addListener(this::setupPack);

        // Run our common setup.
        TerrainSlabs.init();
    }

    private void setupServer(FMLCommonSetupEvent event) {
        event.enqueueWork(FlattenableBlockRegistry::apply);
    }

    private void setupClient(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            TerrainSlabsNeoForgeClient.init(event);
        });
    }

    private void setupPack(AddPackFindersEvent event) {
        NeoForgeBuiltinPacks.addPack(event);
    }
}
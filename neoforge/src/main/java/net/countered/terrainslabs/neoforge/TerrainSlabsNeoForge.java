package net.countered.terrainslabs.neoforge;

import eu.midnightdust.lib.config.MidnightConfig;
import net.countered.terrainslabs.TerrainSlabs;
import net.countered.terrainslabs.generation.ChunkPostProcessor;
import net.countered.terrainslabs.neoforge.client.NeoForgeBuiltinPacks;
import net.countered.terrainslabs.neoforge.client.TerrainSlabsNeoForgeClient;
import net.countered.terrainslabs.neoforge.feature.ModFeatures;
import net.countered.terrainslabs.platform.neoforge.PlatformConfigHooksImpl;
import net.countered.terrainslabs.registries.FlattenableBlockRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;

@Mod(TerrainSlabs.MOD_ID)
public final class TerrainSlabsNeoForge {

    public TerrainSlabsNeoForge(IEventBus modBus) {
        MidnightConfig.init(TerrainSlabs.MOD_ID, PlatformConfigHooksImpl.class);
        ModFeatures.FEATURES.register(modBus);

        modBus.addListener(this::setupServer);
        modBus.addListener(this::setupClient);
        modBus.addListener(this::setupPack);
        NeoForge.EVENT_BUS.addListener(this::onChunkLoad);
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

    private void onChunkLoad(ChunkEvent.Load event) {
        if (!event.isNewChunk()) return;
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        if (!(event.getChunk() instanceof LevelChunk chunk)) return;
        new ChunkPostProcessor(level, chunk).process();
    }
}
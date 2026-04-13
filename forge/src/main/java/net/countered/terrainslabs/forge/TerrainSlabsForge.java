package net.countered.terrainslabs.forge;

import dev.architectury.platform.forge.EventBuses;
import eu.midnightdust.lib.config.MidnightConfig;
import net.countered.platform.forge.PlatformConfigHooksImpl;
import net.countered.terrainslabs.TerrainSlabs;
import net.countered.terrainslabs.forge.feature.ModFeatures;
import net.countered.terrainslabs.registries.FlattenableBlockRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(TerrainSlabs.MOD_ID)
public final class TerrainSlabsForge {
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

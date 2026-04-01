package net.countered.terrainslabs.forge;

import dev.architectury.platform.forge.EventBuses;
import eu.midnightdust.lib.config.MidnightConfig;
import net.countered.platform.forge.PlatformConfigHooksImpl;
import net.countered.terrainslabs.TerrainSlabs;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(TerrainSlabs.MOD_ID)
public final class TerrainSlabsForge {
    public TerrainSlabsForge() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(TerrainSlabs.MOD_ID, modEventBus);

        ModFeatures.FEATURES.register(modEventBus);
        MidnightConfig.init(TerrainSlabs.MOD_ID, PlatformConfigHooksImpl.class);

        // Run our common setup.
        TerrainSlabs.init();
    }
}

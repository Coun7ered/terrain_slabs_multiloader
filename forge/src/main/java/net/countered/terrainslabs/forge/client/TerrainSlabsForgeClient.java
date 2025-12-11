package net.countered.terrainslabs.forge.client;

import net.countered.platform.ClientPlatformHooks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class TerrainSlabsForgeClient {

    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
        ClientPlatformHooks.set(new ClientPlatformHooksForge());
        ClientPlatformHooks.get().registerRenderLayers();
        ClientPlatformHooks.get().registerBlockColorProviders();
        ClientPlatformHooks.get().registerItemColorProviders();
    }
}

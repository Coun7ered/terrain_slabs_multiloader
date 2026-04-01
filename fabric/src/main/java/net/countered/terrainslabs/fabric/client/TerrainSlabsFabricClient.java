package net.countered.terrainslabs.fabric.client;

import net.countered.platform.PlatformClientHooks;
import net.fabricmc.api.ClientModInitializer;

public final class TerrainSlabsFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        PlatformClientHooks.registerRenderLayers();
        PlatformClientHooks.registerBlockColorProviders();
        PlatformClientHooks.registerItemColorProviders();
        PlatformClientHooks.registerBuiltinResourcePacks();
    }
}

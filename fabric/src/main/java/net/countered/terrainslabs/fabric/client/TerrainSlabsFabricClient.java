package net.countered.terrainslabs.fabric.client;

import net.countered.platform.ClientPlatformHooks;
import net.fabricmc.api.ClientModInitializer;

public final class TerrainSlabsFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        ClientPlatformHooks.set(new ClientPlatformHooksFabric());
        ClientPlatformHooks.get().registerRenderLayers();
        ClientPlatformHooks.get().registerBlockColorProviders();
        ClientPlatformHooks.get().registerItemColorProviders();
        ClientPlatformHooks.get().registerBuiltinResourcePacks();
    }
}

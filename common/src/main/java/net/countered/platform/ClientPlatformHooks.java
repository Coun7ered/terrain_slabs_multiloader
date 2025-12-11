package net.countered.platform;

public abstract class ClientPlatformHooks {
    private static ClientPlatformHooks INSTANCE;

    public void registerRenderLayers() {};
    public void registerBlockColorProviders(){};
    public void registerItemColorProviders(){};
    public void registerBuiltinResourcePacks(){};

    public static ClientPlatformHooks get() {
        return INSTANCE;
    }
    public static void set(ClientPlatformHooks impl) {
        INSTANCE = impl;
    }
}

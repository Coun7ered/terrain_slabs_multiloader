package net.countered.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class PlatformClientHooks {

    @ExpectPlatform
    public static void registerRenderLayers() {};
    @ExpectPlatform
    public static void registerBlockColorProviders(){};
    @ExpectPlatform
    public static void registerItemColorProviders(){};
    @ExpectPlatform
    public static void registerBuiltinResourcePacks(){};
}

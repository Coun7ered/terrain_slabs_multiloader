package net.countered.terrainslabs.forge;

import dev.architectury.platform.forge.EventBuses;
import net.countered.terrainslabs.TerrainSlabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(TerrainSlabs.MOD_ID)
public final class TerrainSlabsForge {
    public TerrainSlabsForge() {
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(TerrainSlabs.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        // Run our common setup.
        TerrainSlabs.init();
    }
}

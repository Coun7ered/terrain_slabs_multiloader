package net.countered.terrainslabs.fabric;

import eu.midnightdust.lib.config.MidnightConfig;
import net.countered.terrainslabs.TerrainSlabs;
import net.countered.terrainslabs.fabric.feature.ModAddedFeatures;
import net.countered.terrainslabs.model.SlabOffsetModel;
import net.countered.terrainslabs.platform.fabric.PlatformConfigHooksImpl;
import net.countered.terrainslabs.registries.FlattenableBlockRegistry;
import net.countered.terrainslabs.util.MixinHelper;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;

public final class TerrainSlabsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // Run our common setup.
        TerrainSlabs.init();

        ModAddedFeatures.registerFeatures();
        MidnightConfig.init(TerrainSlabs.MOD_ID, PlatformConfigHooksImpl.class);
        FlattenableBlockRegistry.apply();

        ModelLoadingPlugin.register(context -> {
            context.modifyBlockModelAfterBake().register((state, context1) -> {
                if (MixinHelper.terrain_slabs$isStateValidOnTop(context1.state())) {
                    return new SlabOffsetModel(state);
                }
                return state;
            });
        });
    }
}

package net.countered.terrainslabs.fabric;

import eu.midnightdust.lib.config.MidnightConfig;
import net.countered.terrainslabs.TerrainSlabs;
import net.countered.terrainslabs.fabric.feature.ModAddedFeatures;
import net.countered.terrainslabs.fabric.model.SlabOffsetModel;
import net.countered.terrainslabs.platform.fabric.PlatformConfigHooksImpl;
import net.countered.terrainslabs.registries.ModFlattenablesRegistry;
import net.countered.terrainslabs.registries.ModTillableRegistry;
import net.countered.terrainslabs.util.OnTopHelper;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;

public final class TerrainSlabsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // Run our common setup.
        TerrainSlabs.init();

        ModFlattenablesRegistry.registerFlattenables();
        ModTillableRegistry.registerTillables();

        ModAddedFeatures.registerFeatures();
        MidnightConfig.init(TerrainSlabs.MOD_ID, PlatformConfigHooksImpl.class);

        ModelLoadingPlugin.register(context -> {
            context.modifyBlockModelAfterBake().register((state, context1) -> {
                if (OnTopHelper.terrain_slabs$isStateValidOnTop(context1.state())) {
                    return new SlabOffsetModel(state);
                }
                return state;
            });
        });
    }
}

package net.countered.terrainslabs.fabric.client;

import net.countered.terrainslabs.TerrainSlabs;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.resources.ResourceLocation;

/**
 * Wraps the snow slab's baked layer models with SnowSlabFabricModel so the bottom
 * half renders the stored slab. Call register() from the Fabric client initializer.
 */
public final class SnowSlabModelRegistration {

    private SnowSlabModelRegistration() {}

    public static void register() {
        ModelLoadingPlugin.register(ctx -> {
            ctx.modifyModelAfterBake().register((model, modelCtx) -> {
                if (model == null) return model;
                ResourceLocation id = modelCtx.resourceId();
                if (id != null
                        && id.getNamespace().equals(TerrainSlabs.MOD_ID)
                        && id.getPath().startsWith("block/snow_slab/snow_slab_test_")) {
                    return new SnowSlabFabricModel(model);
                }
                return model;
            });
        });
    }
}

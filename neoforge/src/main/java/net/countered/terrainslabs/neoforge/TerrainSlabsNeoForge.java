package net.countered.terrainslabs.neoforge;

import eu.midnightdust.lib.config.MidnightConfig;
import net.countered.terrainslabs.TerrainSlabs;
import net.countered.terrainslabs.neoforge.client.NeoForgeBuiltinPacks;
import net.countered.terrainslabs.neoforge.feature.ModFeatures;
import net.countered.terrainslabs.neoforge.model.SlabOffsetModel;
import net.countered.terrainslabs.platform.PlatformConfigHooks;
import net.countered.terrainslabs.platform.neoforge.PlatformConfigHooksImpl;
import net.countered.terrainslabs.registries.ModBlocksRegistry;
import net.countered.terrainslabs.registries.ModTillableRegistry;
import net.countered.terrainslabs.util.OnTopHelper;
import net.minecraft.client.color.block.BlockTintSources;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;

import java.util.List;

@Mod(TerrainSlabs.MOD_ID)
public final class TerrainSlabsNeoForge {

    public TerrainSlabsNeoForge(IEventBus modBus) {

        TerrainSlabs.init();

        PlatformConfigHooks.setProvider(new PlatformConfigHooks.Provider() {
            @Override
            public boolean isSlabGenerationEnabled() {
                return PlatformConfigHooksImpl.isSlabGenerationEnabled();
            }

            @Override
            public boolean isVegetationOnSlabsEnabled() {
                return PlatformConfigHooksImpl.isVegetationOnSlabsEnabled();
            }

            @Override
            public boolean isSnowOnSlabsEnabled() {
                return PlatformConfigHooksImpl.isSnowOnSlabsEnabled();
            }

            @Override
            public int getSlabRunLength() {
                return PlatformConfigHooksImpl.getSlabRunLength();
            }

            @Override
            public boolean isCornerSlabsEnabled() {
                return PlatformConfigHooksImpl.isCornerSlabsEnabled();
            }
        });

        ModFeatures.FEATURES.register(modBus);

        modBus.addListener(this::setupServer);
        modBus.addListener(this::setupPack);
        modBus.addListener(this::onModifyBakingResult);
        modBus.addListener(this::registerBlockColorProviders);
    }

    private void setupServer(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            MidnightConfig.init(TerrainSlabs.MOD_ID, PlatformConfigHooksImpl.class);
//     TODO       ModFlattenablesRegistry.registerFlattenables();
            ModTillableRegistry.registerTillables();
        });
    }

    private void setupPack(AddPackFindersEvent event) {
        NeoForgeBuiltinPacks.addPack(event);
    }

    public void onModifyBakingResult(ModelEvent.ModifyBakingResult event) {
        var models = event.getBakingResult().blockStateModels();

        for (var entry : models.entrySet()) {
            BlockState state = entry.getKey();
            if (OnTopHelper.terrain_slabs$isStateValidOnTop(state)) {
                models.put(state, new SlabOffsetModel(entry.getValue()));
            }
        }
    }

    public void registerBlockColorProviders(RegisterColorHandlersEvent.BlockTintSources event) {
        event.register(List.of(BlockTintSources.grass()), ModBlocksRegistry.GRASS_SLAB.get());
    }
}
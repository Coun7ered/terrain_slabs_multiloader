package net.countered.terrainslabs.feature;

import dev.architectury.registry.level.biome.BiomeModifications;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.countered.terrainslabs.TerrainSlabs;
import net.countered.terrainslabs.feature.generation.SlabFeature;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class ModAddedFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(TerrainSlabs.MOD_ID, Registries.FEATURE);

    public static final RegistrySupplier<Feature<NoneFeatureConfiguration>> SLAB_FEATURE = FEATURES.register("slab_feature", () -> new SlabFeature(NoneFeatureConfiguration.CODEC));;
    public static final ResourceKey<PlacedFeature> SLAB_FEATURE_PLACED_KEY = ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(TerrainSlabs.MOD_ID, "slab_feature_placed"));

    public static void registerFeatures() {
        FEATURES.register();

        BiomeModifications.addProperties((context, mutableProperties) -> {
            mutableProperties.getGenerationProperties().addFeature(
                    GenerationStep.Decoration.UNDERGROUND_STRUCTURES,
                    SLAB_FEATURE_PLACED_KEY
            );
        });
    }
}
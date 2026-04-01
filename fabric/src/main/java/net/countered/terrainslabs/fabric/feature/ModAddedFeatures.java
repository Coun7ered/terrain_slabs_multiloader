package net.countered.terrainslabs.fabric.feature;

import net.countered.terrainslabs.TerrainSlabs;
import net.countered.terrainslabs.generation.SlabFeature;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class ModAddedFeatures {

    public static final Feature<NoneFeatureConfiguration> SLAB_FEATURE = new SlabFeature(NoneFeatureConfiguration.CODEC);
    public static final ResourceKey<PlacedFeature> SLAB_FEATURE_PLACED_KEY = ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(TerrainSlabs.MOD_ID, "slab_feature_placed"));

    public static void registerFeatures() {
        Registry.register(
                BuiltInRegistries.FEATURE,
                new ResourceLocation(TerrainSlabs.MOD_ID, "slab_feature"),
                SLAB_FEATURE
        );
        BiomeModifications.addFeature(BiomeSelectors.all(), GenerationStep.Decoration.UNDERGROUND_STRUCTURES, SLAB_FEATURE_PLACED_KEY);
    }
}

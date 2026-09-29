package net.countered.terrainslabs.fabric.feature;

import net.countered.terrainslabs.TerrainSlabs;
import net.countered.terrainslabs.feature.generation.SlabFeature;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class ModAddedFeatures {

    public static final ResourceKey<PlacedFeature> SLAB_FEATURE_PLACED_KEY = ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(TerrainSlabs.MOD_ID, "slab_feature_placed"));

    public static void registerFeatures() {
        Registry.register(
                BuiltInRegistries.FEATURE_TYPE,
                Identifier.fromNamespaceAndPath(TerrainSlabs.MOD_ID, "slab_feature"),
                SlabFeature.CODEC
        );
        BiomeModifications.addFeature(BiomeSelectors.all(), GenerationStep.Decoration.UNDERGROUND_STRUCTURES, SLAB_FEATURE_PLACED_KEY);
    }
}

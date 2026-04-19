package net.countered.terrainslabs.neoforge.feature;

import net.countered.terrainslabs.TerrainSlabs;
import net.countered.terrainslabs.generation.SlabFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;


public class ModFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES =
        DeferredRegister.create(NeoForgeRegistries.FEATURES, TerrainSlabs.MOD_ID);

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> SLAB_FEATURE =
            FEATURES.register("slab_feature", () ->
                    new SlabFeature(NoneFeatureConfiguration.CODEC)
            );
}
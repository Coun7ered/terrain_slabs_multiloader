package net.countered.terrainslabs.forge;

import net.countered.terrainslabs.TerrainSlabs;
import net.countered.terrainslabs.generation.SlabFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES =
        DeferredRegister.create(ForgeRegistries.FEATURES, TerrainSlabs.MOD_ID);

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> SLAB_FEATURE =
            FEATURES.register("slab_feature", () ->
                    new SlabFeature(NoneFeatureConfiguration.CODEC)
            );
}
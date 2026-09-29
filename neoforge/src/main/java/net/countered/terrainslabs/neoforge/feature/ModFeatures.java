package net.countered.terrainslabs.neoforge.feature;

import com.mojang.serialization.MapCodec;
import net.countered.terrainslabs.TerrainSlabs;
import net.countered.terrainslabs.feature.generation.SlabFeature;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


public class ModFeatures {

    public static final DeferredRegister<MapCodec<? extends Feature>> FEATURES =
        DeferredRegister.create(Registries.FEATURE_TYPE, TerrainSlabs.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<SlabFeature>> SLAB_FEATURE =
            FEATURES.register("slab_feature", () -> SlabFeature.CODEC);
}

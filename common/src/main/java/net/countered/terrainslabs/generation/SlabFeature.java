package net.countered.terrainslabs.generation;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class SlabFeature extends Feature<NoneFeatureConfiguration> {

    public SlabFeature(Codec codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext context) {
        System.out.println("SlabFeature place method called");
        return false;
    }
}

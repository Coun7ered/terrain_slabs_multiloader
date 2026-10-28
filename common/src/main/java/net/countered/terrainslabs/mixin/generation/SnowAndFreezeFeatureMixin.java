package net.countered.terrainslabs.mixin.generation;

import com.llamalad7.mixinextras.sugar.Local;
import net.countered.terrainslabs.block.customslabs.CustomSlab;
import net.countered.terrainslabs.config.TSConfig;
import net.countered.terrainslabs.registries.ModBlocksRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.SnowAndFreezeFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SnowAndFreezeFeature.class)
public class SnowAndFreezeFeatureMixin {

    @Inject(
            method = "place",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/biome/Biome;shouldSnow(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;)Z"
            )
    )
    private void terrain_slabs$changeSlabToSnowSlab(
            WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin,
            CallbackInfoReturnable<Boolean> cir,
            @Local(ordinal = 0) BlockPos.MutableBlockPos topPos,
            @Local(ordinal = 1) BlockPos.MutableBlockPos belowPos,
            @Local(ordinal = 0) Biome biome
    ) {
        if (TSConfig.isSnowOnSlabsEnabled()) return;

        BlockState belowState = level.getBlockState(belowPos);
        if (!belowState.is(BlockTags.SLABS)) return;
        if (belowState.is(ModBlocksRegistry.SNOW_SLAB.get())) return;
        if (belowState.is(BlockTags.WOODEN_SLABS)) return;
        if (belowState.is(BlockTags.CANNOT_SUPPORT_SNOW_LAYER)) return;
        if (belowState.hasProperty(SlabBlock.WATERLOGGED) && belowState.getValue(SlabBlock.WATERLOGGED)) return;
        if (belowState.hasProperty(CustomSlab.GENERATED) && !belowState.getValue(CustomSlab.GENERATED)) return;

        if (biome.getPrecipitationAt(topPos, level.getSeaLevel()) == Biome.Precipitation.SNOW
                && level.isInsideBuildHeight(topPos.getY())
                && level.getBrightness(LightLayer.BLOCK, topPos) < 10
                && level.getBlockState(topPos).isAir()) {

            BlockState newSlab = ModBlocksRegistry.SNOW_SLAB.get().defaultBlockState()
                    .setValue(SlabBlock.TYPE, belowState.getValue(SlabBlock.TYPE));
            if (belowState.hasProperty(SlabBlock.WATERLOGGED)) {
                newSlab = newSlab.setValue(SlabBlock.WATERLOGGED, belowState.getValue(SlabBlock.WATERLOGGED));
            }
            if (belowState.hasProperty(CustomSlab.GENERATED)) {
                newSlab = newSlab.setValue(CustomSlab.GENERATED, belowState.getValue(CustomSlab.GENERATED));
            }
            level.setBlock(belowPos, newSlab, 2);
        }
    }
}

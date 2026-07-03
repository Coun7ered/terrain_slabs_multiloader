package net.countered.terrainslabs.mixin.feature;

import net.countered.terrainslabs.block.ModSlabsMap;
import net.countered.terrainslabs.block.customslabs.specialslabs.CustomSlab;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.VegetationPatchFeature;
import net.minecraft.world.level.levelgen.feature.configurations.VegetationPatchConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.function.Predicate;

/**
 * Vegetation patches (e.g. Lush Caves' ceiling moss) run in VEGETAL_DECORATION,
 * which is well after our SlabFeature (UNDERGROUND_STRUCTURES). By the time a
 * patch scans the ceiling, our feature has already placed a slab there matching
 * whatever raw stone was underneath (e.g. deepslate) - not the moss the patch is
 * about to place. The patch's own "replaceable" check only lets it touch that
 * position at all because the slab's base block is tagged (#minecraft:dirt /
 * #minecraft:moss_replaceable), but it would otherwise overwrite our slab with a
 * full moss_block, losing the slab shape.
 *
 * This mirrors MixinDiskFeature: when the patch is about to place its ground
 * state where one of our slabs already sits, place the equivalent slab instead,
 * preserving the existing slab's TYPE/WATERLOGGED/GENERATED.
 */
@Mixin(VegetationPatchFeature.class)
public class MixinVegetationPatchFeature {

    @ModifyVariable(
            method = "placeGround",
            at = @At("STORE"),
            ordinal = 0
    )
    private BlockState terrain_slabs$slabAwareGroundState(
            BlockState groundState,
            WorldGenLevel level,
            VegetationPatchConfiguration config,
            Predicate<BlockState> replaceableBlocks,
            RandomSource random,
            BlockPos.MutableBlockPos mutablePos,
            int maxDistance
    ) {
        BlockState existingState = level.getBlockState(mutablePos);
        if (!(existingState.getBlock() instanceof SlabBlock)) return groundState;

        Block matchingSlab = ModSlabsMap.getSlabForBlock(groundState.getBlock());
        if (matchingSlab == null) return groundState;

        BlockState newState = matchingSlab.defaultBlockState()
                .setValue(SlabBlock.TYPE, existingState.getValue(SlabBlock.TYPE))
                .setValue(SlabBlock.WATERLOGGED, existingState.getValue(SlabBlock.WATERLOGGED));
        if (existingState.hasProperty(CustomSlab.GENERATED) && newState.hasProperty(CustomSlab.GENERATED)) {
            newState = newState.setValue(CustomSlab.GENERATED, existingState.getValue(CustomSlab.GENERATED));
        }
        return newState;
    }
}

package net.countered.terrainslabs.mixin.feature;

import com.llamalad7.mixinextras.sugar.Local;
import net.countered.terrainslabs.block.ModSlabsMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.BulkSectionAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OreFeature.class)
public abstract class MixinOreFeature {

    /**
     * After placing an ore feature, check the block above and below. If it's a slab, replace it with the corresponding slab for that ore type.
     */
    @Inject(
            method = "doPlace",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/chunk/LevelChunkSection;setBlockState(IIILnet/minecraft/world/level/block/state/BlockState;Z)Lnet/minecraft/world/level/block/state/BlockState;"
            )
    )
    private void terrain_slabs$updateSlabs(
            WorldGenLevel level, RandomSource random, OreConfiguration config,
            double minX, double maxX, double minZ, double maxZ, double minY, double maxY,
            int x, int y, int z, int width, int height,
            CallbackInfoReturnable<Boolean> cir,
            @Local(ordinal = 0) BlockPos.MutableBlockPos mutableBlockPos,
            @Local(ordinal = 0) OreConfiguration.TargetBlockState targetBlockState,
            @Local(ordinal = 0) BulkSectionAccess bulkSectionAccess
    ) {
        Block oreBlock = targetBlockState.state.getBlock();
        Block newSlab = ModSlabsMap.getSlabForBlock(oreBlock);

        if (newSlab == null) return;

        // 1. Check ABOVE (y + 1)
        terrain_slabs$checkAndReplace(level, bulkSectionAccess, mutableBlockPos.above(), newSlab);

        // 2. Check BELOW (y - 1)
        terrain_slabs$checkAndReplace(level, bulkSectionAccess, mutableBlockPos.below(), newSlab);
    }

    /**
     * Helper to check if a block at a specific position is a slab and replace it if necessary.
     */
    @Unique
    private void terrain_slabs$checkAndReplace(WorldGenLevel level, BulkSectionAccess access, BlockPos pos, Block newSlabBlock) {
        if (level.isOutsideBuildHeight(pos.getY())) return;

        BlockState currentState = access.getBlockState(pos);

        if (currentState.getBlock() instanceof SlabBlock && !currentState.is(newSlabBlock)) {
            BlockState newState = newSlabBlock.defaultBlockState()
                    .setValue(SlabBlock.TYPE, currentState.getValue(SlabBlock.TYPE))
                    .setValue(SlabBlock.WATERLOGGED, currentState.getValue(SlabBlock.WATERLOGGED));

            LevelChunkSection section = access.getSection(pos);
            if (section != null) {
                section.setBlockState(
                        SectionPos.sectionRelative(pos.getX()),
                        SectionPos.sectionRelative(pos.getY()),
                        SectionPos.sectionRelative(pos.getZ()),
                        newState,
                        false
                );
            }
        }
    }
}

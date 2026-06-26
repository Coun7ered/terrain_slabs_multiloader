package net.countered.terrainslabs.mixin.offset.place;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.countered.terrainslabs.api.SlabHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Targets include all vanilla classes that need to be modified
 */
@Mixin( value = {
        }, targets = {
        "net.minecraft.world.level.block.BushBlock",
        "net.minecraft.world.level.block.MushroomBlock",
        "net.minecraft.world.level.block.TorchBlock",
        "net.minecraft.world.level.block.LanternBlock",
        "net.minecraft.world.level.block.SnowLayerBlock",
        "net.minecraft.world.level.block.WitherRoseBlock",
        "net.minecraft.world.level.block.PitcherCropBlock",
        "net.minecraft.world.level.block.SeaPickleBlock",
        "net.minecraft.world.level.block.SmallDripleafBlock",
        "net.minecraft.world.level.block.CandleBlock",
        "net.minecraft.world.level.block.CandleCakeBlock",
        "net.minecraft.world.level.block.SeaPickleBlock",
        "net.minecraft.world.level.block.CakeBlock",
        "net.minecraft.world.level.block.FireBlock",
        "net.minecraft.world.level.block.SoulFireBlock",
        "net.minecraft.world.level.block.BaseFireBlock",
        "net.minecraft.world.level.block.CactusBlock",
        "net.minecraft.world.level.block.SugarCaneBlock",
        "net.minecraft.world.level.block.GrowingPlantBlock",
        "net.minecraft.world.level.block.BambooSaplingBlock",
        "net.minecraft.world.level.block.BambooStalkBlock",
        "net.minecraft.world.level.block.AmethystClusterBlock",
        "net.minecraft.world.level.block.HangingRootsBlock",
        "net.minecraft.world.level.block.SporeBlossomBlock",
        "net.minecraft.world.level.block.LightningRodBlock",
        "net.minecraft.world.level.block.EndRodBlock",
        "net.minecraft.world.level.block.BaseCoralPlantTypeBlock"
})
public class MixinBlocks {
    /**
     * When calling for the state below a block, pretends it's the matching full block when relevant.
     */
    @SuppressWarnings({"MixinAnnotationTarget", "InvalidInjectorMethodSignature"})
    @WrapOperation( method = "canSurvive", require = 0, at = @At( value = "INVOKE",
            target = "Lnet/minecraft/world/level/LevelReader;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"
    ) )
    private BlockState terrain_slabs$convertBlockState(
            LevelReader instance, BlockPos offPos, Operation<BlockState> original,
            BlockState state, LevelReader level, BlockPos pos
    ) {
        return SlabHelper.terrain_slabs$convertBlockState( instance, offPos, original, state, level, pos );
    }

    /**
     * When checking if below block "can support center", gives true for the top face of a bottom slab.
     */
    @SuppressWarnings({"MixinAnnotationTarget", "InvalidInjectorMethodSignature"})
    @WrapOperation( method = "canSurvive", require = 0, at = {
            @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;canSupportCenter(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z"),
            @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/TorchBlock;canSupportCenter(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z")
    } )
    private boolean terrain_slabs$slabsSupportCenter(
            LevelReader instance, BlockPos offsetPos, Direction direction, Operation<Boolean> original,
            BlockState state, LevelReader level, BlockPos pos
    ) {
        return SlabHelper.terrain_slabs$slabsSupportCenter(
                instance, offsetPos, direction, original, state, level, pos
        );
    }

    /**
     * Fix particle position. Lazy implementation may need to be fixed later.
     */
    @SuppressWarnings({"MixinAnnotationTarget", "InvalidInjectorMethodSignature"})
    @WrapOperation( method = "animateTick", require = 0, at =
    @At( value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V" )
    )
    private void terrain_slabs$offsetParticles(
            Level instance, ParticleOptions particleData,
            double x, double y, double z,
            double xSpeed, double ySpeed, double zSpeed,
            Operation<Void> original,
            BlockState state, Level level, BlockPos pos, RandomSource random
    ) {
        SlabHelper.terrain_slabs$offsetParticles(
                instance, particleData, x, y, z, xSpeed, ySpeed, zSpeed, original, state, level, pos, random
        );
    }
}

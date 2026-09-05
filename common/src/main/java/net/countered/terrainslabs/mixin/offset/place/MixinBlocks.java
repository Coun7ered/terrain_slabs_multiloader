package net.countered.terrainslabs.mixin.offset.place;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.polymixin.api.DynamicTargets;
import net.countered.terrainslabs.api.SlabHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Targets include all vanilla classes that need to be modified
 */
@DynamicTargets
@Mixin( priority = 1001, targets = {
        "net.minecraft.world.level.block.BushBlock",
        "net.minecraft.world.level.block.TorchBlock",
        "net.minecraft.world.level.block.LanternBlock",
        "net.minecraft.world.level.block.SnowLayerBlock",
        "net.minecraft.world.level.block.SmallDripleafBlock",
        "net.minecraft.world.level.block.AbstractCandleBlock",
        "net.minecraft.world.level.block.CakeBlock",
        "net.minecraft.world.level.block.BaseFireBlock",
        "net.minecraft.world.level.block.CactusBlock",
        "net.minecraft.world.level.block.SugarCaneBlock",
        "net.minecraft.world.level.block.BambooSaplingBlock",
        "net.minecraft.world.level.block.BambooStalkBlock",
        "net.minecraft.world.level.block.AmethystClusterBlock",
        "net.minecraft.world.level.block.HangingRootsBlock",
        "net.minecraft.world.level.block.SporeBlossomBlock",
        "net.minecraft.world.level.block.RodBlock",
        "net.minecraft.world.level.block.BaseCoralPlantTypeBlock",
        "net.minecraft.world.level.block.GrowingPlantBlock"
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

    @SuppressWarnings({"MixinAnnotationTarget", "InvalidInjectorMethodSignature"})
    @WrapOperation( method = "canSurvive", require = 0, at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Block;isFaceFull(Lnet/minecraft/world/phys/shapes/VoxelShape;Lnet/minecraft/core/Direction;)Z")
    )
    private boolean terrain_slabs$slabsHaveFullFace(
            VoxelShape pShape, Direction pFace, Operation<Boolean> original,
            BlockState state, LevelReader world, BlockPos pos
    ) {
        boolean origOutput = original.call(pShape, pFace);
        return SlabHelper.terrain_slabs$slabsSupportGeneric(
                world, pos.relative(pFace), pFace, origOutput, state, world, pos
        );
    }

    @SuppressWarnings({"MixinAnnotationTarget", "InvalidInjectorMethodSignature"})
    @WrapOperation( method = "canSurvive", require = 0, at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Block;canSupportRigidBlock(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Z")
    )
    private boolean terrain_slabs$slabsSupportRigid(
            BlockGetter pLevel, BlockPos pPos, Operation<Boolean> original,
            BlockState state, LevelReader world, BlockPos pos
    ) {
        boolean origOutput = original.call(world, pPos);
        return SlabHelper.terrain_slabs$slabsSupportGeneric(
                world, pPos, Direction.UP, origOutput, state, world, pos
        );
    }
}

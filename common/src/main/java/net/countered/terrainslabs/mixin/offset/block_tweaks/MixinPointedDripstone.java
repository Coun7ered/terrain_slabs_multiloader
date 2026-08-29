package net.countered.terrainslabs.mixin.offset.block_tweaks;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.countered.terrainslabs.api.IConditionalOffset;
import net.countered.terrainslabs.api.SlabHelper;
import net.countered.terrainslabs.api.helperInterface.ISpikeConversion;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin( PointedDripstoneBlock.class )
public class MixinPointedDripstone implements ISpikeConversion<DripstoneThickness>, IConditionalOffset {

    @Override
    public <L extends BlockGetter> boolean couldBeOntop(L level, BlockPos pos, BlockState state) {
        return state.getValue( PointedDripstoneBlock.TIP_DIRECTION ) == Direction.UP;
    }

    @Override
    public <L extends BlockGetter> boolean couldBeOnbottom(L level, BlockPos pos, BlockState state) {
        return state.getValue( PointedDripstoneBlock.TIP_DIRECTION ) == Direction.DOWN;
    }

    @WrapOperation( method = "isValidPointedDripstonePlacement", at = @At( value = "INVOKE",
            target = "Lnet/minecraft/world/level/LevelReader;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"
    ) )
    private static BlockState terrain_slabs$convertBlockState(
            LevelReader instance, BlockPos offPos, Operation<BlockState> original,
            LevelReader level, BlockPos pos, Direction dir
    ) {
        BlockState state = Blocks.POINTED_DRIPSTONE.defaultBlockState(); //Should not matter...
        return SlabHelper.terrain_slabs$convertBlockState( instance, offPos, original, state, level, pos );
    }

    @WrapOperation( method = "spawnDripParticle(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/material/Fluid;)V",
            at = @At( value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V" )
    )
    private static void terrain_slabs$offsetParticles(
            Level instance, ParticleOptions particleData,
            double x, double y, double z, double xSpeed, double ySpeed, double zSpeed,
            Operation<Void> original,
            Level level, BlockPos pos, BlockState state, Fluid fluid
    ) {
        SlabHelper.terrain_slabs$offsetParticles(
                instance, particleData, x, y, z, xSpeed, ySpeed, zSpeed, original, state, level, pos, null
        );
    }
}

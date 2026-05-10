package net.countered.terrainslabs.mixin.ontop.place;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.countered.platform.PlatformConfigHooks;
import net.countered.terrainslabs.block.interfaces.ISlabCopy;
import net.countered.terrainslabs.util.MixinHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

// TODO: Make these fill automatically
@Mixin( targets = {
        "net.minecraft.world.level.block.state.BlockBehaviour",
        "net.minecraft.world.level.block.BushBlock",
        "net.minecraft.world.level.block.MushroomBlock",
        "net.minecraft.world.level.block.TorchBlock",
        "net.minecraft.world.level.block.LanternBlock"
})
public class MixinBlockBehaviours {

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
        BlockState stateAtOffset = original.call( instance, offPos );
        if ( terrain_slabs$skipModify( offPos, state, pos ) || MixinHelper.terrain_slabs$notBottomSlab( stateAtOffset ) ) {
            return stateAtOffset;
        }

        return ISlabCopy.getOriginState( stateAtOffset );
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
        boolean origOutput = original.call( instance, offsetPos, direction );
        if (
                direction != Direction.UP
                || terrain_slabs$skipModify( offsetPos, state, pos )
                || MixinHelper.terrain_slabs$notBottomSlab( level.getBlockState( offsetPos ) )
        ) {
            return origOutput;
        }

        return true;
    }

    /**
     * Fix particle position. Lazy implementation may need to be fixed later.
     */
    @SuppressWarnings("MixinAnnotationTarget")
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
        original.call( instance, particleData, x,
                y + state.getOffset( level, pos ).y(),
                z, xSpeed, ySpeed, zSpeed );
    }


    //==================//
    // Helper Functions //
    //==================//


    @Unique
    private static boolean terrain_slabs$skipModify(BlockPos offPos, BlockState targetState, BlockPos pos ) {
        return !PlatformConfigHooks.isVegetationOnSlabsEnabled()
                || !MixinHelper.terrain_slabs$isStateValidOnTop( targetState )
                || !( offPos.getX() == pos.getX() && offPos.getZ() == pos.getZ() && offPos.getY() == pos.getY() - 1 );
    }
}

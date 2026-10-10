package net.countered.terrainslabs.mixin.feature;

import net.countered.terrainslabs.block.customslabs.specialslabs.CustomSlab;
import net.countered.terrainslabs.block.interfaces.IOffsetState;
import net.countered.terrainslabs.registries.ModBlocksRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(WorldGenRegion.class)
public class WorldGenRegionMixin {

    @Unique
    private static final Set<Block> SOIL_SLAB_BLOCKS = Set.of(
            ModBlocksRegistry.GRASS_SLAB.get(),
            ModBlocksRegistry.PODZOL_SLAB.get(),
            ModBlocksRegistry.MYCELIUM_SLAB.get(),
            ModBlocksRegistry.PATH_SLAB.get()
    );

    /**
     * fix for grass slabs on village paths
     */
    @Inject(method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;II)Z", at = @At("HEAD"), cancellable = true)
    private void terrain_slabs$onSetBlock(BlockPos pos, BlockState state, int flags, int recursionLeft, CallbackInfoReturnable<Boolean> cir) {
        if (!state.is(Blocks.DIRT_PATH)) return;

        WorldGenRegion level = (WorldGenRegion)(Object) this;
        BlockState aboveState = level.getBlockState(pos.above());

        if ( SOIL_SLAB_BLOCKS.contains(aboveState.getBlock()) ) {
            cir.setReturnValue(level.setBlock(pos, Blocks.DIRT.defaultBlockState(), flags, recursionLeft));
            level.setBlock(pos.above(), ModBlocksRegistry.PATH_SLAB.get().defaultBlockState()
                    .setValue(BlockStateProperties.SLAB_TYPE, aboveState.getValue(BlockStateProperties.SLAB_TYPE))
                    .setValue(CustomSlab.GENERATED, true), flags);
        }
    }

    /**
     * Worldgen offset handler.
     */
    @ModifyVariable( method = "setBlock", at = @At("HEAD"), argsOnly = true )
    private BlockState terrain_slabs$convertBlockState(BlockState arg1, BlockPos pos,
            BlockState state, int flags, int recursionLeft
    ) {
        if ( !((IOffsetState) state).terrain_slabs$hasOffsetState() ) {
            return state;
        }

        WorldGenRegion level = (WorldGenRegion) (Object) this;
        if ( IOffsetState.canGenerateOntop( level, pos, state ) ) {
            return ((IOffsetState) state ).terrain_slabs$getOntopState( level, pos, state );
        } else if (IOffsetState.canGenerateOnbottom(level, pos, state)) {
            return ((IOffsetState) state).terrain_slabs$getOnbottomState( level, pos, state );
        }

        return state;
    }
}
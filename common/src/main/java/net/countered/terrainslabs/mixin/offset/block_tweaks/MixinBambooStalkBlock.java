package net.countered.terrainslabs.mixin.offset.block_tweaks;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.countered.terrainslabs.api.SlabHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BambooStalkBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin( BambooStalkBlock.class )
public class MixinBambooStalkBlock {

    @WrapOperation( method = "getStateForPlacement", at = @At( value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;") )
    private BlockState terrain_slabs$convertBlockStateBamboo(
            Level instance, BlockPos pos, Operation<BlockState> original,
            BlockPlaceContext context
    ) {
        return SlabHelper.terrain_slabs$convertBlockState(
                instance, pos, original, Blocks.BAMBOO.defaultBlockState(),
                instance, context.getClickedPos() );
    }

}
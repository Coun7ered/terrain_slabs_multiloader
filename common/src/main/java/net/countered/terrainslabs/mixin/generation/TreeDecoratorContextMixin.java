package net.countered.terrainslabs.mixin.generation;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.countered.terrainslabs.block.customslabs.CustomSlab;
import net.countered.terrainslabs.registries.ModBlocksRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TreeDecorator.Context.class)
public abstract class TreeDecoratorContextMixin {

    @WrapMethod(method = "setBlock")
    private void terrainslabs$podzolSlab(BlockPos pos, BlockState state, Operation<Void> original) {
        if (!state.is(Blocks.PODZOL)) {
            original.call(pos, state);
            return;
        }

        TreeDecorator.Context self = (TreeDecorator.Context) (Object) this;
        BlockState[] slab = new BlockState[1];
        self.level().isStateAtPosition(pos, s -> {
            if (s.is(ModBlocksRegistry.DIRT_SLAB.get())
                    || s.is(ModBlocksRegistry.GRASS_SLAB.get())
                    || s.is(ModBlocksRegistry.MOSS_SLAB.get())
                    || s.is(ModBlocksRegistry.MUD_SLAB.get())) {
                slab[0] = ModBlocksRegistry.PODZOL_SLAB.get().defaultBlockState()
                        .setValue(SlabBlock.TYPE, s.getValue(SlabBlock.TYPE))
                        .setValue(CustomSlab.GENERATED, s.getValue(CustomSlab.GENERATED))
                        .setValue(SlabBlock.WATERLOGGED, s.getValue(SlabBlock.WATERLOGGED));
            }
            return false;
        });
        original.call(pos, slab[0] != null ? slab[0] : state);
    }
}
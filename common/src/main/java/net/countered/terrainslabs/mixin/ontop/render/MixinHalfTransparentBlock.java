package net.countered.terrainslabs.mixin.ontop.render;

import net.countered.terrainslabs.block.customslabs.specialslabs.HalfTransparentSlab;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Vanilla {@link HalfTransparentBlock#skipRendering} only culls the shared face
 * against another block of the <i>same</i> block. That means a full ice block
 * next to one of our {@link HalfTransparentSlab}s never culls its own face, so
 * even though the slab culls its side, the full block's face still renders and a
 * seam remains.
 *
 * <p>This injection makes the full block also cull its face when the neighbour
 * is a {@link HalfTransparentSlab} that declares this block as its full-block
 * equivalent, and the slab's solid volume actually covers the shared face. That
 * completes the two-way culling between full ice-type blocks and their slabs.
 */
@Mixin(HalfTransparentBlock.class)
public abstract class MixinHalfTransparentBlock {

    @Inject(method = "skipRendering", at = @At("HEAD"), cancellable = true)
    private void terrain_slabs$cullAgainstMatchingSlab(BlockState state, BlockState neighborState,
                                                       Direction direction,
                                                       CallbackInfoReturnable<Boolean> cir) {
        if (!(neighborState.getBlock() instanceof HalfTransparentSlab slab)) {
            return;
        }
        // Only cull if the slab considers THIS block its full-block equivalent,
        // so different translucent materials never wrongly merge.
        if (!slab.isFullBlockEquivalent(state)) {
            return;
        }
        // `direction` points from this full block toward the slab. The slab must
        // present a solid face on the opposite side (the side facing us) for the
        // shared face to be fully covered.
        if (slabCoversFace(neighborState, direction.getOpposite())) {
            cir.setReturnValue(true);
        }
    }

    private static boolean slabCoversFace(BlockState slabState, Direction face) {
        if (!slabState.hasProperty(BlockStateProperties.SLAB_TYPE)) return false;
        SlabType t = slabState.getValue(BlockStateProperties.SLAB_TYPE);
        if (t == SlabType.DOUBLE) return true;
        return switch (face) {
            case DOWN -> t == SlabType.BOTTOM;
            case UP -> t == SlabType.TOP;
            // Horizontal: the full block's side face is full height, but a single
            // (non-double) slab only backs HALF of it. Culling the whole face
            // would leave a see-through hole over the empty half, so we must NOT
            // cull here. Only a DOUBLE slab (handled above) fully backs the face.
            default -> false;
        };
    }
}

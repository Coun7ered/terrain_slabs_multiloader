package net.countered.terrainslabs.block.customslabs.specialslabs;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.EnchantmentTags;

import java.util.function.Supplier;

/**
 * A slab for translucent, ice-like blocks (regular ice, BWG black ice, etc.).
 *
 * <p>Translucent blocks show a seam where two of them meet, because the shared
 * interior face is still drawn. Vanilla {@code HalfTransparentBlock} suppresses
 * this in {@link #skipRendering} by culling the shared face when the neighbour
 * is the same block. Plain {@code SlabBlock} doesn't, so ice slabs render with
 * visible internal seams against each other and against full ice blocks.
 *
 * <p>This restores culling, slab-aware so it never culls a face that isn't
 * actually fully covered (which would create see-through holes):
 * <ul>
 *   <li>Against the <b>same slab block</b>: cull the shared face only when both
 *       sides' solid volumes meet over that whole face.</li>
 *   <li>Against the <b>equivalent full block</b> (e.g. an ICE slab next to a
 *       full ICE block): the full block covers every face, so cull whenever our
 *       own face is solid.</li>
 * </ul>
 *
 * <p>The equivalent full block is supplied optionally; pass {@code null} to cull
 * only against same-block slabs.
 */
public class HalfTransparentSlab extends CustomSlab {

    private final Supplier<Block> fullBlockEquivalent;
    private Block cachedFullBlock;
    private boolean fullBlockResolved;

    public HalfTransparentSlab(BlockBehaviour.Properties properties) {
        this(properties, null);
    }

    /**
     * @param fullBlockEquivalent supplier of the full block this slab is made
     *                            from (e.g. {@code () -> Blocks.ICE}); the shared
     *                            face against that block is culled too. May be
     *                            null. A supplier is used so a not-yet-registered
     *                            block (e.g. an addon/BWG block) can be referenced.
     */
    public HalfTransparentSlab(BlockBehaviour.Properties properties, Supplier<Block> fullBlockEquivalent) {
        super(properties);
        this.fullBlockEquivalent = fullBlockEquivalent;
    }

    /**
     * Mirrors vanilla {@link net.minecraft.world.level.block.IceBlock#playerDestroy}:
     * when broken without silk touch, a regular-ice slab melts into water (unless
     * the dimension is ultrawarm, where it just disappears). Water is only placed
     * when the block below can hold it, exactly as vanilla does.
     *
     * <p>Because only the translucent regular-ice slabs use this class (packed and
     * blue ice slabs are plain opaque slabs), this correctly leaves those dry.
     */
    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state,
                              BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);

        // Silk touch (PREVENTS_ICE_MELTING) drops the slab and skips melting.
        if (EnchantmentHelper.hasTag(tool, EnchantmentTags.PREVENTS_ICE_MELTING)) {
            return;
        }
        if (level.dimensionType().ultraWarm()) {
            level.removeBlock(pos, false);
            return;
        }
        BlockState below = level.getBlockState(pos.below());
        if (below.blocksMotion() || below.liquid()) {
            level.setBlockAndUpdate(pos, Blocks.WATER.defaultBlockState());
        }
    }

    @Override
    protected boolean skipRendering(BlockState state, BlockState neighborState, Direction direction) {
        SlabType self = state.getValue(BlockStateProperties.SLAB_TYPE);

        // Against the equivalent full block: it covers the whole shared face, so
        // cull whenever OUR face on `direction` is solid.
        if (isFullBlockEquivalent(neighborState)) {
            return faceIsSolid(self, direction);
        }

        // Against the same slab block: cull only when both sides meet over the
        // whole shared face.
        if (neighborState.is(this)) {
            SlabType other = neighborState.getValue(BlockStateProperties.SLAB_TYPE);
            if (self == SlabType.DOUBLE) {
                return coversFace(other, direction.getOpposite());
            }
            switch (direction) {
                case UP:
                    return self == SlabType.TOP && coversFace(other, Direction.DOWN);
                case DOWN:
                    return self == SlabType.BOTTOM && coversFace(other, Direction.UP);
                default:
                    return other == SlabType.DOUBLE || self == other;
            }
        }

        // Anything else: defer to vanilla (keeps material boundaries visible).
        return super.skipRendering(state, neighborState, direction);
    }

    public boolean isFullBlockEquivalent(BlockState neighborState) {
        if (fullBlockEquivalent == null) return false;
        if (!fullBlockResolved) {
            cachedFullBlock = fullBlockEquivalent.get();
            fullBlockResolved = true;
        }
        return cachedFullBlock != null && neighborState.is(cachedFullBlock);
    }

    /** Is this slab's own face on {@code direction} a fully solid face? */
    private static boolean faceIsSolid(SlabType self, Direction direction) {
        return switch (direction) {
            case UP -> self == SlabType.TOP || self == SlabType.DOUBLE;
            case DOWN -> self == SlabType.BOTTOM || self == SlabType.DOUBLE;
            // Horizontal faces of a single slab are only half-height, so they are
            // never fully covered by the neighbour over the WHOLE cube face; but
            // against a full block the slab's half-face IS fully backed, so it is
            // safe to cull. (The slab only occupies half, and that half is flush
            // against the full block.)
            default -> true;
        };
    }

    /** Does a same-block slab of type {@code t} present a solid face on {@code face}? */
    private static boolean coversFace(SlabType t, Direction face) {
        if (t == SlabType.DOUBLE) return true;
        return switch (face) {
            case DOWN -> t == SlabType.BOTTOM;
            case UP -> t == SlabType.TOP;
            default -> false;
        };
    }
}

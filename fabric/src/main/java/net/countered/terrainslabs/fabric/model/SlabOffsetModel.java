package net.countered.terrainslabs.fabric.model;

import net.countered.terrainslabs.util.OnTopHelper;
import net.fabricmc.fabric.api.client.renderer.v1.Renderer;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.MutableMesh;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadEmitter;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.SlabType;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class SlabOffsetModel implements BlockStateModel {

    private final BlockStateModel wrapped;

    public SlabOffsetModel(BlockStateModel wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public void emitQuads(
            QuadEmitter emitter,
            BlockAndTintGetter blockView,
            BlockPos pos,
            BlockState state,
            RandomSource random,
            Predicate<@Nullable Direction> cullTest
    ) {
        boolean slabBelow = isPosSlab(blockView, pos.below());
        boolean isAboveShifted = isAboveShifted(blockView, state, pos);

        boolean shouldShift = isAboveShifted || slabBelow;

        if ((!shouldShift && !hasSlabNeighbor(blockView, pos)) || !OnTopHelper.terrain_slabs$isStateValidOnTop(state)) {
            wrapped.emitQuads(emitter, blockView, pos, state, random, cullTest);
            return;
        }
        MutableMesh mesh = Renderer.get().mutableMesh();

        wrapped.emitQuads(mesh.emitter(), blockView, pos, state, random, dir -> false);

        mesh.forEach(quad -> {
            Direction face = quad.cullFace();

            boolean neighborOnSlab = face != null && isNeighborOnSlab(blockView, pos, face);
            if (!shouldShift && !neighborOnSlab && face != null && cullTest.test(face)) {
                return;
            }

            emitter.copyFrom(quad);

            if (shouldShift) {
                for (int i = 0; i < 4; i++) {
                    emitter.pos(i, quad.x(i), quad.y(i) - 0.5f, quad.z(i));
                }
            }

            if (shouldShift || neighborOnSlab) {
                emitter.cullFace(null);
            }

            emitter.emit();
        });
    }

    private boolean isAboveShifted(BlockAndTintGetter blockView, BlockState state, BlockPos pos) {
        if (state.getBlock() instanceof DoublePlantBlock && state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER) {
            return isPosSlab(blockView, pos.below(2));
        }
        return false;
    }

    private boolean isPosSlab(BlockAndTintGetter blockView, BlockPos pos) {
        BlockState state = blockView.getBlockState(pos);
        return state.getBlock() instanceof SlabBlock && state.getValue(SlabBlock.TYPE) == SlabType.BOTTOM;
    }

    private boolean isNeighborOnSlab(BlockAndTintGetter blockView, BlockPos pos, Direction dir) {
        BlockPos neighborPos = pos.relative(dir);
        BlockState below = blockView.getBlockState(neighborPos.below());
        return below.getBlock() instanceof SlabBlock
                && below.getValue(SlabBlock.TYPE) == SlabType.BOTTOM;
    }

    private boolean hasSlabNeighbor(BlockAndTintGetter blockView, BlockPos pos) {
        for (Direction dir : Direction.values()) {
            if (isNeighborOnSlab(blockView, pos, dir)) return true;
        }
        return false;
    }

    @Override
    public @Nullable Object createGeometryKey(BlockAndTintGetter blockView, BlockPos pos, BlockState state, RandomSource random) {
        return BlockStateModel.super.createGeometryKey(blockView, pos, state, random);
    }

    @Override
    public void collectParts(RandomSource random, List<BlockStateModelPart> output) {
        wrapped.collectParts(random, output);
    }

    @Override
    public Material.Baked particleMaterial() {
        return wrapped.particleMaterial();
    }

    @Override
    public @BakedQuad.MaterialFlags int materialFlags() {
        return wrapped.materialFlags();
    }
}
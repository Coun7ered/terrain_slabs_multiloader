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
import net.minecraft.world.level.block.state.BlockState;
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
        if (!OnTopHelper.shouldHandleModel(blockView, pos, state)) {
            wrapped.emitQuads(emitter, blockView, pos, state, random, cullTest);
            return;
        }

        boolean shouldShift = OnTopHelper.shouldShift(blockView, pos, state);
        MutableMesh mesh = Renderer.get().mutableMesh();

        wrapped.emitQuads(mesh.emitter(), blockView, pos, state, random, dir -> false);

        mesh.forEach(quad -> {
            Direction face = quad.cullFace();

            boolean neighborOnSlab = face != null && OnTopHelper.isNeighborOnSlab(blockView, pos, face);
            if (!shouldShift && !neighborOnSlab && face != null && cullTest.test(face)) {
                return;
            }

            emitter.copyFrom(quad);
            float dy = shouldShift ? -0.5f : 0f;
            for (int i = 0; i < 4; i++) {
                emitter.pos(i, quad.x(i), quad.y(i) + dy, quad.z(i));
            }

            if (shouldShift || neighborOnSlab) {
                emitter.cullFace(null);
            }

            emitter.emit();
        });
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

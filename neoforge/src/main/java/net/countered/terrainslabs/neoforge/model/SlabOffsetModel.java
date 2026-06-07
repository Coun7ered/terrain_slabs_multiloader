package net.countered.terrainslabs.neoforge.model;

import net.countered.terrainslabs.util.OnTopHelper;
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
import org.joml.Vector3f;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SlabOffsetModel implements BlockStateModel {

    private final BlockStateModel wrapped;

    public SlabOffsetModel(BlockStateModel wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public void collectParts(
            BlockAndTintGetter level,
            BlockPos pos,
            BlockState state,
            RandomSource random,
            List<BlockStateModelPart> parts
    ) {
        boolean slabBelow = isPosSlab(level, pos.below());
        boolean isAboveShifted = isAboveShifted(level, state, pos);
        boolean shouldShift = isAboveShifted || slabBelow;

        if ((!shouldShift && !hasSlabNeighbor(level, pos)) || !OnTopHelper.terrain_slabs$isStateValidOnTop(state)) {
            wrapped.collectParts(level, pos, state, random, parts);
            return;
        }

        List<BlockStateModelPart> originalParts = new ArrayList<>();
        wrapped.collectParts(level, pos, state, random, originalParts);

        for (BlockStateModelPart part : originalParts) {
            java.util.Map<Direction, List<BakedQuad>> newCullQuads = new java.util.HashMap<>();
            List<BakedQuad> newUnculledQuads = new ArrayList<>();

            for (Direction dir : Direction.values()) {
                List<BakedQuad> originalQuads = part.getQuads(dir);
                if (originalQuads != null && !originalQuads.isEmpty()) {

                    boolean neighborOnSlab = isNeighborOnSlab(level, pos, dir);

                    for (BakedQuad quad : originalQuads) {
                        BakedQuad modifiedQuad = quad;
                        if (shouldShift) {
                            modifiedQuad = moveQuadDown(modifiedQuad);
                        }

                        if (shouldShift || neighborOnSlab) {
                            newUnculledQuads.add(modifiedQuad);
                        } else {
                            newCullQuads.computeIfAbsent(dir, k -> new ArrayList<>()).add(modifiedQuad);
                        }
                    }
                }
            }

            List<BakedQuad> originalUnculled = part.getQuads(null);
            if (originalUnculled != null) {
                for (BakedQuad quad : originalUnculled) {
                    BakedQuad modifiedQuad = quad;
                    if (shouldShift) {
                        modifiedQuad = moveQuadDown(modifiedQuad);
                    }
                    newUnculledQuads.add(modifiedQuad);
                }
            }

            parts.add(new BlockStateModelPart() {
                @Override
                public List<BakedQuad> getQuads(@Nullable Direction side) {
                    if (side == null) {
                        return newUnculledQuads;
                    }
                    return newCullQuads.getOrDefault(side, List.of());
                }

                @Override
                public boolean useAmbientOcclusion() {
                    return part.useAmbientOcclusion();
                }

                @Override
                public Material.Baked particleMaterial() {
                    return part.particleMaterial();
                }

                @Override
                public @BakedQuad.MaterialFlags int materialFlags() {
                    return part.materialFlags();
                }
            });
        }
    }

    private BakedQuad moveQuadDown(BakedQuad quad) {
        Vector3f p0 = new Vector3f(quad.position0().x(), quad.position0().y() - 0.5f, quad.position0().z());
        Vector3f p1 = new Vector3f(quad.position1().x(), quad.position1().y() - 0.5f, quad.position1().z());
        Vector3f p2 = new Vector3f(quad.position2().x(), quad.position2().y() - 0.5f, quad.position2().z());
        Vector3f p3 = new Vector3f(quad.position3().x(), quad.position3().y() - 0.5f, quad.position3().z());

        return new BakedQuad(
                p0, p1, p2, p3,
                quad.packedUV0(), quad.packedUV1(), quad.packedUV2(), quad.packedUV3(),
                quad.direction(),
                quad.materialInfo(),
                quad.bakedNormals(), quad.bakedColors()
        );
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
    public @Nullable Object createGeometryKey(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random) {
        return wrapped.createGeometryKey(level, pos, state, random);
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
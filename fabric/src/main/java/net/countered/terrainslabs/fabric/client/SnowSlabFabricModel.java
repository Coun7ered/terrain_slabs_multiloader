package net.countered.terrainslabs.fabric.client;

import net.countered.terrainslabs.block.customslabs.soilslabs.GrassSlab;
import net.countered.terrainslabs.snowslab.SnowSlabBlockEntity;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.function.Supplier;

/**
 * Fabric dynamic model for the snow slab. Emits the snow (wrapped model) plus the
 * stored bottom-slab's quads, manually re-emitted so they keep:
 *   - correct render layer (cutout for grass etc.) via BlendMode
 *   - biome tint, by baking the slab's block-color into tinted quads' vertex colors
 */
@SuppressWarnings({"deprecation", "removal"})
public class SnowSlabFabricModel extends ForwardingBakedModel {

    public SnowSlabFabricModel(BakedModel wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos,
                               Supplier<RandomSource> randomSupplier, RenderContext context) {
        // 1) Snow on top.
        super.emitBlockQuads(blockView, state, pos, randomSupplier, context);

        // 2) Stored bottom slab.
        BlockState slabState = null;
        if (blockView.getBlockEntity(pos) instanceof SnowSlabBlockEntity be) {
            slabState = be.getBottomSlab();
        }
        if (slabState == null || slabState.isAir()) return;

        // Grass (and any SNOWY-aware slab) should show its snowy, untinted side when
        // snow sits on top - matching vanilla snow-on-grass.
        if (slabState.getBlock() instanceof GrassSlab && slabState.hasProperty(GrassSlab.SNOWY)) {
            slabState = slabState.setValue(GrassSlab.SNOWY, true);
        }

        BakedModel slabModel = Minecraft.getInstance().getBlockRenderer().getBlockModel(slabState);
        BlockColors blockColors = Minecraft.getInstance().getBlockColors();

        Renderer renderer = RendererAccess.INSTANCE.getRenderer();
        // Cutout-mipped covers grass overlay transparency; solid slabs are unaffected.
        RenderMaterial cutout = renderer.materialFinder()
                .blendMode(BlendMode.CUTOUT_MIPPED).find();

        QuadEmitter emitter = context.getEmitter();
        RandomSource random = randomSupplier.get();

        // Emit all faces EXCEPT the slab's top (UP) - it's the interior seam under
        // the snow and never visible.
        emitFace(null, slabModel, slabState, blockView, pos, random, emitter, cutout, blockColors);
        for (Direction dir : Direction.values()) {
            if (dir == Direction.UP) continue;
            emitFace(dir, slabModel, slabState, blockView, pos, random, emitter, cutout, blockColors);
        }
    }

    private void emitFace(Direction cullFace, BakedModel slabModel, BlockState slabState,
                          BlockAndTintGetter view, BlockPos pos, RandomSource random,
                          QuadEmitter emitter, RenderMaterial material, BlockColors blockColors) {
        List<BakedQuad> quads = slabModel.getQuads(slabState, cullFace, random);
        for (BakedQuad quad : quads) {
            emitter.fromVanilla(quad, material, cullFace);
            emitter.emit();
        }
    }
}

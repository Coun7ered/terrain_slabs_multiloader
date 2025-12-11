package net.countered.terrainslabs.forge.client;

import net.countered.platform.ClientPlatformHooks;
import net.countered.terrainslabs.block.ModBlocksRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.Blocks;

public class ClientPlatformHooksForge extends ClientPlatformHooks {

    @Override
    public void registerRenderLayers() {
        ItemBlockRenderTypes.setRenderLayer(ModBlocksRegistry.ICE_SLAB.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModBlocksRegistry.GRASS_SLAB.get(), RenderType.cutoutMipped());
        // TODO ontop
    }

    @Override
    public void registerBlockColorProviders() {
        Minecraft.getInstance().getBlockColors().register(
                (state, world, pos, tintIndex) ->
                        world != null && pos != null
                                ? BiomeColors.getAverageGrassColor(world, pos)
                                : GrassColor.getDefaultColor(),
                ModBlocksRegistry.GRASS_SLAB.get()
        );
    }

    @Override
    public void registerItemColorProviders() {
        Minecraft.getInstance().getItemColors().register(
                (stack, tintIndex) ->
                        Minecraft.getInstance().getBlockColors()
                                .getColor(Blocks.GRASS_BLOCK.defaultBlockState(), null, null, tintIndex),
                ModBlocksRegistry.GRASS_SLAB.get()
        );
    }

    @Override
    public void registerBuiltinResourcePacks() {

    }
}


package net.countered.platform.forge;

import net.countered.terrainslabs.block.ModBlocksRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.Blocks;

public class ClientPlatformHooksForge {

    public void registerRenderLayers() {

        // TODO ontop
    }

    public void registerBlockColorProviders() {
        Minecraft.getInstance().getBlockColors().register(
                (state, world, pos, tintIndex) ->
                        world != null && pos != null
                                ? BiomeColors.getAverageGrassColor(world, pos)
                                : GrassColor.getDefaultColor(),
                ModBlocksRegistry.GRASS_SLAB.get()
        );
    }

    public void registerItemColorProviders() {
        Minecraft.getInstance().getItemColors().register(
                (stack, tintIndex) ->
                        Minecraft.getInstance().getBlockColors()
                                .getColor(Blocks.GRASS_BLOCK.defaultBlockState(), null, null, tintIndex),
                ModBlocksRegistry.GRASS_SLAB.get()
        );
    }


    public void registerBuiltinResourcePacks() {

    }
}


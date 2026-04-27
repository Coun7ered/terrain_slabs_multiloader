package net.countered.terrainslabs.platform.fabric;

import com.mojang.serialization.Codec;
import net.countered.terrainslabs.TerrainSlabs;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.chunk.ChunkAccess;

import java.util.ArrayList;
import java.util.List;

public class PlatformChunkPersistenceImpl {

    public static final AttachmentType<List<BlockPos>> BOT_SLAB_POSITIONS = AttachmentRegistry.create(
            ResourceLocation.fromNamespaceAndPath(TerrainSlabs.MOD_ID, "bot_slab_positions"),
            builder -> builder
                    .persistent(Codec.list(BlockPos.CODEC))
                    .initializer(ArrayList::new)
    );

    public static List<BlockPos> getBotSlabPositions(ChunkAccess chunk) {
        return chunk.getAttached(BOT_SLAB_POSITIONS);
    }

    public static void setBotSlabPositions(ChunkAccess chunk, List<BlockPos> blockPosList) {
        chunk.setAttached(BOT_SLAB_POSITIONS, blockPosList);
    }
}

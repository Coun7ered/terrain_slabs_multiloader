package net.countered.terrainslabs.snowslab;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.countered.terrainslabs.TerrainSlabs;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Supplier;

/**
 * Standalone registration for the TEST snow slab. Mirrors the mod's existing
 * ModBlocksRegistry conventions (Architectury DeferredRegister + RegistrySupplier)
 * but keeps everything self-contained so it can be removed in one piece.
 *
 * Call SnowSlabRegistry.register() from TerrainSlabs.init() (one line) to enable it.
 */
@SuppressWarnings("UnstableApiUsage")
public class SnowSlabRegistry {

    private static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(TerrainSlabs.MOD_ID, Registries.BLOCK);
    private static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(TerrainSlabs.MOD_ID, Registries.ITEM);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(TerrainSlabs.MOD_ID, Registries.BLOCK_ENTITY_TYPE);

    public static final RegistrySupplier<Block> SNOW_SLAB = BLOCKS.register(
            "snow_slab_test",
            () -> new SnowSlabBlock(BlockBehaviour.Properties.of()
                    // Vanilla SnowLayerBlock uses hardness 0.1 and does NOT require the
                    // correct tool for drops, so it breaks fast by hand and faster with
                    // a shovel. Match that here.
                    .strength(0.1F)
                    .sound(net.minecraft.world.level.block.SoundType.SNOW)
                    .noOcclusion()
                    .isViewBlocking((state, level, pos) -> false)
                    .isSuffocating((state, level, pos) -> false)
                    .randomTicks()));

    public static final RegistrySupplier<Item> SNOW_SLAB_ITEM = ITEMS.register(
            "snow_slab_test",
            () -> new BlockItem(SNOW_SLAB.get(), new Item.Properties()));

    public static final RegistrySupplier<BlockEntityType<SnowSlabBlockEntity>> SNOW_SLAB_BE =
            BLOCK_ENTITIES.register("snow_slab_test", () -> blockEntityType());

    @SuppressWarnings("ConstantConditions")
    private static BlockEntityType<SnowSlabBlockEntity> blockEntityType() {
        // Build via the vanilla builder; null datafixer type is standard for mods.
        return BlockEntityType.Builder
                .of(SnowSlabBlockEntity::new, SNOW_SLAB.get())
                .build(null);
    }

    public static void register() {
        BLOCKS.register();
        ITEMS.register();
        BLOCK_ENTITIES.register();
    }
}

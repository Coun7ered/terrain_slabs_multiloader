package net.countered.datagen;


import net.countered.terrainslabs.block.customslabs.specialslabs.CustomSlab;
import net.countered.terrainslabs.registries.ModBlocksRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider extends FabricBlockLootTableProvider {


    public ModLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        HolderLookup.RegistryLookup<Enchantment> enchantmentRegistryLookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);

        this.add(ModBlocksRegistry.DIRT_SLAB.get(), block -> silkSlabDrops(block, Blocks.DIRT));
        this.add(ModBlocksRegistry.MUD_SLAB.get(), block -> silkSlabDrops(block, Blocks.MUD));
        this.add(ModBlocksRegistry.COARSE_SLAB.get(), block -> silkSlabDrops(block, Blocks.COARSE_DIRT));
        this.add(ModBlocksRegistry.DEEPSLATE_SLAB.get(), block -> silkSlabDrops(block, Blocks.COBBLED_DEEPSLATE));
        this.add(ModBlocksRegistry.MOSS_SLAB.get(), block -> silkSlabDrops(block, Blocks.MOSS_BLOCK));

        //terralith
        this.add(ModBlocksRegistry.CALCITE_SLAB.get(), block -> silkSlabDrops(block, Blocks.CALCITE));
        this.add(ModBlocksRegistry.SMOOTH_BASALT_SLAB.get(), block -> silkSlabDrops(block, Blocks.SMOOTH_BASALT));
        this.add(ModBlocksRegistry.LIGHT_BLUE_TERRACOTTA_SLAB.get(), block -> silkSlabDrops(block, Blocks.LIGHT_BLUE_TERRACOTTA));
        this.add(ModBlocksRegistry.CYAN_TERRACOTTA_SLAB.get(), block -> silkSlabDrops(block, Blocks.CYAN_TERRACOTTA));
        this.add(ModBlocksRegistry.ICE_SLAB.get(), block -> silkSlabDrops(block, Blocks.ICE));
        this.add(ModBlocksRegistry.ROOTED_DIRT_SLAB.get(), block -> silkSlabDrops(block, Blocks.ROOTED_DIRT));
        this.add(ModBlocksRegistry.PACKED_MUD_SLAB.get(), block -> silkSlabDrops(block, Blocks.PACKED_MUD));
        this.add(ModBlocksRegistry.BLUE_ICE_SLAB.get(), block -> silkSlabDrops(block, Blocks.BLUE_ICE));
        this.add(ModBlocksRegistry.BLACK_TERRACOTTA_SLAB.get(), block -> silkSlabDrops(block, Blocks.BLACK_TERRACOTTA));
        this.add(ModBlocksRegistry.CUSTOM_PRISMARINE_SLAB.get(), block -> silkSlabDrops(block, Blocks.PRISMARINE));
        this.add(ModBlocksRegistry.SAND_SLAB.get(), block -> silkSlabDrops(block, Blocks.SAND));
        this.add(ModBlocksRegistry.RED_SAND_SLAB.get(), block -> silkSlabDrops(block, Blocks.RED_SAND));

        this.add(ModBlocksRegistry.TERRACOTTA_SLAB.get(), block -> silkSlabDrops(block, Blocks.TERRACOTTA));
        this.add(ModBlocksRegistry.RED_TERRACOTTA_SLAB.get(), block -> silkSlabDrops(block, Blocks.RED_TERRACOTTA));
        this.add(ModBlocksRegistry.ORANGE_TERRACOTTA_SLAB.get(), block -> silkSlabDrops(block, Blocks.ORANGE_TERRACOTTA));
        this.add(ModBlocksRegistry.LIGHT_GRAY_TERRACOTTA_SLAB.get(), block -> silkSlabDrops(block, Blocks.LIGHT_GRAY_TERRACOTTA));
        this.add(ModBlocksRegistry.WHITE_TERRACOTTA_SLAB.get(), block -> silkSlabDrops(block, Blocks.WHITE_TERRACOTTA));
        this.add(ModBlocksRegistry.BROWN_TERRACOTTA_SLAB.get(), block -> silkSlabDrops(block, Blocks.BROWN_TERRACOTTA));
        this.add(ModBlocksRegistry.YELLOW_TERRACOTTA_SLAB.get(), block -> silkSlabDrops(block, Blocks.YELLOW_TERRACOTTA));

        this.add(ModBlocksRegistry.CUSTOM_STONE_SLAB.get(), block -> silkDropsOther(block, Blocks.STONE_SLAB, Blocks.COBBLESTONE));
        this.add(ModBlocksRegistry.CUSTOM_ANDESITE_SLAB.get(), block -> silkDropsOther(block, Blocks.ANDESITE_SLAB, Blocks.ANDESITE));
        this.add(ModBlocksRegistry.CUSTOM_DIORITE_SLAB.get(), block -> silkDropsOther(block, Blocks.DIORITE_SLAB, Blocks.DIORITE));
        this.add(ModBlocksRegistry.CUSTOM_GRANITE_SLAB.get(), block -> silkDropsOther(block, Blocks.GRANITE_SLAB, Blocks.GRANITE));
        this.add(ModBlocksRegistry.CUSTOM_SANDSTONE_SLAB.get(), block -> silkDropsOther(block, Blocks.SANDSTONE_SLAB, Blocks.SANDSTONE));
        this.add(ModBlocksRegistry.CUSTOM_RED_SANDSTONE_SLAB.get(), block -> silkDropsOther(block, Blocks.RED_SANDSTONE_SLAB, Blocks.RED_SANDSTONE));
        this.add(ModBlocksRegistry.CUSTOM_COBBLESTONE_SLAB.get(), block -> silkDropsOther(block, Blocks.COBBLESTONE_SLAB, Blocks.COBBLESTONE));
        this.add(ModBlocksRegistry.CUSTOM_MOSSY_COBBLESTONE_SLAB.get(), block -> silkDropsOther(block, Blocks.MOSSY_COBBLESTONE_SLAB, Blocks.MOSSY_COBBLESTONE));
        this.add(ModBlocksRegistry.CUSTOM_COBBLED_DEEPSLATE_SLAB.get(), block -> silkDropsOther(block, Blocks.COBBLED_DEEPSLATE_SLAB, Blocks.COBBLED_DEEPSLATE));
        this.add(ModBlocksRegistry.CUSTOM_TUFF_SLAB.get(), block -> silkDropsOther(block, Blocks.TUFF_SLAB, Blocks.TUFF));

        this.add(ModBlocksRegistry.MYCELIUM_SLAB.get(), block -> silkSlabDrops(block, Blocks.DIRT));
        this.add(ModBlocksRegistry.PODZOL_SLAB.get(), block -> silkSlabDrops(block, Blocks.DIRT));
        this.add(ModBlocksRegistry.GRASS_SLAB.get(), block -> silkSlabDrops(block, Blocks.DIRT));
        this.add(ModBlocksRegistry.PATH_SLAB.get(), block -> silkSlabDrops(block, Blocks.DIRT));

        this.add(ModBlocksRegistry.SOUL_SAND_SLAB.get(), block -> silkSlabDrops(block, Blocks.SOUL_SAND));
        this.add(ModBlocksRegistry.SOUL_SOIL_SLAB.get(), block -> silkSlabDrops(block, Blocks.SOUL_SOIL));
        this.add(ModBlocksRegistry.NETHERRACK_SLAB.get(), block -> silkSlabDrops(block, Blocks.NETHERRACK));
        this.add(ModBlocksRegistry.WARPED_NYLIUM_SLAB.get(), block -> silkSlabDrops(block, Blocks.NETHERRACK));
        this.add(ModBlocksRegistry.CRIMSON_NYLIUM_SLAB.get(), block -> silkSlabDrops(block, Blocks.NETHERRACK));
        this.add(ModBlocksRegistry.BASALT_SLAB.get(), block -> silkSlabDrops(block, Blocks.BASALT));
        this.add(ModBlocksRegistry.CUSTOM_BLACKSTONE_SLAB.get(), block -> silkSlabDrops(block, Blocks.BLACKSTONE));
        this.add(ModBlocksRegistry.ENDSTONE_SLAB.get(), block -> silkSlabDrops(block, Blocks.END_STONE));

        this.add(ModBlocksRegistry.PACKED_ICE_SLAB.get(), this::onlySilkSlabDrops);

        this.add(ModBlocksRegistry.SNOW_SLAB.get(), block -> silkSlabDropsParts(block, Items.SNOWBALL));
        this.add(ModBlocksRegistry.CLAY_SLAB.get(), block -> silkSlabDropsParts(block, Items.CLAY_BALL));

        this.add(
                ModBlocksRegistry.GRAVEL_SLAB.get(),
                block -> gravelSlabDrops(block, Blocks.GRAVEL, Items.FLINT, enchantmentRegistryLookup)
        );
    }

    private LootTable.Builder snowDrops(Block block) {
        return LootTable.lootTable()
                .withPool(
                        LootPool.lootPool()
                                // Condition: Player exists
                                .when(LootItemEntityPropertyCondition.entityPresent(LootContext.EntityTarget.THIS))
                                .add(
                                        AlternativesEntry.alternatives(
                                                // 1. No silk touch -> snowballs
                                                AlternativesEntry.alternatives(
                                                        SnowLayerBlock.LAYERS.getPossibleValues(),
                                                        integer -> LootItem.lootTableItem(Items.SNOWBALL)
                                                                .when(
                                                                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                                                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SnowLayerBlock.LAYERS, integer))
                                                                )
                                                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(integer.floatValue())))
                                                ).when(hasSilkTouch()),

                                                // 2. Silk touch -> snow layer block
                                                AlternativesEntry.alternatives(
                                                        SnowLayerBlock.LAYERS.getPossibleValues(),
                                                        integer -> (integer == 8 ? LootItem.lootTableItem(Blocks.SNOW_BLOCK) : LootItem.lootTableItem(Blocks.SNOW))
                                                                .when(
                                                                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                                                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SnowLayerBlock.LAYERS, integer))
                                                                )
                                                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(integer.floatValue())))
                                                )
                                        )
                                )
                );
    }

    /**
     * Adds a loot table entry that makes the slab drop its base block instead of itself.
     */
    private LootTable.Builder gravelSlabDrops(Block slab, Block gravelDrop, Item flintDrop, HolderLookup.RegistryLookup<Enchantment> enc) {
        return LootTable.lootTable()
                .withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(
                                        AlternativesEntry.alternatives(
                                                // 1. Silk Touch returns 1 or 2 slabs
                                                LootItem.lootTableItem(slab)
                                                        .when(hasSilkTouch())
                                                        .apply(
                                                                SetItemCountFunction.setCount(ConstantValue.exactly(2.0F))
                                                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(slab)
                                                                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SlabBlock.TYPE, SlabType.DOUBLE)))
                                                        ),

                                                // 2. No Silk Touch + GENERATED=true -> Flint
                                                LootItem.lootTableItem(flintDrop)
                                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(slab)
                                                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CustomSlab.GENERATED, true)))
                                                        .when(BonusLevelTableCondition.bonusLevelFlatChance(enc.getOrThrow(Enchantments.FORTUNE),
                                                                0.1F, 0.14285715F, 0.25F, 1.0F // Fortune levels for flint drops
                                                        ))
                                                        .apply(
                                                                SetItemCountFunction.setCount(ConstantValue.exactly(2.0F))
                                                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(slab)
                                                                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SlabBlock.TYPE, SlabType.DOUBLE)))
                                                        ),

                                                // 3. No Silk Touch + GENERATED=true + No Flint -> Gravel Block
                                                LootItem.lootTableItem(gravelDrop)
                                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(slab)
                                                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CustomSlab.GENERATED, true)))
                                                        .apply(
                                                                SetItemCountFunction.setCount(ConstantValue.exactly(2.0F))
                                                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(slab)
                                                                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SlabBlock.TYPE, SlabType.DOUBLE)))
                                                        )
                                        )
                                )
                );
    }

    public LootTable.Builder silkSlabDrops(Block slab, Block drop) {
        return LootTable.lootTable()
                .withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(
                                        AlternativesEntry.alternatives(
                                                // 1. Primary: Silk Touch always drops the slab item
                                                LootItem.lootTableItem(slab)
                                                        .when(hasSilkTouch())
                                                        .apply(
                                                                SetItemCountFunction.setCount(ConstantValue.exactly(2.0F))
                                                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(slab)
                                                                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                                                                        .hasProperty(SlabBlock.TYPE, SlabType.DOUBLE)))
                                                        ),

                                                // 2. Secondary: If GENERATED=true (and no Silk Touch), drop the base block (e.g., Dirt)
                                                LootItem.lootTableItem(drop)
                                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(slab)
                                                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                                                        .hasProperty(CustomSlab.GENERATED, true)))
                                                        .apply(
                                                                SetItemCountFunction.setCount(ConstantValue.exactly(2.0F))
                                                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(slab)
                                                                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                                                                        .hasProperty(SlabBlock.TYPE, SlabType.DOUBLE)))
                                                        ),

                                                // 3. Fallback: If GENERATED=false, drop the slab item itself
                                                LootItem.lootTableItem(slab)
                                                        .apply(
                                                                SetItemCountFunction.setCount(ConstantValue.exactly(2.0F))
                                                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(slab)
                                                                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                                                                        .hasProperty(SlabBlock.TYPE, SlabType.DOUBLE)))
                                                        )
                                        )
                                )
                );
    }

    public LootTable.Builder silkDropsOther(Block originalSlab, Block silkSlab, Block dropBlock) {
        return LootTable.lootTable()
                .withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(
                                        AlternativesEntry.alternatives(
                                                // 1. Primary: Silk Touch always drops the slab item
                                                LootItem.lootTableItem(silkSlab)
                                                        .when(hasSilkTouch())
                                                        .apply(
                                                                SetItemCountFunction.setCount(ConstantValue.exactly(2.0F))
                                                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(originalSlab)
                                                                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                                                                        .hasProperty(SlabBlock.TYPE, SlabType.DOUBLE)))
                                                        ),

                                                // 2. Secondary: If GENERATED=true (and no Silk Touch), drop the base block (e.g., Dirt)
                                                LootItem.lootTableItem(dropBlock)
                                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(originalSlab)
                                                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                                                        .hasProperty(CustomSlab.GENERATED, true)))
                                                        .apply(
                                                                SetItemCountFunction.setCount(ConstantValue.exactly(2.0F))
                                                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(originalSlab)
                                                                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                                                                        .hasProperty(SlabBlock.TYPE, SlabType.DOUBLE)))
                                                        ),

                                                // 3. Fallback: If GENERATED=false, drop the slab item itself
                                                LootItem.lootTableItem(silkSlab)
                                                        .apply(
                                                                SetItemCountFunction.setCount(ConstantValue.exactly(2.0F))
                                                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(originalSlab)
                                                                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                                                                        .hasProperty(SlabBlock.TYPE, SlabType.DOUBLE)))
                                                        )
                                        )
                                )
                );
    }

    public LootTable.Builder silkSlabDropsParts(Block slab, Item drop) {
        return LootTable.lootTable()
                .withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(
                                        AlternativesEntry.alternatives(
                                                // 1. Silk Touch: Always returns the slab item (1 or 2)
                                                LootItem.lootTableItem(slab)
                                                        .when(hasSilkTouch())
                                                        .apply(
                                                                SetItemCountFunction.setCount(ConstantValue.exactly(2.0F))
                                                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(slab)
                                                                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                                                                        .hasProperty(SlabBlock.TYPE, SlabType.DOUBLE)))
                                                        ),

                                                // 2. No Silk Touch + GENERATED=true: Returns parts (4 for single, 8 for double)
                                                LootItem.lootTableItem(drop)
                                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(slab)
                                                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                                                        .hasProperty(CustomSlab.GENERATED, true)))
                                                        .apply(
                                                                SetItemCountFunction.setCount(ConstantValue.exactly(8.0F))
                                                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(slab)
                                                                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                                                                        .hasProperty(SlabBlock.TYPE, SlabType.DOUBLE)))
                                                        )
                                                        .apply(
                                                                SetItemCountFunction.setCount(ConstantValue.exactly(4.0F))
                                                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(slab)
                                                                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                                                                        .hasProperty(SlabBlock.TYPE, SlabType.BOTTOM)))
                                                        )
                                                        .apply(
                                                                SetItemCountFunction.setCount(ConstantValue.exactly(4.0F))
                                                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(slab)
                                                                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                                                                        .hasProperty(SlabBlock.TYPE, SlabType.TOP)))
                                                        ),

                                                // 3. Fallback (GENERATED=false): Drop the slab item normally
                                                LootItem.lootTableItem(slab)
                                                        .apply(
                                                                SetItemCountFunction.setCount(ConstantValue.exactly(2.0F))
                                                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(slab)
                                                                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                                                                        .hasProperty(SlabBlock.TYPE, SlabType.DOUBLE)))
                                                        )
                                        )
                                )
                );
    }

    public LootTable.Builder onlySilkSlabDrops(Block slab) {
        return LootTable.lootTable()
                .withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(
                                        AlternativesEntry.alternatives(
                                                // 1. Drop slab if Silk Touch is used
                                                LootItem.lootTableItem(slab)
                                                        .when(hasSilkTouch())
                                                        .apply(
                                                                SetItemCountFunction.setCount(ConstantValue.exactly(2.0F))
                                                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(slab)
                                                                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                                                                        .hasProperty(SlabBlock.TYPE, SlabType.DOUBLE)))
                                                        ),

                                                // 2. Drop slab if it was NOT naturally generated (player-placed fallback)
                                                LootItem.lootTableItem(slab)
                                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(slab)
                                                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                                                        .hasProperty(CustomSlab.GENERATED, false)))
                                                        .apply(
                                                                SetItemCountFunction.setCount(ConstantValue.exactly(2.0F))
                                                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(slab)
                                                                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                                                                        .hasProperty(SlabBlock.TYPE, SlabType.DOUBLE)))
                                                        )
                                        )
                                )
                );
    }
}

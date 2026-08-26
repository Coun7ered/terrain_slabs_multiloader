package net.countered.terrainslabs.mixin.offset.block_tweaks;

import net.countered.terrainslabs.api.helperInterface.IAttachedFaceOffset;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.RodBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin( value = {
        AmethystClusterBlock.class,
        RodBlock.class
} )
public abstract class AttachedFaceAssigner implements IAttachedFaceOffset {}

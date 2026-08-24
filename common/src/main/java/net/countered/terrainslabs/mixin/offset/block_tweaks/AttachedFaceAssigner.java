package net.countered.terrainslabs.mixin.offset.block_tweaks;

import net.countered.terrainslabs.api.helperInterface.IAttachedFaceOffset;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.EndRodBlock;
import net.minecraft.world.level.block.LightningRodBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin( value = {
        AmethystClusterBlock.class,
        LightningRodBlock.class,
        EndRodBlock.class
} )
public abstract class AttachedFaceAssigner implements IAttachedFaceOffset {}

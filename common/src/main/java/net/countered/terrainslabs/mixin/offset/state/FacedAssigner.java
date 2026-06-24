package net.countered.terrainslabs.mixin.offset.state;

import net.countered.terrainslabs.api.IFacedOffsetable;
import net.minecraft.world.level.block.*;
import org.spongepowered.asm.mixin.Mixin;

@Mixin( value = { WebBlock.class, EndRodBlock.class, LightningRodBlock.class, TorchBlock.class } )
public class FacedAssigner implements IFacedOffsetable {}

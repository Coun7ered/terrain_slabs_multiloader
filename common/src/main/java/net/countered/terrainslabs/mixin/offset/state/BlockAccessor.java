package net.countered.terrainslabs.mixin.offset.state;

import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Safe pre-init description Id access
 */
@Mixin( Block.class )
public interface BlockAccessor {
    @Accessor("descriptionId")
    String terrain_slabs$getDescriptionId();
}

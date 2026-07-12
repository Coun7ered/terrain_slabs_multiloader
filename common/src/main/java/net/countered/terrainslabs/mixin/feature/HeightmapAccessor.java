package net.countered.terrainslabs.mixin.feature;

import net.minecraft.world.level.levelgen.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin( Heightmap.class )
public interface HeightmapAccessor {

    @Invoker("setHeight")
    void terrain_slabs$setHeight(int x, int z, int value);

}

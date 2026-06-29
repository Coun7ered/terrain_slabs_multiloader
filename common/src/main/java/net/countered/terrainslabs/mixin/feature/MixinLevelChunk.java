package net.countered.terrainslabs.mixin.feature;

import net.countered.terrainslabs.generation.OffsetFeature;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.ProtoChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin( LevelChunk.class )
public class MixinLevelChunk {

    /**
     * Initiate fix for offset positions at last possible moment. (protoChunk members only used in "this" call after)
     */
    @ModifyVariable( method = "<init>(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/chunk/ProtoChunk;Lnet/minecraft/world/level/chunk/LevelChunk$PostLoadProcessor;)V", at = @At("HEAD"), argsOnly = true )
    private static ProtoChunk terrain_slabs$offsetGeneration(
            ProtoChunk pChunk, ServerLevel level, ProtoChunk unused,
            LevelChunk.PostLoadProcessor postLoad
    ) {
        OffsetFeature.run( level, pChunk );
        return pChunk;
    }

}

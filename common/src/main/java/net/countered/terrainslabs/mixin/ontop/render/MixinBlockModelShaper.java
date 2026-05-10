package net.countered.terrainslabs.mixin.ontop.render;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.countered.terrainslabs.block.interfaces.IOffsetState;
import net.minecraft.client.renderer.block.BlockModelShaper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;

@Mixin( BlockModelShaper.class )
public abstract class MixinBlockModelShaper {

    @WrapOperation( method = "getBlockModel", at = @At( value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;" ) )
    private Object terrain_slabs$replaceCopiedState(
            Map instance, Object o, Operation<Object> original
    ) {
        if ( !((IOffsetState) o).terrain_slabs$getOffset() ) {
            return original.call( instance, o );
        }

        return original.call( instance, ((IOffsetState) o).terrain_slabs$getOppositeState() );
    }

}

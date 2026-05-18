package net.countered.terrainslabs.mixin.ontop.state;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.countered.platform.PlatformConfigHooks;
import net.countered.terrainslabs.block.OffsetProperty;
import net.countered.terrainslabs.block.interfaces.IOffsetState;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.function.Function;

@Mixin( StateDefinition.class )
public abstract class MixinStateDefinition<O, S> {

    @Inject( method = "<init>", at = @At( "HEAD" ) )
    private static <O, S> void terrain_slabs$addOffsetProperty(
            Function<O, S> stateValueFunction, Object owner,
            StateDefinition.Factory<O, S> valueFunction,
            Map<String, Property<?>> propertiesByName, CallbackInfo ci
    ) {
        if ( !(owner instanceof Block ownerBlock) || !terrain_slabs$shouldAddOffsetStates( ownerBlock ) ) {
            return;
        }

        propertiesByName.put( "offset", OffsetProperty.ONTOP);
    }

//    /**
//     * correctly populate states to fix crash on offset state change
//     */
//    @WrapOperation( method = "<init>", at = @At( value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/StateHolder;populateNeighbours(Ljava/util/Map;)V" ) )
//    private void terrain_slabs$populateMirrorNeighbors(
//            StateHolder<O, S> instance, Map<Map<Property<?>, Comparable<?>>, S> possibleStateMap,
//            Operation<Void> original
//    ) {
//        original.call( instance, possibleStateMap);
//        if ( instance instanceof IOffsetState offsetState && offsetState.terrain_slabs$hasOffsetState() ) {
//            original.call( offsetState.terrain_slabs$getOppositeState(), possibleStateMap);
//        }
//    }

    //==================//
    // Helper Functions //
    //==================//

    @Unique
    private static boolean terrain_slabs$shouldAddOffsetStates( Block block ) {
        if ( terrain_slabs$isDefaultOffset( block ) ) {
            return !PlatformConfigHooks.excludeOnTop( block );
        }

        return PlatformConfigHooks.includeOntop( block );
    }

    // This is getting complicated, needs to be easier to read.
    @Unique
    private static boolean terrain_slabs$isDefaultOffset( Block block ) {
        if ( block instanceof BushBlock) {
            return PlatformConfigHooks.isVegetationOnSlabsEnabled();

        } else if ( block instanceof TorchBlock || block instanceof LanternBlock) {
            return !(block instanceof WallTorchBlock) && !(block instanceof RedstoneWallTorchBlock);

        } else if ( block instanceof SnowLayerBlock ) {
            return PlatformConfigHooks.isSnowOnSlabsEnabled();
        }

        return false;
    }

}

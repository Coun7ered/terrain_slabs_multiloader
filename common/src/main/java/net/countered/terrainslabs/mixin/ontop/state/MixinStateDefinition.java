package net.countered.terrainslabs.mixin.ontop.state;

import net.countered.platform.PlatformConfigHooks;
import net.countered.terrainslabs.block.OffsetProperty;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Map;
import java.util.function.Function;

@Mixin( StateDefinition.class )
public abstract class MixinStateDefinition {

    @ModifyVariable( method = "<init>", at = @At("HEAD"), argsOnly = true )
    private static <O, S> Map<String, Property<?>> terrain_slabs$addOffsetProperty(
            Map<String, Property<?>> propertiesByName,
            Function<O, S> stateValueFunction, O owner, StateDefinition.Factory<O, S> valueFunction
    ) {
        if ( !(owner instanceof Block ownerBlock) || !terrain_slabs$shouldAddOffsetStates( ownerBlock ) ) {
            return propertiesByName;
        }

        propertiesByName.put( "offset", OffsetProperty.ONTOP);
        return propertiesByName;
    }


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

    // TODO: Way to dynamically add/remove vegetation classes...
    // hint: someClass.isInstance(someObj)
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

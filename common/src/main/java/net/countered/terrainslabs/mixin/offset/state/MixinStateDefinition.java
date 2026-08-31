package net.countered.terrainslabs.mixin.offset.state;

import net.countered.terrainslabs.api.OffsetClasses;
import net.countered.terrainslabs.block.OffsetProperty;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Map;
import java.util.function.Function;

/**
 * Bake in all offset states for classes that are generally included.
 */
@Mixin( StateDefinition.class )
public abstract class MixinStateDefinition {

    /**
     * Many blocks are baked with offset states, we will check whether to use them later.
     */
    @ModifyVariable( method = "<init>", at = @At("HEAD"), argsOnly = true )
    private static <O, S> Map<String, Property<?>> terrain_slabs$addOffsetProperty(
            Map<String, Property<?>> propertiesByName,
            Function<O, S> stateValueFunction, O owner, StateDefinition.Factory<O, S> valueFunction
    ) {
        if ( !(owner instanceof Block) || propertiesByName.containsKey( "offset" ) ) {
            return propertiesByName;
        }

        boolean canOnTop = OffsetClasses.isDefaultOntop( (Block) owner );
        boolean canOnBottom = OffsetClasses.isDefaultOnbottom( (Block) owner );
        if ( canOnBottom && canOnTop ) {
            propertiesByName.put( "offset", OffsetProperty.ALL );
        } else if ( canOnBottom ) {
            propertiesByName.put( "offset", OffsetProperty.ONBOTTOM );
        } else if ( canOnTop ) {
            propertiesByName.put( "offset", OffsetProperty.ONTOP );
        }

        return propertiesByName;
    }
}

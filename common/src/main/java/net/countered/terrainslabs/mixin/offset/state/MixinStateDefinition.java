package net.countered.terrainslabs.mixin.offset.state;

import net.countered.terrainslabs.api.IExcludedFromOffset;
import net.countered.terrainslabs.block.OffsetProperty;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.GameMasterBlock;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Mixin( StateDefinition.class )
public abstract class MixinStateDefinition {

    /**
     * Many blocks are baked with offset states, we will check whether to use them later.
     * <p>
     * Hopefully a more intelligent system can be used later.
     */
    @ModifyVariable( method = "<init>", at = @At("HEAD"), argsOnly = true )
    private static <O, S> Map<String, Property<?>> terrain_slabs$addOffsetProperty(
            Map<String, Property<?>> propertiesByName,
            Function<O, S> stateValueFunction, O owner, StateDefinition.Factory<O, S> valueFunction
    ) {
        if ( !( owner instanceof Block) || terrain_slabs$instanceofExcludedClass( ( (Block) owner ) ) ) {
            return propertiesByName;
        }

        propertiesByName.put( "offset", OffsetProperty.ALL );
        return propertiesByName;
    }


    //=========//
    // Helpers //
    //=========//


    @Unique
    private static List<Class<?>> terrain_slabs$excludedClasses = new ArrayList<>( List.of(
            GameMasterBlock.class, EntityBlock.class, IExcludedFromOffset.class
    ) );

    @Unique
    private static boolean terrain_slabs$instanceofExcludedClass(Block block ) {
        for ( Class<?> clazz : terrain_slabs$excludedClasses ) {
            if ( clazz.isInstance( block ) ) {
                return true;
            }
        }

        return false;
    }
}

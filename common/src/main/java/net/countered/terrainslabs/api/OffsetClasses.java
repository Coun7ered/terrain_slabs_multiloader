package net.countered.terrainslabs.api;

import net.countered.terrainslabs.platform.PlatformConfigHooks;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * API for controlling classes that are enabled rather than just blocks.
 */
@SuppressWarnings("unused")
public final class OffsetClasses {


    //======//
    // Data //
    //======//


    private static final List<Class<?>> VEGETATION_ONTOP_CLASSES = new ArrayList<>( List.of(
            BushBlock.class, CactusBlock.class, SugarCaneBlock.class, BambooStalkBlock.class,
            BambooSaplingBlock.class, TwistingVinesPlantBlock.class, TwistingVinesBlock.class,
            WebBlock.class
    ) );
    private static final List<Class<?>> MISC_ONTOP_CLASSES = new ArrayList<>( List.of(
            TorchBlock.class, LanternBlock.class, CandleBlock.class, FlowerPotBlock.class,
            AmethystClusterBlock.class, LightningRodBlock.class, EndRodBlock.class,
            BaseCoralPlantBlock.class
    ) );
    private static final List<Class<?>> FIRE_ONTOP_CLASSES = new ArrayList<>( List.of(
            BaseFireBlock.class
    ) );
    private static final List<Class<?>> SNOWLAYER_ONTOP_CLASSES = new ArrayList<>( List.of(
            SnowLayerBlock.class
    ) );

    private static final List<Class<?>> VEGETATION_ONBOTTOM_CLASSES = new ArrayList<>( List.of(
            VineBlock.class, CaveVinesBlock.class, CaveVinesPlantBlock.class, HangingRootsBlock.class,
            WeepingVinesBlock.class, WeepingVinesPlantBlock.class, WebBlock.class, SporeBlossomBlock.class
    ) );
    private static final List<Class<?>> MISC_ONBOTTOM_CLASSES = new ArrayList<>( List.of(
            LanternBlock.class, AmethystClusterBlock.class, LightningRodBlock.class, EndRodBlock.class
    ) );

    private static final List<Class<?>> EXCLUDED_CLASSES = new ArrayList<>( List.of(
            GameMasterBlock.class, SaplingBlock.class, AzaleaBlock.class, BaseCoralWallFanBlock.class
    ) );


    //========================//
    // Data Retrieval Methods //
    //========================//


    public static boolean isDefaultOntop( Block block ) {
        if ( instanceOf( block, EXCLUDED_CLASSES ) ) {
            return false;
        }

        if ( instanceOf( block, VEGETATION_ONTOP_CLASSES ) ) {
            return PlatformConfigHooks.isVegetationOnSlabsEnabled();

        } else if ( instanceOf( block, MISC_ONTOP_CLASSES ) ) {
            return true;

        } else if ( instanceOf( block, FIRE_ONTOP_CLASSES ) ) {
            return PlatformConfigHooks.canFireBlocksOffset();

        } else if ( instanceOf( block, SNOWLAYER_ONTOP_CLASSES ) ) {
            return PlatformConfigHooks.isSnowOnSlabsEnabled();
        }

        return false;
    }

    public static boolean isDefaultOnbottom( Block block ) {
        if ( instanceOf( block, EXCLUDED_CLASSES ) ) {
            return false;
        }

        if ( instanceOf( block, VEGETATION_ONBOTTOM_CLASSES ) ) {
            return PlatformConfigHooks.isVegetationOnSlabsEnabled();

        } else return instanceOf( block, MISC_ONBOTTOM_CLASSES );
    }

    private static boolean instanceOf( Object obj, List<Class<?>> classList ) {
        for ( Class<?> clazz : classList ) {
            if ( clazz.isInstance( obj ) ) {
                return true;
            }
        }
        return false;
    }


    //====================//
    // Actual API Methods //
    //====================//


    public static void addDefaultClass( Class<?> clazz, String category ) {
        addDefaultClass( clazz, Category.valueOf( category ) );
    }
    public static void addDefaultClass( Class<?> clazz, Category category ) {
        switch ( category ) {
            case ONTOP_VEGETATION:
                VEGETATION_ONTOP_CLASSES.add( clazz );
                break;
            case ONBOTTOM_VEGETATION:
                VEGETATION_ONBOTTOM_CLASSES.add( clazz );
                break;
            case ONTOP_MISC:
                MISC_ONTOP_CLASSES.add( clazz );
                break;
            case ONBOTTOM_MISC:
                MISC_ONBOTTOM_CLASSES.add( clazz );
                break;
            case ONTOP_FIRE:
                FIRE_ONTOP_CLASSES.add( clazz );
                break;
            case ONTOP_SNOWLAYER:
                SNOWLAYER_ONTOP_CLASSES.add( clazz );
                break;
            case EXCLUDED:
                EXCLUDED_CLASSES.add( clazz );
                break;
            default:
                throw new IllegalArgumentException(
                        "Category " + category + " is not valid for enum OffsetCategory."
                );
        }
    }

    public enum Category implements StringRepresentable {
        ONTOP_VEGETATION( "ontop_vegetation" ),
        ONBOTTOM_VEGETATION( "onbottom_vegetation" ),
        ONTOP_MISC( "ontop_misc" ),
        ONBOTTOM_MISC( "onbottom_misc" ),
        ONTOP_FIRE( "ontop_fire" ),
        ONTOP_SNOWLAYER( "ontop_snowlayer" ),
        EXCLUDED("excluded");

        final String name;
        Category(String name ) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return name;
        }
    }
}

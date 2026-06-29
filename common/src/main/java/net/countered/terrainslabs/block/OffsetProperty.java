package net.countered.terrainslabs.block;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class OffsetProperty {

    public static final List<EnumProperty<OffsetType>> ALL_PROPERTIES = new ArrayList<>( 3 );

    // Properties
    public static final EnumProperty<OffsetType> ONTOP = registerProperty( () -> EnumProperty.create(
            "offset", OffsetType.class, OffsetType.NONE, OffsetType.ONTOP ) );
    public static final EnumProperty<OffsetType> ONBOTTOM = registerProperty( () -> EnumProperty.create(
            "offset", OffsetType.class, OffsetType.NONE, OffsetType.ONBOTTOM ) );
    public static final EnumProperty<OffsetType> ALL = registerProperty( () -> EnumProperty.create(
            "offset", OffsetType.class ) );

    // Helper Methods
    public static EnumProperty<OffsetType> getPropertyOf(BlockState state ) {
        for ( EnumProperty<OffsetType> property : ALL_PROPERTIES ) {
            if ( state.hasProperty( property ) ) {
                return property;
            }
        }

        return null;
    }

    private static EnumProperty<OffsetType> registerProperty( Supplier<EnumProperty<OffsetType>> supplier ) {
        EnumProperty<OffsetType> newProperty = supplier.get();
        ALL_PROPERTIES.add( newProperty );
        return newProperty;
    }

    // Types enum
    public enum OffsetType implements StringRepresentable {
        NONE("none"),
        ONTOP("ontop"),
        ONBOTTOM("onbottom");

        private final String name;
        OffsetType( String name ) {
            this.name= name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return name;
        }
    }
}

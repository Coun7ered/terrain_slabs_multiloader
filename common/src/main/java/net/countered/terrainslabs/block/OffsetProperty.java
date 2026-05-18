package net.countered.terrainslabs.block;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;

public class OffsetProperty {

    public static final EnumProperty<OffsetType> ONTOP = EnumProperty.create(
            "offset", OffsetType.class, OffsetType.NONE, OffsetType.ONTOP );

    public enum OffsetType implements StringRepresentable {
        NONE("none"),
        ONTOP("ontop");

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

package net.countered.terrainslabs.neoforge.client;

import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.*;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforgespi.locating.IModFile;

import java.nio.file.Path;
import java.util.Optional;

public class NeoForgeBuiltinPacks {

    @SubscribeEvent
    public static void addPack(AddPackFindersEvent event) {
        if (event.getPackType() != PackType.CLIENT_RESOURCES) return;

        event.addRepositorySource(consumer -> {
            Pack pack = createBuiltinPack();
            if (pack != null) {
                consumer.accept(pack);
            }
        });
    }

    private static Pack createBuiltinPack() {
        String id = "terrain_slabs:better_grass_slabs";

        IModFile modFile = ModList.get().getModFileById("terrain_slabs").getFile();
        Path packPath = modFile.findResource("resourcepacks/better_grass_slabs");

        if (packPath == null) return null;

        PackLocationInfo location = new PackLocationInfo(
                id,
                Component.literal("Better Grass Slabs"),
                PackSource.BUILT_IN,
                Optional.empty()
        );

        Pack.ResourcesSupplier supplier = new Pack.ResourcesSupplier() {
            @Override
            public PackResources openPrimary(PackLocationInfo location) {
                return new PathPackResources(location, packPath);
            }

            @Override
            public PackResources openFull(PackLocationInfo location, Pack.Metadata metadata) {
                return openPrimary(location);
            }
        };

        PackSelectionConfig config = new PackSelectionConfig(
                false,
                Pack.Position.TOP,
                false
        );

        return Pack.readMetaAndCreate(
                location,
                supplier,
                PackType.CLIENT_RESOURCES,
                config
        );
    }
}

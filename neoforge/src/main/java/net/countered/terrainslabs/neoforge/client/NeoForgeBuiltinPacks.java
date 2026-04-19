package net.countered.terrainslabs.neoforge.client;

import net.countered.terrainslabs.TerrainSlabs;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforgespi.locating.IModFile;


import java.nio.file.Path;

@Mod.EventBusSubscriber(modid = TerrainSlabs.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NeoForgeBuiltinPacks {

    @SubscribeEvent
    public static void addPackFinders(AddPackFindersEvent event) {

        IModFile file = ModList.get().getModFileById(TerrainSlabs.MOD_ID).getFile();
        Path packPath = file.findResource("resourcepacks/better_grass_slabs");

        if (packPath == null) return;

        Pack.ResourcesSupplier supplier = id -> new PathPackResources(id, packPath, true);

        Pack.Info info = Pack.readPackInfo("better_grass_slabs", supplier);

        if (info == null) return;

        Pack pack = Pack.create(
                "better_grass_slabs",
                Component.literal("Better Grass Slabs"),
                false,
                supplier,
                info,
                PackType.CLIENT_RESOURCES,
                Pack.Position.TOP,
                false,
                PackSource.BUILT_IN
        );

        event.addRepositorySource((packConsumer) -> packConsumer.accept(pack));
    }
}

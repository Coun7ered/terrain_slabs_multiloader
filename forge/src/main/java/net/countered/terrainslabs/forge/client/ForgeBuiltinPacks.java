package net.countered.terrainslabs.forge.client;

import net.countered.terrainslabs.TerrainSlabs;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forgespi.locating.IModFile;

import java.nio.file.Path;

@Mod.EventBusSubscriber(modid = TerrainSlabs.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ForgeBuiltinPacks {

    @SubscribeEvent
    public static void addPackFinders(AddPackFindersEvent event) {

        IModFile file = ModList.get().getModFileById(TerrainSlabs.MOD_ID).getFile();
        Path packPath = file.findResource("resourcepacks/better_grass_slabs");

        System.out.println("pack path: "+ packPath);

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

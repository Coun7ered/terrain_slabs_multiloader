package net.countered.terrainslabs.snowslab;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.levelgen.Heightmap;

/**
 * Debug commands for the snow-slab system.
 *   /snowslabdebug            -> scan radius 4
 *   /snowslabdebug <radius>   -> scan a cube
 *   /snowslabdebug here       -> deep-inspect the single block under your feet
 */
public final class SnowSlabDebugCommand {

    private static final TagKey<Block> TS_ICE_SLABS = TagKey.create(
            Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("terrain_slabs", "ice_slabs"));
    private static final TagKey<Block> TS_PATH_SLABS = TagKey.create(
            Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("terrain_slabs", "path_slabs"));

    private SnowSlabDebugCommand() {}

    public static void register() {
        CommandRegistrationEvent.EVENT.register((dispatcher, registry, selection) -> {
            dispatcher.register(
                Commands.literal("snowslabdebug")
                    .requires(src -> src.hasPermission(2))
                    .executes(ctx -> scan(ctx.getSource(), 4))
                    .then(Commands.literal("here")
                        .executes(ctx -> inspectUnderFeet(ctx.getSource())))
                    .then(Commands.argument("radius", IntegerArgumentType.integer(0, 32))
                        .executes(ctx -> scan(ctx.getSource(),
                                IntegerArgumentType.getInteger(ctx, "radius"))))
            );
        });
    }

    private static int inspectUnderFeet(CommandSourceStack source) {
        if (!(source.getLevel() instanceof ServerLevel level)) {
            source.sendFailure(Component.literal("Not a server level."));
            return 0;
        }
        BlockPos feet = BlockPos.containing(source.getPosition());
        BlockPos slabPos = feet.below();
        BlockPos snowPos = slabPos.above();
        BlockState slabState = level.getBlockState(slabPos);
        Block block = slabState.getBlock();

        line(source, ChatFormatting.AQUA, "=== snowslabdebug HERE ===");
        line(source, ChatFormatting.WHITE, "slabPos=" + slabPos + "  snowPos=" + snowPos);
        line(source, ChatFormatting.WHITE, "block=" + block);

        boolean isSlab = block instanceof SlabBlock;
        String slabType = slabState.hasProperty(SlabBlock.TYPE)
                ? slabState.getValue(SlabBlock.TYPE).toString() : "n/a";
        boolean inTag = slabState.is(BlockTags.SLABS);
        boolean ice = slabState.is(TS_ICE_SLABS);
        boolean path = slabState.is(TS_PATH_SLABS);
        boolean isBottom = slabState.hasProperty(SlabBlock.TYPE)
                && slabState.getValue(SlabBlock.TYPE) == SlabType.BOTTOM;

        line(source, isSlab ? ChatFormatting.GREEN : ChatFormatting.RED,
                "instanceof SlabBlock = " + isSlab + "   slabType=" + slabType);
        line(source, (inTag ? ChatFormatting.GREEN : ChatFormatting.RED),
                "in BlockTags.SLABS = " + inTag);
        line(source, (!ice && !path ? ChatFormatting.GREEN : ChatFormatting.RED),
                "iceSlab=" + ice + "  pathSlab=" + path + " (both must be false)");
        line(source, (isBottom ? ChatFormatting.GREEN : ChatFormatting.RED),
                "isBottomSlab = " + isBottom);

        BlockState atSnow = level.getBlockState(snowPos);
        boolean snowSpotAir = atSnow.isAir();
        line(source, (snowSpotAir ? ChatFormatting.GREEN : ChatFormatting.RED),
                "snowPos block = " + atSnow.getBlock() + "  isAir=" + snowSpotAir);

        boolean raining = level.isRaining();
        boolean thundering = level.isThundering();
        boolean sky = level.canSeeSky(snowPos);
        boolean skySlab = level.canSeeSky(slabPos);
        line(source, (raining ? ChatFormatting.GREEN : ChatFormatting.RED),
                "isRaining = " + raining + "  isThundering=" + thundering);
        line(source, (sky ? ChatFormatting.GREEN : ChatFormatting.RED),
                "canSeeSky(snowPos) = " + sky + "   canSeeSky(slabPos)=" + skySlab);

        Biome biome = level.getBiome(slabPos).value();
        boolean hasPrecip = biome.hasPrecipitation();
        Biome.Precipitation precip = biome.getPrecipitationAt(slabPos);
        float baseTemp = biome.getBaseTemperature();
        boolean precipSnow = precip == Biome.Precipitation.SNOW;
        line(source, ChatFormatting.WHITE,
                "biome=" + level.getBiome(slabPos).unwrapKey()
                        .map(k -> k.location().toString()).orElse("?"));
        line(source, (hasPrecip ? ChatFormatting.GREEN : ChatFormatting.RED),
                "hasPrecipitation = " + hasPrecip);
        line(source, (precipSnow ? ChatFormatting.GREEN : ChatFormatting.RED),
                "getPrecipitationAt = " + precip + "   baseTemp=" + String.format("%.3f", baseTemp));

        BlockPos hmMotion = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, slabPos);
        BlockPos hmSurface = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, slabPos);
        boolean hmHitsSlab = hmMotion.getY() - 1 == slabPos.getY();
        line(source, ChatFormatting.YELLOW,
                "heightmap MOTION_BLOCKING=" + hmMotion + "  WORLD_SURFACE=" + hmSurface);
        line(source, (hmHitsSlab ? ChatFormatting.GREEN : ChatFormatting.RED),
                "heightmap top == this slab? " + hmHitsSlab
                        + "   (if false, vanilla tickIceAndSnow never targets it)");

        boolean wouldForm;
        try {
            wouldForm = SnowSlabBlock.canFormSnowOn(level, slabPos, snowPos);
        } catch (Throwable t) {
            wouldForm = false;
            line(source, ChatFormatting.RED, "canFormSnowOn threw: " + t);
        }
        final boolean fWould = wouldForm;
        line(source, (fWould ? ChatFormatting.GREEN : ChatFormatting.GOLD),
                ">>> canFormSnowOn() = " + fWould
                        + (fWould ? "  (would convert now)" : "  (blocked - see RED above)"));
        return 1;
    }

    private static void line(CommandSourceStack src, ChatFormatting color, String msg) {
        src.sendSuccess(() -> Component.literal(msg).withStyle(color), false);
    }

    private static int scan(CommandSourceStack source, int radius) {
        if (!(source.getLevel() instanceof ServerLevel level)) {
            source.sendFailure(Component.literal("Not a server level."));
            return 0;
        }
        BlockPos center = BlockPos.containing(source.getPosition());
        boolean raining = level.isRaining();
        line(source, ChatFormatting.AQUA,
                "[snowslabdebug] center=" + center + " radius=" + radius + " worldRaining=" + raining);

        int found = 0, would = 0;
        for (BlockPos slabPos : BlockPos.betweenClosed(
                center.offset(-radius, -radius, -radius),
                center.offset(radius, radius, radius))) {
            BlockState slabState = level.getBlockState(slabPos);
            if (!(slabState.getBlock() instanceof SlabBlock)) continue;
            found++;
            BlockPos p = slabPos.immutable();
            boolean ok = SnowSlabBlock.canFormSnowOn(level, p, p.above());
            if (ok) would++;
            final boolean fOk = ok;
            line(source, fOk ? ChatFormatting.GREEN : ChatFormatting.GRAY,
                    (fOk ? "[FORM] " : "[skip] ") + p.toShortString() + " " + slabState.getBlock());
        }
        final int fFound = found, fWould = would;
        line(source, ChatFormatting.YELLOW,
                "[snowslabdebug] slabs found=" + fFound + " would-form=" + fWould);
        return found;
    }
}

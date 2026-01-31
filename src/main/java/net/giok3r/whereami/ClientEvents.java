package net.giok3r.whereami;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import org.apache.commons.lang3.mutable.MutableInt;

@EventBusSubscriber(value = Dist.CLIENT, modid = WhereAmI.MODID)
public class ClientEvents {
    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.getDebugOverlay().showDebugScreen()) {
            return;
        }

        LocalPlayer player = mc.player;
        if (player == null) {
            return;
        }

        Font font = mc.font;
        Level level = player.level();

        MutableInt guiY = new MutableInt(10);

        renderLocation(event, guiY, player, font);

        renderBiome(event, guiY, level, player, font);

        renderTimeAndDay(event, guiY, level, font);
    }

    private static void renderLocation(RenderGuiEvent.Post event, MutableInt guiY, LocalPlayer player, Font font) {
        if (!Config.displayLocation && !Config.displayDirection) {
            return;
        }

        int x = player.blockPosition().getX();
        int y = player.blockPosition().getY();
        int z = player.blockPosition().getZ();

        CompassDirection direction = CompassDirection.fromAngle(player.getYRot());

        Component text;
        if (Config.displayLocation) {
            String directionSuffix = Config.displayDirection ? " " + direction.name() : "";
            text = Component.literal(x + ", " + y + ", " + z + directionSuffix);
        } else {
            text = Component.literal(direction.name());
        }
        event.getGuiGraphics().drawString(font, text, 10, guiY.getAndAdd(10), -1, true);
    }

    private static void renderBiome(RenderGuiEvent.Post event, MutableInt guiY, Level level, LocalPlayer player, Font font) {
        if (!Config.displayBiome) {
            return;
        }

        Holder<Biome> biomeHolder = level.getBiome(player.blockPosition());
        if (biomeHolder.getKey() != null) {
            var biomeId = biomeHolder.getKey().identifier();
            Component biomeName = Component.translatable("biome." + biomeId.getNamespace() + "." + biomeId.getPath());
            event.getGuiGraphics().drawString(font, biomeName, 10, guiY.getAndAdd(10), 0xFFCCCCCC, true);
        }
    }

    private static void renderTimeAndDay(RenderGuiEvent.Post event, MutableInt guiY, Level level, Font font) {
        if (!Config.displayTimeAndDay) {
            return;
        }

        int time = (int) level.getDayTime() % 24_000;
        int hour;
        String amOrPm;
        if (time < 7000) {
            // 6am-12pm
            hour = time / 1000 + 6;
            if (hour == 12) {
                amOrPm = "PM";
            } else {
                amOrPm = "AM";
            }
        } else if (time < 19_000) {
            // 1pm-12am
            hour = time / 1000 - 6;
            if (hour == 12) {
                amOrPm = "AM";
            } else {
                amOrPm = "PM";
            }
        } else {
            // 1am-5am
            hour = time / 1000 - 18;
            amOrPm = "AM";
        }
        int minute = (int) ((time % 1000) * 0.06);
        int day = (int) (level.getDayTime() / 24_000);
        String timeStr = String.format("%d:%02d %s (Day %d)", hour, minute, amOrPm, day);
        event.getGuiGraphics().drawString(font, timeStr, 10, guiY.getAndAdd(10), 0xFFAAAAAA, true);
    }
}

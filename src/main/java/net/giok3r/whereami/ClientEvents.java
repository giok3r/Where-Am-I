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

@EventBusSubscriber(value = Dist.CLIENT, modid = WhereAmI.MODID, bus = EventBusSubscriber.Bus.GAME)
public class ClientEvents {
    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        LocalPlayer player= Minecraft.getInstance().player;
        if(player==null) {
            return;
        }
        int x = (int) player.position().x;
        int y = (int) player.position().y;
        int z = (int) player.position().z;

        Font font= Minecraft.getInstance().font;
        Component text= Component.literal(x + ", " + y +", " + z);
        event.getGuiGraphics().drawString(font, text,10,10, -1, true);

        Level level = player.level();
        Holder<Biome> biomeHolder = level.getBiome(player.blockPosition());
        if (biomeHolder.getKey() != null) {
            var biomeId = biomeHolder.getKey().location();
            Component biomeName = Component.translatable("biome." + biomeId.getNamespace() + "." + biomeId.getPath());
            event.getGuiGraphics().drawString(font, biomeName, 10, 20, 0xFFCCCCCC, true);
        }

        int time = (int) level.getDayTime();
        int hour;
        String amOrPm;
        if (time <7000) {
            // 6am-12pm
            hour = time/1000+6;
            if (hour == 12) {
                amOrPm = "PM";
            } else {
                amOrPm = "AM";
            }
        } else if (time <19_000) {
            // 1pm-12am
            hour = time/1000-6;
            if (hour == 12) {
                amOrPm = "AM";
            } else {
                amOrPm = "PM";
            }
        } else {
            // 1am-5am
            hour = time/1000-18;
            amOrPm = "AM";
        }
        int minute = (int) ((time % 1000) * 0.06);
        String timeStr = String.format("%d:%02d %s", hour, minute, amOrPm);
        event.getGuiGraphics().drawString(font, timeStr, 10, 30, 0xFFAAAAAA, true);
    }
}

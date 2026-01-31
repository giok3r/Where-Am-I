package net.giok3r.whereami;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
@EventBusSubscriber(modid = WhereAmI.MODID)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue DISPLAY_LOCATION = BUILDER
            .comment("Show the player's location on screen")
            .define("display.location", true);
    private static final ModConfigSpec.BooleanValue DISPLAY_DIRECTION = BUILDER
            .comment("Show the compass direction the player is facing next to their coordinates")
            .define("display.direction", true);
    private static final ModConfigSpec.BooleanValue DISPLAY_BIOME = BUILDER
            .comment("Show the biome the player is in")
            .define("display.biome", true);
    private static final ModConfigSpec.BooleanValue DISPLAY_TIME_AND_DAY = BUILDER
            .comment("Show the time and in-game day")
            .define("display.time_and_day", true);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean displayLocation;
    public static boolean displayDirection;
    public static boolean displayBiome;
    public static boolean displayTimeAndDay;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        displayLocation = DISPLAY_LOCATION.get();
        displayDirection = DISPLAY_DIRECTION.get();
        displayBiome = DISPLAY_BIOME.get();
        displayTimeAndDay = DISPLAY_TIME_AND_DAY.get();
    }
}

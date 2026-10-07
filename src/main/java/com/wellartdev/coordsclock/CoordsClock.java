package com.wellartdev.coordsclock;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * A tiny always-on HUD widget: your block coordinates, the direction
 * you're facing, the in-game time and day, and the current biome.
 *
 * The widget sits in the bottom-left corner and refreshes every 10
 * ticks, so it always shows fresh data without any configuration —
 * just drop the mod in and play.
 *
 * Client-side only. No mixins, no networking, nothing persisted.
 */
public class CoordsClock implements ClientModInitializer {
    public static final String MOD_ID = "coordsclock";

    /** Refresh the widget state every 10 ticks (~0.5s). */
    private static final int REFRESH_TICKS = 10;

    // Read by the HUD element every frame; rebuilt by the tick below.
    private String lineOne = "";
    private String lineTwo = "";
    private int tick = 0;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);

        HudElementRegistry.addLast(Identifier.fromNamespaceAndPath(MOD_ID, "coords_clock"),
                (extractor, deltaTracker) -> {
                    if (lineOne.isEmpty() || lineTwo.isEmpty()) {
                        return;
                    }
                    Minecraft mc = Minecraft.getInstance();
                    int x = 6;
                    int y = mc.getWindow().getGuiScaledHeight() - 28;
                    extractor.text(mc.font, lineOne, x, y, 0xFFFFFF);
                    extractor.text(mc.font, lineTwo, x, y + 10, 0xFFFFFF);
                });
    }

    private void onTick(Minecraft mc) {
        tick++;
        if (tick % REFRESH_TICKS != 0) {
            return;
        }
        if (mc.player == null || mc.level == null) {
            lineOne = "";
            lineTwo = "";
            return;
        }

        BlockPos p = mc.player.blockPosition();
        String facing = capitalize(mc.player.getDirection().getName());
        lineOne = "XYZ: " + p.getX() + " / " + p.getY() + " / " + p.getZ() + "  (" + facing + ")";

        // In 26.2 the old Level#getDayTime() is gone; the overworld
        // clock time is the daylight-cycle tick count clocks display.
        long dayTime = mc.level.getOverworldClockTime();
        long day = dayTime / 24000L;
        long tod = dayTime % 24000L;
        int hours = (int) ((tod / 1000L + 6) % 24);
        int minutes = (int) ((tod % 1000L) * 60 / 1000);
        String clock = String.format("%02d:%02d", hours, minutes);

        // In 26.2 ResourceKey exposes the id via identifier(), not location().
        String biome = mc.level.getBiome(p).unwrapKey()
                .map(k -> prettify(k.identifier().getPath()))
                .orElse("unknown");
        lineTwo = "Day " + day + " — " + clock + " — " + biome;
    }

    /** Capitalizes the first letter, e.g. "north" -> "North". */
    private static String capitalize(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    /** Turns a biome id like "old_growth_pine_taiga" into "Old Growth Pine Taiga". */
    private static String prettify(String id) {
        return Arrays.stream(id.split("_"))
                .map(CoordsClock::capitalize)
                .collect(Collectors.joining(" "));
    }
}

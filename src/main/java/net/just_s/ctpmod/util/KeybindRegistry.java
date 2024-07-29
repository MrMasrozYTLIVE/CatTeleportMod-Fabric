package net.just_s.ctpmod.util;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.just_s.ctpmod.CTPMod;
import net.just_s.ctpmod.config.Point;

public class KeybindRegistry {
    public static void registerKeyBinds() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            for(Point point : CTPMod.CONFIG.points) {
                if(point.getKeybind().matchesCurrentKey()) CTPMod.startReconnect(point);
            }
        });
    }
}

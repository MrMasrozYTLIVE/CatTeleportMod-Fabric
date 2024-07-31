package net.just_s.ctpmod.util;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.util.Utils;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.ModifierKeyCodeImpl;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.just_s.ctpmod.CTPMod;
import net.just_s.ctpmod.config.KeyBind;
import net.just_s.ctpmod.config.ModConfig;
import net.just_s.ctpmod.config.Point;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class KeybindRegistry {
    public static void registerType() {
        AutoConfig.getGuiRegistry(ModConfig.class).registerTypeProvider((i18n, field, config, defaults, registry) -> {
            Point point = (Point) config;
            KeyBind keyBind = Utils.getUnsafely(field, point, CTPMod.DEFAULT_KEYBIND);

            List<AbstractConfigListEntry> list = new ArrayList<>();
            ConfigEntryBuilder builder = ConfigEntryBuilder.create();
            list.add(builder.startModifierKeyCodeField(Text.translatable(i18n), keyBind.toModifierKeyCode())
                    .setModifierSaveConsumer(point::setKeyBind)
                    .setAllowMouse(false)
                    .build());

            return list;
        }, KeyBind.class);
    }

    public static void registerEvent() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(client.currentScreen != null) return;

            for(Point point : CTPMod.CONFIG.points) {
                if(point.getKeyBind().toModifierKeyCode().matchesCurrentKey()) CTPMod.startReconnect(point);
            }
        });
    }
}

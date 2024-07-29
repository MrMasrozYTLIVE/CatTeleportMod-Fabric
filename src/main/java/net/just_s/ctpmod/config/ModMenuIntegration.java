package net.just_s.ctpmod.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.util.Utils;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.api.ModifierKeyCode;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> AutoConfig.getConfigScreen(ModConfig.class, parent).get();
    }

    public static void registerCustomTypes() {
        AutoConfig.getGuiRegistry(ModConfig.class).registerTypeProvider((i18n, field, config, defaults, registry) -> {
            Point point = (Point) config;
            ModifierKeyCode keyCode = Utils.getUnsafely(field, point, ModifierKeyCode.unknown());

            List<AbstractConfigListEntry> list = new ArrayList<>();
            ConfigEntryBuilder builder = ConfigEntryBuilder.create();
            list.add(builder.startModifierKeyCodeField(Text.translatable(i18n), keyCode)
                            .setModifierSaveConsumer(point::setKeybind)
                            .setAllowMouse(false)
                            .build());

            return list;
        }, ModifierKeyCode.class);
    }
}

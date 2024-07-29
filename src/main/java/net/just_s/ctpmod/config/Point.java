package net.just_s.ctpmod.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.clothconfig2.api.ModifierKeyCode;
import net.minecraft.client.option.KeyBinding;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Point {
    @ConfigEntry.Gui.Tooltip
    private String name;
    @ConfigEntry.Gui.Tooltip
    private int startPeriod;
    @ConfigEntry.Gui.Tooltip
    private int endPeriod;
    private ModifierKeyCode keybind = ModifierKeyCode.unknown();

    public Point(String name, int startPeriod, int endPeriod) {
        this(name, startPeriod, endPeriod, ModifierKeyCode.unknown());
    }

    @Override
    public String toString() {
        return "Point §f" + name + " §2with period: §f" + startPeriod + "-" + endPeriod + "§2 and keybind:§f" + keybind.toString() + "§2.";
    }
}

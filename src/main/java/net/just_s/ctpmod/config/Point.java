package net.just_s.ctpmod.config;

import lombok.*;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.clothconfig2.api.ModifierKeyCode;
import net.just_s.ctpmod.CTPMod;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Point {
    @ConfigEntry.Gui.Tooltip
    private String name;
    @ConfigEntry.Gui.Tooltip
    private float startPeriod;
    @ConfigEntry.Gui.Tooltip
    private float endPeriod;
    private KeyBind keyBind = CTPMod.DEFAULT_KEYBIND;

    public Point(String name, float startPeriod, float endPeriod) {
        this(name, startPeriod, endPeriod, CTPMod.DEFAULT_KEYBIND);
    }

    @Override
    public String toString() {
        return "Point §f" + name + " §2with period: §f" + startPeriod + "-" + endPeriod + "§2 with keybind: §f" + keyBind + "§2.";
    }

    public void setKeyBind(ModifierKeyCode modifierKeyCode) {
        this.keyBind = KeyBind.of(modifierKeyCode);
    }
}

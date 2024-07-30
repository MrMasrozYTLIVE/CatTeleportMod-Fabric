package net.just_s.ctpmod.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

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

    @Override
    public String toString() {
        return "Point §f" + name + " §2with period: §f" + startPeriod + "-" + endPeriod + "§2.";
    }
}

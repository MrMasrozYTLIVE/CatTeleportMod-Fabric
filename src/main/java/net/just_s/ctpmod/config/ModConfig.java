package net.just_s.ctpmod.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.just_s.ctpmod.CTPMod;

import java.util.ArrayList;
import java.util.List;

@Config(name = CTPMod.MOD_ID)
public class ModConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip
    public int delta = 0;
    public List<Point> points = new ArrayList<>();
}

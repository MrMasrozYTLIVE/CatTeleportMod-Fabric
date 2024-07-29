package net.just_s.ctpmod.config;

import com.google.gson.Gson;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class Point {
    @ConfigEntry.Gui.Tooltip
    private String name;
    @ConfigEntry.Gui.Tooltip
    private int startPeriod;
    @ConfigEntry.Gui.Tooltip
    private int endPeriod;

    public Point() {}

    public Point(String name, int startPeriod, int endPeriod) {
        this.name = name;
        this.startPeriod = startPeriod;
        this.endPeriod = endPeriod;
    }

    public String getName() {return this.name;}

    public void setName(String newName) {this.name = newName;}

    public String toJson() {
        Map<String, String> point = new HashMap<>();
        point.put("name", this.name);
        point.put("startPeriod", "" + this.startPeriod);
        point.put("endPeriod", "" + this.endPeriod);
        Gson gson = new Gson();
        return gson.toJson(point);
    }

    @Override
    public String toString() {
        return "Point §f" + name + " §2with period: §f" + startPeriod + "-" + endPeriod + "§2.";
    }

    public void setStartPeriod(int value) {
        this.startPeriod = value;
    }

    public void setEndPeriod(int value) {
        this.endPeriod = value;
    }

    public int getStartPeriod() {
        return this.startPeriod;
    }

    public int getEndPeriod() {
        return this.endPeriod;
    }
}

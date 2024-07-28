package net.just_s.ctpmod.config;

import me.shedaniel.clothconfig2.api.*;
import net.just_s.ctpmod.CTPMod;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;


public class ConfigScreen {
    public static ConfigCategory mainCategory;
    public static ConfigEntryBuilder entryBuilder;

    public static Screen buildScreen (Screen currentScreen) {
        // CTPMod.points = pass;
        // Сохранить CTP.points в json
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(currentScreen)
                .setTitle(Text.of("Point Menu"))
                .setTransparentBackground(true)
                .setSavingRunnable(CTPMod.config::save);

        // mainCategory - Это экран
        mainCategory = builder.getOrCreateCategory(Text.of("Null"));
        // entryBuilder - это билдер значения в точке
        entryBuilder = builder.entryBuilder();

        mainCategory.addEntry(entryBuilder.startIntField(Text.of("Delta"), CTPMod.delta)
                        .setDefaultValue(1)
                        .setTooltip(Text.of("Amount of seconds between server and client (can be negative)"))
                        .setSaveConsumer(newValue -> CTPMod.delta = newValue)
                .build());
        CTPMod.points.forEach(ConfigScreen::createOption);
        // Это потом позволит делать кейбинды
        // mainCategory.addEntry(entryBuilder.startModifierKeyCodeField().build());

        return builder.build();
    }

    public static void createOption(String pointName, Point point) {
        List<AbstractConfigListEntry> listOfEntries = new ArrayList<>();
        listOfEntries.add(entryBuilder.startStrField(Text.of("Point Name"), pointName)
                .setDefaultValue(pointName) // Recommended: Used when user click "Reset"
                .setTooltip(Text.of("The name of current Point")) // Optional: Shown when the user hover over this option
                .setSaveConsumer(point::setName) // Сохранить точку в CTPMod.points
                .build());
        listOfEntries.add(entryBuilder.startIntField(Text.of("Start Period Time"), point.getStartPeriod())
                .setDefaultValue(point.getStartPeriod())
                .setTooltip(Text.of("Amount of seconds before rejoining server"))
                .setSaveConsumer(point::setStartPeriod)
                .build());
        listOfEntries.add(entryBuilder.startIntField(Text.of("End Period Time"), point.getEndPeriod())
                .setDefaultValue(point.getEndPeriod())
                .setTooltip(Text.of("Amount of seconds after rejoining should not be executed"))
                .setSaveConsumer(point::setEndPeriod)
                .build());
        // startSubCategory - одна точка
        mainCategory.addEntry(entryBuilder.startSubCategory(Text.of(pointName), listOfEntries).build());
    }
}

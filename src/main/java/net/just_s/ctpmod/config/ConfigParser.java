package net.just_s.ctpmod.config;

import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;
import net.just_s.ctpmod.CTPMod;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.File;
import java.io.FileWriter;
import java.util.Objects;

public class ConfigParser {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static final ConfigParser INSTANCE = new ConfigParser(FabricLoader.getInstance().getConfigDir().resolve("ctpmod.json").toFile());
    static {
        INSTANCE.load();
    }

    public final File file;

    public ConfigParser(File file) {
        this.file = file;
    }

    public boolean deletePoint(String pointName) {
        Point point = CTPMod.points.remove(pointName);
        if(point == null) return false;

        save();
        return true;
    }

    public void addPoint(Point point) {
        //if (CTPMod.points == null) {CTPMod.points = new Point[]{};}
        CTPMod.points.put(point.getName(), point);
        save();
    }

    public void load() {
        CTPMod.LOGGER.debug("Loading config...");
        if (file.exists()) {
            try {
                String json_string = Files.readString(Path.of(file.toString()), StandardCharsets.US_ASCII);
                JsonObject json = JsonParser.parseString(json_string).getAsJsonObject();

                CTPMod.delta = json.getAsJsonPrimitive("delta").getAsInt();
                for(Point point : GSON.fromJson(json.getAsJsonArray("points").toString(), Point[].class)) {
                    CTPMod.points.put(point.getName(), point);
                }
            } catch (Exception e) {
                CTPMod.LOGGER.error("Could not load config from file '{}'", file.getAbsolutePath(), e);
            }
        }
        save();
    }

    public void save() {
        CTPMod.LOGGER.debug("Saving config...");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(toJson());
        } catch (Exception e) {
            CTPMod.LOGGER.error("Could not save config to file '{}'", file.getAbsolutePath(), e);
        }
    }

    protected String toJson(){
        StringBuilder json = new StringBuilder();
        for(Point point : CTPMod.points.values()) {
            json.append(point.toJson());
        }
        return "{\"delta\":" + CTPMod.delta + ", \"points\":[" + json + "]}";
    }
}

package net.just_s.ctpmod;

import dev.terminalmc.autoreconnectrf.AutoReconnect;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.shedaniel.clothconfig2.api.ModifierKeyCode;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.just_s.ctpmod.config.KeyBind;
import net.just_s.ctpmod.config.ModConfig;
import net.just_s.ctpmod.config.Point;
import net.just_s.ctpmod.util.CommandRegistry;
import net.just_s.ctpmod.util.KeybindRegistry;
import net.just_s.ctpmod.util.ReconnectThread;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.MinecraftClient;

import java.util.Objects;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class CTPMod implements ClientModInitializer {
	public static final String MOD_ID = "ctpmod";
	public static final MinecraftClient MC = MinecraftClient.getInstance();
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static final KeyBind DEFAULT_KEYBIND = KeyBind.of(ModifierKeyCode.unknown());
	public static ServerInfo currentServer = null;
	public static CTPMod INSTANCE = new CTPMod();
	private static ReconnectThread reconnectThread;
	public static ModConfig CONFIG;
	private static ConfigHolder<ModConfig> CONFIG_HOLDER;

	@Override
	public void onInitializeClient() {
		KeybindRegistry.registerType();
		AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
		CONFIG_HOLDER = AutoConfig.getConfigHolder(ModConfig.class);
		CONFIG = CONFIG_HOLDER.getConfig();

		CommandRegistry.register();
		KeybindRegistry.registerEvent();
		ClientPlayConnectionEvents.JOIN.register((networkHandler, packetSender, client) -> currentServer = client.getCurrentServerEntry());
	}

	static boolean isAutoReconnectPresent = FabricLoader.getInstance().isModLoaded("autoreconnectrf");
	public static void startReconnect(Point point) {
		if (CTPMod.MC.isInSingleplayer()) return;

		if (isAutoReconnectPresent) AutoReconnect.cancelAutoReconnect();

		Screen newScr = new DisconnectedScreen(
				new MultiplayerScreen(new TitleScreen()),
				Text.of("§8[§6CatTeleport§8]"),
				Text.of("startReconnect"));

		Objects.requireNonNull(CTPMod.MC.getNetworkHandler()).getConnection().disconnect(Text.of("reconnecting"));
		MC.disconnect();

		reconnectThread = new ReconnectThread(point.getStartPeriod(), point.getEndPeriod());
		reconnectThread.start();

		MC.setScreen(newScr);
	}

	public void finishReconnect() {
		connectToServer(currentServer);
	}

	public void cancelReconnect() {
		try {
			reconnectThread.join();
		} catch (InterruptedException | NullPointerException ignored) {}
		LOGGER.info("Reconnecting cancelled.");
		MC.setScreen(new DisconnectedScreen(new MultiplayerScreen(new TitleScreen()), Text.of("§8[§6CatTeleport§8]"), Text.of("cancelReconnect")));
	}

	public void connectToServer(ServerInfo targetInfo) {
		ConnectScreen.connect(new MultiplayerScreen(new TitleScreen()), MC, ServerAddress.parse(targetInfo.address), targetInfo, false, null);
	}

	public static Text generateFeedback(String message, Object... args) {
		//Send message in chat that only user can see
		//§0  black			§8	dark_gray		§g	minecoin_gold
		//§1  dark_blue		§9	blue			§f	white
		//§2  dark_green	§a	green			§7	gray
		//§3  dark_aqua		§b	aqua			§e	yellow
		//§4  dark_red		§c	red				§6	gold
		//§5  dark_purple	§d	light_purple
		for (int i = 0; i < args.length; i++) {
			message = message.replace("{" + i + "}", args[i].toString());
		}
		return Text.of("§8[§6CatTeleport§8]§2 " + message);
	}

	public static Point getPoint(String name) {
		if(name.isEmpty()) return null;
		Optional<Point> point = CONFIG.points.stream().filter(p -> p.getName().equalsIgnoreCase(name)).findFirst();
        return point.orElse(null);
    }

	public static void addPoint(Point point) {
		if(point == null) return;
		CONFIG.points.add(point);
		CONFIG_HOLDER.save();
	}

	public static boolean deletePoint(Point point) {
		boolean deleted = CONFIG.points.remove(point);
		CONFIG_HOLDER.save();
		return deleted;
	}

	public static boolean deletePoint(String name) {
		return deletePoint(getPoint(name));
	}
}

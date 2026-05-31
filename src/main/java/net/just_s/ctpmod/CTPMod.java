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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.network.chat.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Objects;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class CTPMod implements ClientModInitializer {
	public static final String MOD_ID = "ctpmod";
	public static final Minecraft MC = Minecraft.getInstance();
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static final KeyBind DEFAULT_KEYBIND = KeyBind.of(ModifierKeyCode.unknown());
	public static ServerData currentServer = null;
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
		ClientPlayConnectionEvents.JOIN.register((networkHandler, packetSender, client) -> currentServer = client.getCurrentServer());
	}

	static boolean isAutoReconnectPresent = FabricLoader.getInstance().isModLoaded("autoreconnectrf");
	public static void startReconnect(Point point) {
        Minecraft mc = CTPMod.MC;

		if (mc.isLocalServer()) {
            if(mc.player != null) mc.player.sendSystemMessage(Component.literal("§8[§6CatTeleport§8]§r Teleporting is disabled in SinglePlayer."));
            return;
        }

		if (isAutoReconnectPresent) AutoReconnect.cancelAutoReconnect();

		Screen newScr = new DisconnectedScreen(
				new JoinMultiplayerScreen(new TitleScreen()),
				Component.nullToEmpty("§8[§6CatTeleport§8]"),
				Component.nullToEmpty("startReconnect"));

		Objects.requireNonNull(mc.getConnection()).getConnection().disconnect(Component.nullToEmpty("reconnecting"));
		MC.disconnect(newScr, false);

		reconnectThread = new ReconnectThread(point.getStartPeriod(), point.getEndPeriod());
		reconnectThread.start();
	}

	public void finishReconnect() {
		connectToServer(currentServer);
	}

	public void cancelReconnect() {
		try {
			reconnectThread.join();
		} catch (InterruptedException | NullPointerException ignored) {}
		LOGGER.info("Reconnecting cancelled.");
		MC.setScreen(new DisconnectedScreen(new JoinMultiplayerScreen(new TitleScreen()), Component.nullToEmpty("§8[§6CatTeleport§8]"), Component.nullToEmpty("cancelReconnect")));
	}

	public void connectToServer(ServerData targetInfo) {
		ConnectScreen.startConnecting(new JoinMultiplayerScreen(new TitleScreen()), MC, ServerAddress.parseString(targetInfo.ip), targetInfo, false, null);
	}

	public static Component generateFeedback(String message, Object... args) {
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
		return Component.nullToEmpty("§8[§6CatTeleport§8]§2 " + message);
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

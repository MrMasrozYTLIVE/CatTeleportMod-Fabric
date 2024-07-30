package net.just_s.ctpmod.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.just_s.ctpmod.CTPMod;
import net.minecraft.client.MinecraftClient;

@Environment(EnvType.CLIENT)
public class ReconnectThread extends Thread {
    private final float secondsToReconnect;

    public ReconnectThread(float start, float end) {
        super();
        this.secondsToReconnect = (end - start) / 2 + start;
    }

    /**
     * Tries to connect to the server using a socket as many times as is set, and returns if it could connect
     */
    @Override
    public void run() {
        try {
            float seconds = Math.max(secondsToReconnect - CTPMod.delta, 0);
            CTPMod.LOGGER.info("reconnect in {} sec", seconds);

            Thread.sleep((long) (seconds * 1000L));
            synchronized (this) {
                CTPMod.LOGGER.info("Reconnecting to server.");
                MinecraftClient.getInstance().execute(CTPMod.INSTANCE::finishReconnect);
            }
            return;
        } catch (/*IOException* |*/ InterruptedException e) {
            CTPMod.LOGGER.error("Reconnection failed. Reason:", e);
        }
        MinecraftClient.getInstance().execute(CTPMod.INSTANCE::cancelReconnect);
    }
}

package net.just_s.ctpmod.commands;

import com.mojang.brigadier.context.CommandContext;
import me.shedaniel.clothconfig2.api.ModifierKeyCode;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.just_s.ctpmod.CTPMod;
import net.just_s.ctpmod.config.Point;

public class RcCommand {
    public static int run(CommandContext<FabricClientCommandSource> ctx) {
        int seconds = ctx.getArgument("seconds", int.class);
        if (CTPMod.MC.isInSingleplayer()) {
            ctx.getSource().sendFeedback(CTPMod.generateFeedback(
                    "§cCTPMod doesn't work in §4SinglePlayer§c. You can use §dNether Portals§c instead of rejoining."
            ));
            return 1;
        }
        CTPMod.startReconnect(new Point(null, seconds, seconds, ModifierKeyCode.unknown()));
        return 1;
    }
}

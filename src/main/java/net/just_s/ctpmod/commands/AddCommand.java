package net.just_s.ctpmod.commands;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.just_s.ctpmod.CTPMod;
import net.just_s.ctpmod.config.Point;

public class AddCommand {
    public static int run(CommandContext<FabricClientCommandSource> ctx) {
        String waypointName = ctx.getArgument("name", String.class);
        int startPeriod = ctx.getArgument("startPeriod", int.class);
        int endPeriod = ctx.getArgument("endPeriod", int.class);
        CTPMod.CONFIG.points.add(new Point(waypointName, startPeriod, endPeriod));
        ctx.getSource().sendFeedback(CTPMod.generateFeedback(
                "Point §f{0} §aadded§2 with period: §f{1}-{2}§2.",
                waypointName, startPeriod, endPeriod
        ));
        return 1;
    }
}

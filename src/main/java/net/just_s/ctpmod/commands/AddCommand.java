package net.just_s.ctpmod.commands;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.just_s.ctpmod.CTPMod;
import net.just_s.ctpmod.config.Point;

public class AddCommand {
    public static int run(CommandContext<FabricClientCommandSource> ctx) {
        String waypointName = ctx.getArgument("name", String.class);
        float startPeriod = ctx.getArgument("startPeriod", float.class);
        float endPeriod = ctx.getArgument("endPeriod", float.class);
        CTPMod.addPoint(new Point(waypointName, startPeriod, endPeriod));
        ctx.getSource().sendFeedback(CTPMod.generateFeedback(
                "Point §f{0} §aadded§2 with period: §f{1}-{2}§2.",
                waypointName, startPeriod, endPeriod
        ));
        return 1;
    }
}

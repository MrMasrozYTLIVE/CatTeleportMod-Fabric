package net.just_s.ctpmod.commands;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.just_s.ctpmod.CTPMod;
import net.just_s.ctpmod.config.Point;

import java.util.stream.Collectors;

public class ListCommand {
    public static int run(CommandContext<FabricClientCommandSource> ctx) {
        String message = "list of loaded Points:\n" + CTPMod.CONFIG.points.stream().map(Point::toString).collect(Collectors.joining("\n"));
        ctx.getSource().sendFeedback(CTPMod.generateFeedback(
                message
        ));
        return 1;
    }
}

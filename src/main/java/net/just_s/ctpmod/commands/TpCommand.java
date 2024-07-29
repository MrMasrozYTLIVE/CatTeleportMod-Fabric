package net.just_s.ctpmod.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.just_s.ctpmod.CTPMod;
import net.just_s.ctpmod.config.Point;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class TpCommand {
    public static CompletableFuture<Suggestions> suggest(CommandContext<FabricClientCommandSource> ignoredCtx, SuggestionsBuilder builder) {
        for (Point point : CTPMod.CONFIG.points) {
            builder.suggest(point.getName());
        }
        return builder.buildFuture();
    }

    public static int run(CommandContext<FabricClientCommandSource> ctx) {
        if (CTPMod.MC.isInSingleplayer()) {
            ctx.getSource().sendFeedback(CTPMod.generateFeedback(
                    "§cCTPMod doesn't work in §4SinglePlayer§c. You can use §dNether Portals§c instead of rejoining."
            ));
            return 1;
        }
        String pointName = ctx.getArgument("name", String.class);
        Optional<Point> point = CTPMod.CONFIG.points.stream().filter(p -> p.getName().equals(pointName)).findFirst();
        if (point.isEmpty()) {
            ctx.getSource().sendFeedback(CTPMod.generateFeedback(
                    "§cThere is no §fPoint §cwith name \"§f{0}§c\".",
                    pointName
            ));
            return 0;
        }

        //if everything is okay, only then start reconnect cycle:
        CTPMod.startReconnect(point.get());
        return 1;
    }
}

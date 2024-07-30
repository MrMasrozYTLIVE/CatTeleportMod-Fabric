package net.just_s.ctpmod.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.just_s.ctpmod.CTPMod;

import java.util.concurrent.CompletableFuture;

public class DeleteCommand {
    public static CompletableFuture<Suggestions> suggest(CommandContext<FabricClientCommandSource> ctx, SuggestionsBuilder builder) {
        return TpCommand.suggest(ctx, builder);
    }

    public static int run(CommandContext<FabricClientCommandSource> ctx) {
        String pointName = ctx.getArgument("name", String.class);

        if (CTPMod.deletePoint(pointName)) {
            ctx.getSource().sendFeedback(CTPMod.generateFeedback(
                    "Point §f{0} §cdeleted§2.",
                    pointName
            ));
        } else {
            ctx.getSource().sendFeedback(CTPMod.generateFeedback(
                    "Point §f{0} §cwas not found!§2.",
                    pointName
            ));
        }
        return 1;
    }
}

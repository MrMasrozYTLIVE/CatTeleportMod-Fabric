package net.just_s.ctpmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.just_s.ctpmod.CTPMod;
import net.just_s.ctpmod.config.Point;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.ServerCommandSource;

import java.util.stream.Collectors;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class ListCommand {
//    public static void register(CommandDispatcher<FabricClientCommandSource> fabricClientCommandSourceCommandDispatcher, CommandRegistryAccess commandRegistryAccess) {
//        fabricClientCommandSourceCommandDispatcher.register(
//                literal("ctp").then(
//                        literal("list").executes(ListCommand::run)
//                )
//        );
//    }

    public static int run(CommandContext<ServerCommandSource> ctx) {
        String message = "list of loaded Points:\n" + CTPMod.CONFIG.points.stream().map(Point::toString).collect(Collectors.joining("\n"));
        ctx.getSource().sendFeedback(CTPMod.generateFeedback(
                message
        ), false);
        return 1;
    }
}

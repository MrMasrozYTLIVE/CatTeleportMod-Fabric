package net.just_s.ctpmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.just_s.ctpmod.CTPMod;
import net.minecraft.command.CommandRegistryAccess;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class ListCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> fabricClientCommandSourceCommandDispatcher, CommandRegistryAccess commandRegistryAccess) {
        fabricClientCommandSourceCommandDispatcher.register(
                literal(CTPMod.MOD_CMD).then(
                        literal("list").executes(ListCommand::run)
                )
        );
    }

    private static int run(CommandContext<FabricClientCommandSource> ctx) {
        String message = "list of loaded Points:\n" + String.join("\n", CTPMod.points.keySet());
        ctx.getSource().sendFeedback(CTPMod.generateFeedback(
                message
        ));
        return 1;
    }
}

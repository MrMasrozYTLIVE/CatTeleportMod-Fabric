package net.just_s.ctpmod.util;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.just_s.ctpmod.commands.ListCommand;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;

public class CommandRegistry {
    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            LiteralCommandNode<ServerCommandSource> mainCommand = CommandManager
                    .literal("ctp")
                    .build();

            LiteralCommandNode<ServerCommandSource> listNode = CommandManager
                    .literal("list")
                    .executes(ListCommand::run)
                    .build();

            dispatcher.getRoot().addChild(mainCommand);
            mainCommand.addChild(listNode);
        });
    }
}

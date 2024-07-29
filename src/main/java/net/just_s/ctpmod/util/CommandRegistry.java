package net.just_s.ctpmod.util;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.just_s.ctpmod.commands.*;
import net.minecraft.command.CommandRegistryAccess;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class CommandRegistry {
    public static void registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register((CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) -> {
            LiteralCommandNode<FabricClientCommandSource> mainCommand = literal("ctp").build();

            // List SubCommand
            LiteralCommandNode<FabricClientCommandSource> listNode = literal("list").executes(ListCommand::run).build();

            // RC SubCommand
            LiteralCommandNode<FabricClientCommandSource> rcNode = literal("rc").build();
            rcNode.addChild(argument("seconds", IntegerArgumentType.integer(1))
                    .executes(RcCommand::run).build());

            // TP SubCommand
            LiteralCommandNode<FabricClientCommandSource> tpNode = literal("tp").build();
            tpNode.addChild(argument("name", StringArgumentType.word())
                    .suggests(TpCommand::suggest).executes(TpCommand::run).build());

            // Delete SubCommand
            LiteralCommandNode<FabricClientCommandSource> deleteNode = literal("delete").build();
            deleteNode.addChild(argument("name", StringArgumentType.word())
                    .suggests(DeleteCommand::suggest).executes(DeleteCommand::run).build());

            // Add SubCommand
            LiteralCommandNode<FabricClientCommandSource> addNode = literal("add").build();
            ArgumentCommandNode<FabricClientCommandSource, String> nameArgument = argument("name", StringArgumentType.word()).build();
            ArgumentCommandNode<FabricClientCommandSource, Integer> startArgument = argument("startPeriod", IntegerArgumentType.integer(0)).build();
            startArgument.addChild(argument("endPeriod", IntegerArgumentType.integer(1))
                    .executes(AddCommand::run).build());
            nameArgument.addChild(startArgument);
            addNode.addChild(nameArgument);

            dispatcher.getRoot().addChild(mainCommand);
            mainCommand.addChild(listNode);
            mainCommand.addChild(rcNode);
            mainCommand.addChild(tpNode);
            mainCommand.addChild(deleteNode);
            mainCommand.addChild(addNode);
        });
    }
}

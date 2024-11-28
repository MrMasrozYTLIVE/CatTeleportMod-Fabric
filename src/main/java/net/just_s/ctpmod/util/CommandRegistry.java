package net.just_s.ctpmod.util;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.just_s.ctpmod.commands.*;
import net.minecraft.command.CommandRegistryAccess;

import java.util.List;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class CommandRegistry {
    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) -> {
            var subs = generateSubcommands();

            registerCommand(literal("ctp").build(), dispatcher, subs);
            registerCommand(literal("cattp").build(), dispatcher, subs);
            registerCommand(literal("catteleport").build(), dispatcher, subs);
        });
    }

    private static void registerCommand(LiteralCommandNode<FabricClientCommandSource> mainCommand, CommandDispatcher<FabricClientCommandSource> dispatcher, List<LiteralCommandNode<FabricClientCommandSource>> subs) {
        dispatcher.getRoot().addChild(mainCommand);

        for(var node : subs) {
            mainCommand.addChild(node);
        }
    }

    private static List<LiteralCommandNode<FabricClientCommandSource>> generateSubcommands() {
        // List SubCommand
        var listNode = literal("list").executes(ListCommand::run).build();

        // RC SubCommand
        var rcNode = literal("rc").build();
        rcNode.addChild(argument("seconds", IntegerArgumentType.integer(1))
                .executes(RcCommand::run).build());

        // TP SubCommand
        var tpNode = literal("tp").build();
        tpNode.addChild(argument("name", StringArgumentType.word())
                .suggests(TpCommand::suggest).executes(TpCommand::run).build());

        // Delete SubCommand
        var deleteNode = literal("delete").build();
        deleteNode.addChild(argument("name", StringArgumentType.word())
                .suggests(DeleteCommand::suggest).executes(DeleteCommand::run).build());

        // Add SubCommand
        var addNode = literal("add").build();
        var nameArgument = argument("name", StringArgumentType.word()).build();
        var startArgument = argument("startPeriod", FloatArgumentType.floatArg(0)).build();
        startArgument.addChild(argument("endPeriod", FloatArgumentType.floatArg(1))
                .executes(AddCommand::run).build());
        nameArgument.addChild(startArgument);
        addNode.addChild(nameArgument);

        return List.of(listNode, rcNode, tpNode, deleteNode, addNode);
    }
}

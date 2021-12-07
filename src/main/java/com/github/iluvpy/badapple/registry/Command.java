package com.github.iluvpy.badapple.registry;

import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import static net.minecraft.server.command.CommandManager.literal;
import com.github.iluvpy.badapple.registry.BadAppleCommand;

public class Command {
    public static void registerCommand() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(literal("BadApple").executes(context -> {
                BadAppleCommand badAppleCommand = new BadAppleCommand();
                if (!BadAppleCommand.isAlreadyExecuting) {
                    badAppleCommand.start();
                }
                return 1;
            }));
        });
    }
}

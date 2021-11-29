package com.github.iluvpy.badapple;

import com.github.iluvpy.badapple.registry.Command;
import net.fabricmc.api.ModInitializer;


public class ModBadApple implements ModInitializer {

    @Override
    public void onInitialize() {
        Command.registerCommand();
    }
}

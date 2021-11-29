package com.github.iluvpy.badapple.registry;

import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Vector;

import static net.minecraft.server.command.CommandManager.literal;

class BadAppleCommand extends Thread {
    public static boolean isAlreadyExecuting = false;
    public static final float WIDTH = 10.0f;
    public static final float HEIGHT = 10.0f;
    public static final float PLAYER_DISTANCE = 30.0f;
    public Vector<BlockPos> placedBlock = new Vector<BlockPos>();

    @Override
    public void run() {
        BadAppleCommand.isAlreadyExecuting = true;

        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        ServerWorld world = client.getServer().getWorld(client.world.getRegistryKey());

        player.setYaw(-90.0f); // look at x direction
        player.setPos(0.0f, 100.0f, 0.0f);
        player.sendMessage(new LiteralText("BadApple Started"), true);
        player.sendMessage(new LiteralText(System.getProperty("user.dir")), false);
        Vec3d playerPos = player.getPos();
        setBlock(world, new BlockPos(playerPos.x, playerPos.y-1.0f, playerPos.z), Blocks.BEDROCK); // add a block where the player can stand on

        for (int i = 0; i < 20; i++) {
            for (float y = -HEIGHT; y < HEIGHT; y++) {
                for (float z = -WIDTH; z < WIDTH; z++) {
                    if (Math.random() > 0.5) {
                        setBlock(world, new BlockPos(playerPos.x+PLAYER_DISTANCE, playerPos.y+y, playerPos.z+z), Blocks.WHITE_WOOL);
                    } else {
                        setBlock(world, new BlockPos(playerPos.x+PLAYER_DISTANCE, playerPos.y+y, playerPos.z+z), Blocks.BLACK_WOOL);
                    }
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        cleanPlacedBlocks(world);
        player.sendMessage(new LiteralText("BadApple Finished"), true);
        BadAppleCommand.isAlreadyExecuting = false;
    }

    public void setBlock(ServerWorld world, BlockPos pos, Block block) {
        world.setBlockState(pos, block.getDefaultState());
        if (!placedBlock.contains(pos)) { // avoid adding the same block pos twice
            placedBlock.add(pos);
        }
    }

    public void cleanPlacedBlocks(ServerWorld world) {
        for (BlockPos pos : placedBlock) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
        }
        placedBlock.clear();
    }
}

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

package com.github.iluvpy.badapple.registry;

import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Vector;

public class BadAppleCommand extends Thread {
    public static boolean isAlreadyExecuting = false;
    public static final float PLAYER_DISTANCE = 60.0f;
    public Vector<BlockPos> placedBlock = new Vector<BlockPos>();

    @Override
    public void run() {
        isAlreadyExecuting = true;
        BadAppleFolder badAppleFolder = new BadAppleFolder();
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        ServerWorld world = client.getServer().getWorld(client.world.getRegistryKey());
        assert player != null;
        assert world != null;
        if (badAppleFolder.exists()) {
            player.sendMessage(new LiteralText("Loading video data..."), false);
            String videoData = badAppleFolder.read();
            player.sendMessage(new LiteralText("finished reading video data file"), false);
            if (videoData == BadAppleFolder.READING_ERROR) {
                player.sendMessage(new LiteralText("Error occurred while reading video data file!"), false);
            } else if (videoData == BadAppleFolder.NO_FILE_ERR) {
                player.sendMessage(new LiteralText("Error: no file was found inside BadApple folder"), false);
            } else {

                String[] allData = videoData.split("d");
                final int WIDTH = parseInt(allData[0]);
                final int HEIGHT = parseInt(allData[1]);
                String pixelData = allData[2];
                String[] frames = pixelData.split("e");
                Vector<Vector<Vector<Boolean>>> allPixels = new Vector<>();
                for (String frame : frames) {
                    char[] pixels = frame.toCharArray();
                    Vector<Vector<Boolean>> pixelFrame = new Vector<>();
                    for (int i = 0; i < HEIGHT; i++) {
                        Vector<Boolean> frameLayer = new Vector<>();
                        for (int j = 0; j < WIDTH; j++) {
                            frameLayer.add(parseChar(pixels[i*WIDTH+j]));
                        }
                        pixelFrame.add(frameLayer);
                    }
                    allPixels.add(pixelFrame);
                }
                player.sendMessage(new LiteralText("Finished loading video data"), false);
                player.setPitch(90.0f); // look down
                player.setPos(WIDTH/2.0f, PLAYER_DISTANCE, HEIGHT/2.0f);
                player.sendMessage(new LiteralText("BadApple Started"), false);
                player.sendMessage(new LiteralText("width: " + WIDTH), false);
                player.sendMessage(new LiteralText("height: " + HEIGHT), false);
                Vec3d playerPos = player.getPos();
                int frameIndex = 1;
                int x = 0;
                int z = 0;
                for (Vector<Vector<Boolean>> frame : allPixels) {
                    if (!BadAppleCommand.isAlreadyExecuting) {break;}
                    for (Vector<Boolean> layer : frame) {
                        for (boolean pixel : layer) {
                            BlockPos pos = new BlockPos(x, 10.0f, z);
                            if (pixel) {
                                setBlock(world, pos, Blocks.WHITE_WOOL);
                            } else {
                                setBlock(world, pos, Blocks.BLACK_WOOL);
                            }
                            x += 1;
                        }
                        x = 0;
                        z += 1;
                    }
                    try {
                        sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    player.sendMessage(new LiteralText("frame " + frameIndex), true);
                    frameIndex += 1;
                    z = 0;
                }

                cleanPlacedBlocks(world);
                player.sendMessage(new LiteralText("BadApple Finished"), false);
            }
        } else {
            player.sendMessage(new LiteralText("Error while executing BadApple command; Couldnt open BadApple folder"), false);
        }

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

    public int parseInt(String str) {
        String actualNumber = "";
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (Character.isDigit(ch)) {
                actualNumber += ch;
            } else {
                break;
            }
        }
        return Integer.parseInt(actualNumber);
    }

    public boolean parseChar(char ch) {
        if (ch >= 48 && ch <= 57) { // if its a digit
            ch -= 48;
            return ch >= 1;
        } else {
            return false;
        }
    }
}
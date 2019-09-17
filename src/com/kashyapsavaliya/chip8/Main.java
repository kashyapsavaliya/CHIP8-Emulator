package com.kashyapsavaliya.chip8;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Chip8 chip8 = new Chip8();
        Keyboard keyboard = new Keyboard();
        Frame frame = new Frame(chip8);

        chip8.initialize();
        chip8.loadGame();

        while (true) { // Start
            // Emulate one cycle
            chip8.emulateCycle();

            // If the draw flag is set, update the screen
            if (chip8.drawFlag) {
                frame.repaint();
                chip8.drawFlag = false;
            }

            // Store key press state (Press and Release)
            keyboard.setKeys();

        }

    }

    private static void drawGraphics() {

    }

}

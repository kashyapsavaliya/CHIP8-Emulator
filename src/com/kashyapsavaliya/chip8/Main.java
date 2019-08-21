package com.kashyapsavaliya.chip8;

public class Main {

    public static void main(String[] args) {
        Chip8 chip8 = new Chip8();
        Keyboard keyboard = new Keyboard();

        chip8.loadGame();

        while (true) { // Start
            // Emulate one cycle
            chip8.emulateCycle();

            // If the draw flag is set, update the screen
            if (chip8.drawFlag) {
                drawGraphics();
            }

            // Store key press state (Press and Release)
            keyboard.setKeys();

        }

    }

    private static void drawGraphics() {

    }

}

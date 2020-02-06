package com.kashyapsavaliya.chip8;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) throws IOException {
        Chip8 chip8 = new Chip8();
        Keyboard keyboard = new Keyboard();
        Panel panel = new Panel(chip8);

        chip8.initialize();
        chip8.loadGame();

        while (true) { // Start
            // Emulate one cycle
            chip8.fetch();
            chip8.emulateCycle();

            // If the draw flag is set, update the screen
            if (chip8.drawFlag) {
                panel.repaint();
                chip8.drawFlag = false;
            }

            // Store key press state (Press and Release)
            keyboard.setKeys();

            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

        }

    }

}

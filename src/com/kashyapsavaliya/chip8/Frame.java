package com.kashyapsavaliya.chip8;

import javax.swing.JFrame;

public class Frame extends JFrame {

    private Panel screen;

    public Frame(Chip8 chip8) {
        JFrame f = new JFrame("CHIP-8");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        screen = new Panel(chip8);
        f.add(screen);
        f.pack();
        f.setVisible(true);
    }

}
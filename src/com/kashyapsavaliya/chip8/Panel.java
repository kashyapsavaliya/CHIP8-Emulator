package com.kashyapsavaliya.chip8;

import javax.swing.*;
import java.awt.*;

class Panel extends JPanel {

    private Chip8 chip8;

    public Panel(Chip8 chip8) {
        this.chip8 = chip8;
    }

    public void paint(Graphics g) {
        char[] gfx = chip8.getGfx();
        for (int i = 0; i < gfx.length; i++) {
            if (gfx[i] == 0) {
                g.setColor(Color.BLACK);
            } else {
                g.setColor(Color.GREEN);
            }
            int x = (i % 64);
            int y = (int) Math.floor(i / 64);

            g.fillRect(x * 10, y * 10, 10, 10);
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(640, 320);
    }

}
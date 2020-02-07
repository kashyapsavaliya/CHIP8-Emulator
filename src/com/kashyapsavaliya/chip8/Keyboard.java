package com.kashyapsavaliya.chip8;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

public class Keyboard implements KeyListener {

    private HashMap<Character, Integer> key;
    private static int[] keyBuffer;

    public Keyboard() {
        key = new HashMap<>();
        keyBuffer = new int[16];
    }

    public void setKeys() {
        key.put('1', 1);
        key.put('2', 2);
        key.put('3', 3);
        key.put('q', 4);
        key.put('w', 5);
        key.put('e', 6);
        key.put('a', 7);
        key.put('s', 8);
        key.put('d', 9);
        key.put('x', 0);
        key.put('z', 0xA);
        key.put('c', 0xB);
        key.put('4', 0xC);
        key.put('r', 0xD);
        key.put('f', 0xE);
        key.put('v', 0xF);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (key.get(e.getKeyChar()) != -1) {
            keyBuffer[key.get(e.getKeyChar())] = 1;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (key.get(e.getKeyChar()) != -1) {
            keyBuffer[key.get(e.getKeyChar())] = 0;
        }
    }

    public int[] getKeyBuffer() {
        return keyBuffer;
    }

}

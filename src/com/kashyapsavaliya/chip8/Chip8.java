package com.kashyapsavaliya.chip8;

import java.io.FileInputStream;
import java.io.IOException;

public class Chip8 {

    private static final int MEMORY = 4090;
    private static final int REGISTER_SIZE = 16;
    private static final int STACK_SIZE = 16;
    private static final int KEY_SIZE = 16;
    private static final int GFX_SIZE = 64 * 32;

    private static char[] chip8_fontset = {
            0xF0, 0x90, 0x90, 0x90, 0xF0, // 0
            0x20, 0x60, 0x20, 0x20, 0x70, // 1
            0xF0, 0x10, 0xF0, 0x80, 0xF0, // 2
            0xF0, 0x10, 0xF0, 0x10, 0xF0, // 3
            0x90, 0x90, 0xF0, 0x10, 0x10, // 4
            0xF0, 0x80, 0xF0, 0x10, 0xF0, // 5
            0xF0, 0x80, 0xF0, 0x90, 0xF0, // 6
            0xF0, 0x10, 0x20, 0x40, 0x40, // 7
            0xF0, 0x90, 0xF0, 0x90, 0xF0, // 8
            0xF0, 0x90, 0xF0, 0x10, 0xF0, // 9
            0xF0, 0x90, 0xF0, 0x90, 0x90, // A
            0xE0, 0x90, 0xE0, 0x90, 0xE0, // B
            0xF0, 0x80, 0x80, 0x80, 0xF0, // C
            0xE0, 0x90, 0x90, 0x90, 0xE0, // D
            0xF0, 0x80, 0xF0, 0x80, 0xF0, // E
            0xF0, 0x80, 0xF0, 0x80, 0x80  // F
    };

    private short opcode;
    private char[] memory;
    private char[] V;
    private short I;
    private short pc;
    private char[] gfx;
    private char delay_timer;
    private char sound_timer;
    private short[] stack;
    private short sp;
    private char[] key;

    public boolean drawFlag;

    public Chip8() {
        initialize();
    }

    private void initialize() {
        // Initialize registers and memory once
        pc = 0x200; // Program counter starts at 0x200
        opcode = 0; // Reset current opcode
        I = 0; // Reset index register
        sp = 0; // Reset stack pointer

        // Reset timers
        delay_timer = 0;
        sound_timer = 0;

        // Clear display
        for (int i = 0; i < GFX_SIZE; i++) {
            gfx[i] = 0;
        }

        // Clear stack
        for (int i = 0; i < STACK_SIZE; i++) {
            stack[i] = 0;
        }

        // Clear registers V0-VF
        for (int i = 0; i < REGISTER_SIZE; i++) {
            V[i] = 0;
        }

        // Clear memory
        for (int i = 0; i < MEMORY; i++) {
            memory[i] = 0;
        }

        // Clear keys
        for (int i = 0; i < KEY_SIZE; i++) {
            key[i] = 0;
        }

        // Load fontset
        for (int i = 0; i < 80; i++) {
            memory[i] = chip8_fontset[i];
        }

    }

    public void loadGame() {
        try {
            FileInputStream file = new FileInputStream("Roms/IBM");
            int bufferSize = file.available();
            byte[] buffer = new byte[bufferSize];
            for (int i = 0; i < bufferSize; i++) {
                memory[i + 512] = (char) buffer[i];
            }
            System.out.println("Rom loaded");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void emulateCycle() {
        // Fetch Opcode
        opcode = (short) (memory[pc] << 8 | memory[pc + 1]);

        // Decode Opcode
        switch (opcode & 0xF000) { // ANNN: Sets I to the address NNN
            // Execute Opcode
            case 0xA000:
                I = (short) (opcode & 0xFFFF);
                pc += 2;
                break;

            default:
                System.out.println("Unknown opcode: " + opcode);
        }

        // Update timers
        if (delay_timer > 0) {
            --delay_timer;
        }

        if (sound_timer > 0) {
            if (sound_timer == 1) {
                System.out.println("BEEP!\n");
                // Sound
            }
            --sound_timer;
        }
    }

}

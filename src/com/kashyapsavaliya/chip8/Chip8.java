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

        // 4-bit register identifier
        short X = (short) ((opcode & 0x0F00) >> 8);
        short Y = (short) ((opcode & 0x00F0) >> 4);

        // Decode Opcode
        switch (opcode & 0xF000) { // ANNN: Sets I to the address NNN
            // Execute Opcode
            case 0x0000:
                switch (opcode & 0x000F) {
                    case 0x0000: // 0x00E0: Clears the screen
                        for (int i = 0; i < GFX_SIZE; i++) {
                            gfx[i] = 0;
                        }
                        pc += 2;
                        break;

                    case 0x000E: // 0x00EE: Returns from subroutine
                        pc = stack[--sp];
                        pc += 2;
                        break;

                    default:
                        System.out.println("Unknown opcode [0x0000]: " + opcode);
                }
                break;

            case 0x0001: // Jumps to address NNN
                pc = (short) (opcode & 0x0FFF);
                break;

            case 0x0002: // Calls subroutine at NNN
                stack[sp] = pc;
                ++sp;
                pc = (short) (opcode & 0x0FFF);
                break;

            case 0x0003: // Skips the next instruction if VX = NN
                if (V[X] == (opcode & 0x00FF)) {
                    pc += 2;
                }
                break;

            case 0x0004: // Skips the next instruction if VX != NN
                if (V[X] != (opcode & 0x00FF)) {
                    pc += 2;
                }
                break;

            case 0x0005: // Skips the next instruction if VX = VY
                if (V[X] == V[Y]) {
                    pc += 2;
                }
                break;

            case 0x0006: // Sets VX to NN
                V[X] = (char) (opcode & 0x00FF);
                break;

            case 0x0007: // Adds NN to VX
                V[X] += (char) (opcode & 0x00FF);

            case 0x0008:
                switch (opcode & 0x000F) {
                    case 0x0000: // Sets VX to the value VY
                        V[X] = V[Y];
                        break;

                    case 0x0001: // Sets VX to VX or VY
                        V[X] = (char) (V[X] | V[Y]);
                        break;

                    case 0x0002: // Sets VX to VX and VY
                        V[X] = (char) (V[X] & V[Y]);
                        break;

                    case 0x0003: // Sets VX to VX xor VY
                        V[X] = (char) (V[X] ^ V[Y]);
                        break;

                    case 0x0004: // Adds VY to VX. VF is set to 1 when there's a carry and 0 if not
                        if (V[Y] > (0xFF - V[X])) {
                            V[0xF] = 1;
                        } else {
                            V[0xF] = 0;
                        }
                        V[X] += V[Y];
                        pc += 2;
                        break;

                    case 0x0005: // VY is subtracted from VX. VF is set to 0 when there's a borrow and 1 if not

                    case 0x0006: // Stores the least significant bit of VX in VF. Shifts VX to the right by 1

                    case 0x0007: // Sets VX to VY minus VX. VF is set to 0 when there's a borrow and 1 if not

                    case 0x000E: // Stores the most significant bit of VX in VF. Shifts VX to the left by 1

                    default:
                        System.out.println("Unknown opcode [0x0008]: " + opcode);
                }
                break;

            case 0x0009: // Skips the next instruction if VX != VY
                if (V[X] != V[Y]) {
                    pc += 2;
                }
                break;

            case 0xA000: // Sets I to the address NNN
                I = (short) (opcode & 0xFFFF);
                pc += 2;
                break;

            case 0xB000: // Jumps to the address NNN plus V0
                pc = (short) ((opcode & 0x0FFF) + V[0]);

            case 0xC000: // Sets VX to the result of a bitwise and operation on a random number and NN

            case 0xD000: // Draws a sprite at coordinate (VX, VY)
            {
                short x = (short) V[X];
                short y = (short) V[Y];
                short height = (short) (opcode & 0x000F);
                short pixel;

                V[0xF] = 0;
                for (int yline = 0; yline < height; yline++) {
                    pixel = (short) memory[I + yline];
                    for (int xline = 0; xline < 8; xline++) {
                        if ((pixel & (0x80 >> xline)) != 0) {
                            if (gfx[(x + xline + ((y + yline) * 64))] == 1) {
                                V[0xF] = 1;
                            }
                            gfx[x + xline + ((y + yline) * 64)] ^= 1;
                        }
                    }
                }
                drawFlag = true;
                pc += 2;
            }
            break;

            case 0xE000:
                switch (opcode & 0x000F) {
                    case 0x009E: // Skips the next instruction if the key stored in VX is pressed
                        if (key[V[X]] != 0) {
                            pc += 4;
                        } else {
                            pc += 2;
                        }
                        break;

                    case 0x00A1: // Skips the next instruction if the key stored in VX isn't pressed

                    default:
                        System.out.println("Unknown opcode [0xE000]: " + opcode);
                }
                break;

            case 0xF000:
                switch (opcode & 0x000F) {
                    case 0x0007: // Sets VX to the value of the delay timer
                        V[X] = delay_timer;
                        break;

                    case 0x000A: // A key press is awaited, and then stored in VX

                    case 0x0015: // Sets the delay timer to VX
                        delay_timer = V[X];
                        break;

                    case 0x0018: // Sets the sound timer to VX
                        sound_timer = V[X];
                        break;

                    case 0x001E: // Adds VX to I
                        I += V[X];
                        break;

                    case 0x0029: // Sets I to the location of the sprite for the character in VX

                    case 0x0033: // Stores the binary-coded decimal representation of VX
                        memory[I] = (char) (V[X] / 100);
                        memory[I + 1] = (char) ((V[X] / 10) % 10);
                        memory[I + 2] = (char) ((V[X] % 100) % 10);
                        pc += 2;
                        break;

                    case 0x0055: // Stores V0 to VX (including VX) in memory starting at address I

                    case 0x0065: // Fills V0 to VX (including VX) with values from memory starting at address I

                    default:
                        System.out.println("Unknown opcode [0xF000]: " + opcode);
                }
                break;

            default:
                System.out.println("Unknown opcode: " + opcode);
                break;
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

package com.kashyapsavaliya.chip8;

public class Cpu {

    private static final int MEMORY = 4090;
    private static final int REGISTER_SIZE = 16;
    private static final int STACK_SIZE = 16;
    private static final int KEY_SIZE = 16;

    private short opcode;
    private char[] memory = new char[MEMORY];
    private char[] V = new char[REGISTER_SIZE];
    private short I;
    private short pc;
    private char[] gfx = new char[64 * 32];
    private char delay_timer;
    private char sound_timer;
    private short[] stack = new short[STACK_SIZE];
    private short sp;
    private char[] key = new char[KEY_SIZE];

}

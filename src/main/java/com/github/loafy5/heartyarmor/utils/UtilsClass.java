package com.github.loafy5.heartyarmor.utils;

public class UtilsClass {

    public static float toSeconds(int ticks) {
        return (float)ticks / 20;
    }
    public static int toTicks(float seconds) {
        return (int)(seconds * 20);
    }
}

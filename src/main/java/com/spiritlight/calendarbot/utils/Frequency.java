package com.spiritlight.calendarbot.utils;

public enum Frequency {
    ONCE,
    EVERY;

    public static Frequency fromString(String in) {
        for(Frequency f : values()) {
            if(f.name().equalsIgnoreCase(in)) return f;
        }
        return null;
    }
}

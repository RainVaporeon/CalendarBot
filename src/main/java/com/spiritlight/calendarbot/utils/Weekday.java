package com.spiritlight.calendarbot.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

public enum Weekday {
    MONDAY(2),
    TUESDAY(3),
    WEDNESDAY(4),
    THURSDAY(5),
    FRIDAY(6),
    SATURDAY(7),
    SUNDAY(1);

    private final int value;

    Weekday(int value) {
        this.value = value;
    }

    public static @Nullable Weekday fromString(String in) {
        for(Weekday name : values()) {
            if(name.name().equalsIgnoreCase(in)) return name;
        }
        return null;
    }

    @Contract(pure = true)
    public static @Nullable Weekday fromValue(int i) {
        for(Weekday day : values()) {
            if(day.value == i) return day;
        }
        return null;
    }

    @Range(from = 0, to = 6)
    public int getValue() {
        return value;
    }

    @Range(from = 1, to = 7)
    public int getValueLiteral() {
        return this == SUNDAY ? 7 : value;
    }

    @Override
    public String toString() {
        return this.name();
    }
}

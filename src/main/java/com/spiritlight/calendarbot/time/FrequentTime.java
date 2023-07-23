package com.spiritlight.calendarbot.time;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.spiritlight.calendarbot.utils.Frequency;
import com.spiritlight.calendarbot.utils.Weekday;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.Objects;

public class FrequentTime extends Time {

    private final Frequency frequency;
    private final Weekday day;
    private final int hour;
    private final int minute;
    private final int second;

    private FrequentTime() {
        this.frequency = Frequency.ONCE;
        this.day = Weekday.MONDAY;
        this.hour = 0;
        this.minute = 0;
        this.second = Integer.MAX_VALUE;
    }

    public FrequentTime(Frequency frequency, Weekday day, int hour, int minute, int second) {
        this.frequency = frequency;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    @Contract("-> null")
    @Nullable
    @Override
    public Date getDate() {
        return null;
    }

    @Override
    public Frequency frequency() {
        return frequency;
    }

    @Override
    public Weekday getWeekday() {
        return day;
    }

    @Override
    public int getHour() {
        return hour;
    }

    @Override
    public int getMinute() {
        return minute;
    }

    @Override
    public int getSecond() {
        return second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FrequentTime that = (FrequentTime) o;
        return hour == that.hour && minute == that.minute && second == that.second && frequency == that.frequency && day == that.day;
    }

    @Override
    public int hashCode() {
        return Objects.hash(frequency, day, hour, minute, second);
    }
}

package com.spiritlight.calendarbot.time;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.spiritlight.calendarbot.utils.Frequency;
import com.spiritlight.calendarbot.utils.Weekday;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

/**
 * @apiNote Note: this class can have a natural ordering that is inconsistent with equals.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DateTime.class, name = "dateTime"),
        @JsonSubTypes.Type(value = FrequentTime.class, name = "freqTime")
})
public abstract class Time implements Comparable<Time> {

    protected Time() {}

    @Nullable
    public abstract Date getDate();

    public abstract Frequency frequency();

    public abstract Weekday getWeekday();

    public abstract int getHour();

    public abstract int getMinute();

    public abstract int getSecond();

    public static Time of(long time) {
        return new DateTime(new Date(time));
    }

    public static Time fromInstant(Instant instant) {
        return new DateTime(new Date(instant.toEpochMilli()));
    }

    public static Time future(long time) {
        return new DateTime(new Date(System.currentTimeMillis() + time));
    }

    public static Time past(long time) {
        return new DateTime(new Date(System.currentTimeMillis() - time));
    }

    public static Time now() {
        return new DateTime();
    }

    public static Time at(Date date) {
        return new DateTime(date);
    }

    public static Time every(Weekday weekday, int hour, int minute, int second) {
        return new FrequentTime(Frequency.EVERY, weekday, hour, minute, second);
    }

    public static Time once(Weekday day, int hour, int minute, int second) {
        return new FrequentTime(Frequency.ONCE, day, hour, minute, second);
    }

    /**
     * Compares this time with that one
     * @param that The time to compare with
     * @return true if this time is earlier than that, false otherwise
     */
    public boolean before(Time that) {
        return this.compareTo(that) < 0;
    }

    /**
     * Compares this time with that one
     * @param that The time to compare with
     * @return true if this time is later than that, false otherwise
     */
    public boolean after(Time that) {
        return this.compareTo(that) > 0;
    }

    @Override
    public abstract boolean equals(Object o);

    @Override
    public int compareTo(@NotNull Time that) {
        if(this.getDate() != null && that.getDate() != null) {
            return this.getDate().compareTo(that.getDate());
        }
        int dDiff = (this.getWeekday().getValueLiteral() - that.getWeekday().getValueLiteral()) * 24 * 60 * 60;
        int hDiff = (this.getHour() - that.getHour()) * 60 * 60;
        int mDiff = (this.getMinute() - that.getMinute()) * 60;
        int sDiff = this.getSecond() - that.getSecond();
        return dDiff + hDiff + mDiff + sDiff;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if(this.getDate() == null) {
            builder.append(this.frequency()).append(' ');
            builder.append(this.getWeekday());
        } else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            builder.append(format.format(this.getDate())).append(" ").append(this.getWeekday());
        }
        builder.append(" at ");
        builder.append(this.getHour()).append(':').append(this.getMinute()).append(':').append(this.getSecond());
        return builder.toString();
    }
}

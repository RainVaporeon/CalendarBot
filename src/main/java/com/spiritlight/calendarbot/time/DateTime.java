package com.spiritlight.calendarbot.time;

import com.spiritlight.calendarbot.utils.Frequency;
import com.spiritlight.calendarbot.utils.Weekday;
import org.jetbrains.annotations.Nullable;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class DateTime extends Time {

    private final Date date;

    private final Calendar cal;

    public DateTime() {
        this(new Date());
    }

    public DateTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        this.date = date;
        this.cal = cal;
    }

    @Nullable
    @Override
    public Date getDate() {
        return this.date;
    }

    @Override
    public Frequency frequency() {
        return Frequency.ONCE;
    }

    @Override
    public Weekday getWeekday() {
        return Weekday.fromValue(cal.get(Calendar.DAY_OF_WEEK));
    }

    @Override
    public int getHour() {
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    @Override
    public int getMinute() {
        return cal.get(Calendar.MINUTE);
    }

    @Override
    public int getSecond() {
        return cal.get(Calendar.SECOND);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateTime dateTime = (DateTime) o;
        return Objects.equals(date, dateTime.date) && Objects.equals(cal, dateTime.cal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, cal);
    }
}

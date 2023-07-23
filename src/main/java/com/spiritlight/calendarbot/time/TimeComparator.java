package com.spiritlight.calendarbot.time;

import com.spiritlight.fishutils.math.Numbers;

import java.util.Comparator;

public class TimeComparator {

    /**
     *
     * @return A comparator that follows the natural order of the given time.
     */
    public static Comparator<Time> earliest() {
        return Comparator.naturalOrder();
    }

    /**
     * Takes a {@link Time} and compares the time, having the closest time
     * with an index of 0, otherwise lower. The comparator never returns a value
     * larger than 0, and the comparator returns 0 if and only if the {@link java.util.Date}
     * contained have equal time, or if the weekday, hour, minute and second matches.
     * @return A comparator in which, if the time is further, the index is lower.
     * The index is never above zero,
     */
    public static Comparator<Time> closest(Time time) {
        return Comparator.comparingInt(t -> differenceIndex(t, time));
    }

    private static int differenceIndex(Time t1, Time t2) {
        if (t1.getDate() != null && t2.getDate() != null) {
            return (int) -Numbers.clamp(Math.abs(t1.getDate().getTime() - t2.getDate().getTime()), 0, Integer.MAX_VALUE);
        }
        int dDiff = Math.abs(t1.getWeekday().getValueLiteral() - t2.getWeekday().getValueLiteral()) * 24 * 60 * 60;
        int hDiff = Math.abs(t1.getHour() - t2.getHour()) * 60 * 60;
        int mDiff = Math.abs(t1.getMinute() - t2.getMinute()) * 60;
        int sDiff = Math.abs(t1.getSecond() - t2.getSecond());
        return -(dDiff + hDiff + mDiff + sDiff);
    }

    /**
     *
     * @return A comparator that is in reverse order.
     */
    public static Comparator<Time> latest() {
        return Comparator.reverseOrder();
    }
}

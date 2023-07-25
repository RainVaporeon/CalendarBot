package com.spiritlight.calendarbot.test;

import com.spiritlight.calendarbot.time.Time;
import com.spiritlight.calendarbot.utils.Weekday;

public class Test {

    public static void main(String[] args) {
        Time time = Time.once(Weekday.SUNDAY, 12, 0, 50);
        Time later = Time.once(Weekday.SUNDAY, 18, 30, 0);
        Time earlier = Time.once(Weekday.SUNDAY, 6, 30, 0);

        System.out.printf("""
                OUT:
                time vs later: before=%s
                time vs earlier: before=%s
                """,
                time.before(later),
                time.before(earlier));
    }

}

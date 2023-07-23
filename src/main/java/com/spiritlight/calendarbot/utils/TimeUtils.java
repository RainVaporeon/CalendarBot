package com.spiritlight.calendarbot.utils;

import com.spiritlight.calendarbot.time.Time;
import com.spiritlight.calendarbot.time.TimeComparator;
import com.spiritlight.calendarbot.time.TimeEvent;

import java.util.List;

public class TimeUtils {

    /**
     * Finds the time closest to the provided time
     * @param events A list of events to fetch
     * @param time The time as a center point
     * @return a TimeEvent that is closest, or null if none is found
     */
    public static TimeEvent getClosestTime(List<TimeEvent> events, Time time) {
        Time t = events.stream().filter(timeEvent -> !timeEvent.isPast())
                .map(TimeEvent::time).max(TimeComparator.closest(time)).orElse(null);
        if(t == null) return null;
        return events.stream().filter(event -> event.time().equals(t)).findFirst().orElse(null);
    }

}

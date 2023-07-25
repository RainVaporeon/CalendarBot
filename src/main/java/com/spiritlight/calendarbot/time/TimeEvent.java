package com.spiritlight.calendarbot.time;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.spiritlight.calendarbot.Main;
import com.spiritlight.calendarbot.utils.Frequency;

import java.util.Date;
import java.util.Objects;

public final class TimeEvent {

    @JsonProperty
    private final String title;
    @JsonProperty
    private final String description;
    @JsonProperty
    private final Time time;
    @JsonProperty
    private final int id;

    @JsonProperty
    private Date lastFiredAt;

    private TimeEvent() {
        this.title = null;
        this.description = null;
        this.time = null;
        this.id = Integer.MIN_VALUE;
        this.lastFiredAt = null;
    }

    private TimeEvent(String title, String description, Time time, int id) {
        this.title = title;
        this.description = description;
        this.time = time;
        this.id = id;
        this.lastFiredAt = null;
    }

    public TimeEvent(String title, String description, Time time) {
        this(title, description, time, Main.database.next());
    }

    public int getId() {
        return id;
    }

    /**
     * Returns if this event is already passed.
     * If this time has a frequency of {@link Frequency#EVERY}, it shows whether
     * the current time has passed this week's event. Otherwise, returns
     * whether the current time has passed this event.
     *
     * @return whether this event is past.
     */
    @JsonIgnore
    public boolean isPast() {
        return isPast(0);
    }

    /**
     * Returns if this event is already passed.
     * If this time has a frequency of {@link Frequency#EVERY}, it shows whether
     * the current time has passed this week's event. Otherwise, returns
     * whether the current time has passed this event.
     *
     * @param tolerance The tolerance value in milliseconds. This value is subtracted
     *                  from the {@code second} parameter from the current time such that
     *                  <pre>
     *                                   TimeEvent event = new TimeEvent("test", null, Time.of(1000)); <br>
     *                                   Time time = Time.of(1100); <br>
     *                                   <br>
     *                                   System.out.println(event.isPast(time, 200));
     *                                   </pre>
     *                  will print out {@code false} as the time (1000) is not larger than
     *                  (1100 - 200)
     * @return whether this event is past.
     */
    @JsonIgnore
    public boolean isPast(long tolerance) {
        return isPast(Time.now(), tolerance);
    }

    private static final int WEEK_TIME = 604800000;
    @JsonIgnore
    public boolean isPast(Time t, long tolerance) {
        if(this.lastFiredAt != null && (this.lastFiredAt.getTime() - WEEK_TIME - tolerance) < System.currentTimeMillis()) return false;
        return (time.compareTo(t) - tolerance / 1000) < 0;
    }

    public String title() {
        return title;
    }

    public String description() {
        return description;
    }

    public Time time() {
        return time;
    }

    public int id() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (TimeEvent) obj;
        return Objects.equals(this.title, that.title) &&
                Objects.equals(this.description, that.description) &&
                Objects.equals(this.time, that.time) &&
                this.id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, time, id);
    }

    @Override
    public String toString() {
        return "TimeEvent[" +
                "title=" + title + ", " +
                "description=" + description + ", " +
                "time=" + time + ", " +
                "id=" + id + ']';
    }

}

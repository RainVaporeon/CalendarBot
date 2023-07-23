package com.spiritlight.calendarbot.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.spiritlight.calendarbot.Main;
import com.spiritlight.calendarbot.time.Time;
import com.spiritlight.calendarbot.time.TimeEvent;
import com.spiritlight.calendarbot.utils.Frequency;
import com.spiritlight.calendarbot.utils.TimeUtils;
import com.spiritlight.fishutils.logging.ILogger;
import com.spiritlight.fishutils.logging.Logger;
import com.spiritlight.fishutils.logging.Loggers;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class GuildDetails {
    @JsonProperty
    private final long guildId;

    private transient TextChannel cachedTextChannel;

    @JsonProperty
    private long announcementChannel;

    @JsonProperty
    private final List<TimeEvent> events = new LinkedList<>();

    private GuildDetails() {
        this.guildId = 0L;
    }

    public GuildDetails(Guild guild, TextChannel channel) {
        this.guildId = guild.getIdLong();
        this.announcementChannel = channel.getIdLong();
        this.cachedTextChannel = channel;
    }

    public GuildDetails(long id, long announcementChannel) {
        this.guildId = id;
        this.announcementChannel = announcementChannel;
        this.cachedTextChannel = Main.bot.getJDA().getTextChannelById(announcementChannel);
    }

    public boolean updateChannel(TextChannel channel) {
        this.cachedTextChannel = channel;
        this.announcementChannel = channel.getIdLong();
        return this.cachedTextChannel != null;
    }

    public boolean updateChannel(long id) {
        this.cachedTextChannel = Main.bot.getJDA().getTextChannelById(id);
        this.announcementChannel = id;
        return this.cachedTextChannel != null;
    }

    public List<TimeEvent> getEvents() {
        return events;
    }

    @JsonIgnore
    public TimeEvent getClosestEvent() {
        return TimeUtils.getClosestTime(events, Time.now());
    }

    public int addEvent(TimeEvent event) {
        ILogger logger = Loggers.getThreadLogger();
        logger.info("Adding event " + event + " to guild " + this.guildId + ": Current time " + Time.now());
        this.events.add(event);
        return event.getId();
    }

    public long getGuildId() {
        return guildId;
    }

    public List<TimeEvent> cancelEvents(Time time) {
        List<TimeEvent> collect = new LinkedList<>();
        events.forEach(t -> {
            if(t.time().equals(time)) {
                events.remove(t);
                collect.add(t);
            }
        });
        return collect;
    }

    public void onUpdate() {
        final Logger logger = Loggers.getThreadLogger();
        events.forEach(event -> {
            if(!event.isPast()) return; // no-op

            if(this.cachedTextChannel == null) {
                logger.warn("Could not dispatch a message to " + this.announcementChannel + " in guild " + guildId);
                return;
            }

            this.cachedTextChannel.sendMessage(event.title()).addEmbeds(getEmbed(event)).queue(m -> {
                logger.success("Successfully dispatched event to " + this.guildId);
            });
            if(event.time().frequency() != Frequency.EVERY)
                events.remove(event);
        });
    }

    public void clearPassedEvents() {
        int size = events.size();
        this.events.removeIf(TimeEvent::isPast);
        int other = events.size();
        Loggers.getThreadLogger().success("Guild <" + guildId + ">: Removed " + (size - other) + " passed entries");
    }

    private static MessageEmbed getEmbed(TimeEvent event) {
        return new EmbedBuilder()
                .setTitle("Calendar")
                .setDescription(event.description())
                .setFooter("Calendar set for " + event.time() + " | ID: " + event.getId())
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GuildDetails that = (GuildDetails) o;
        return guildId == that.guildId && announcementChannel == that.announcementChannel;
    }

    @Override
    public int hashCode() {
        return Objects.hash(guildId, announcementChannel);
    }
}

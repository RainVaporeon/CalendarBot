package com.spiritlight.calendarbot.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spiritlight.calendarbot.config.Configuration;
import com.spiritlight.calendarbot.time.TimeEvent;
import com.spiritlight.fishutils.action.ActionResult;
import com.spiritlight.fishutils.logging.Loggers;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.Contract;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Database extends Configuration {
    private static final File SAVE = new File("data.json");

    @JsonProperty("_comment")
    private final String comment = "This file is for internal data only, do not modify manually!";

    @JsonProperty("nextId")
    private int next = 0;

    @JsonProperty
    private final List<GuildDetails> details = new CopyOnWriteArrayList<>();

    public Database() {

    }

    public List<GuildDetails> getDetails() {
        return details;
    }

    public boolean addDetail(GuildDetails details) {
        if(this.details.contains(details)) return false;
        return this.details.add(details);
    }

    public int next() {
        return next++;
    }

    @Contract("null -> null")
    public GuildDetails getDetail(Guild guild) {
        if(guild == null) return null;
        return getDetail(guild.getIdLong());
    }

    public GuildDetails getDetail(long guild) {
        return this.details.stream().filter(g -> g.getGuildId() == guild).findFirst().orElse(null);
    }

    public boolean guildRegistered(Guild guild) {
        return this.getDetail(guild) != null;
    }

    public void cleanup() {
        Loggers.getThreadLogger().info("Cleaning up passed events...");
        this.details.forEach(GuildDetails::clearPassedEvents);
    }

    public void update() {
        this.details.forEach(GuildDetails::onUpdate);
    }

    @Override
    protected File file() {
        return SAVE;
    }
}

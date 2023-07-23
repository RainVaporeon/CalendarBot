package com.spiritlight.calendarbot.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.File;

public class BotConfig extends Configuration {
    private static final File SAVE = new File("config.json");

    @JsonProperty("bot_token")
    private final String token;

    public BotConfig() {
        this.token = "No token set, please set it in config.json";
    }

    public String getToken() {
        return token;
    }

    @Override
    protected File file() {
        return SAVE;
    }
}

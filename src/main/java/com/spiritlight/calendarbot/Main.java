package com.spiritlight.calendarbot;

import com.spiritlight.calendarbot.bot.Discord;
import com.spiritlight.calendarbot.bot.commands.CommandRegistrar;
import com.spiritlight.calendarbot.config.BotConfig;
import com.spiritlight.calendarbot.data.Database;
import com.spiritlight.fishutils.action.ActionResult;
import com.spiritlight.fishutils.action.Result;
import com.spiritlight.fishutils.collections.Pair;
import com.spiritlight.fishutils.logging.ILogger;
import com.spiritlight.fishutils.logging.Logger;
import com.spiritlight.fishutils.logging.Loggers;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static final Discord bot = new Discord();

    public static final File OUT;

    private static final Logger main;

    public static final BotConfig config = new BotConfig();

    public static final Database database = new Database();

    static {
        OUT = determineOutputFile();

        final File dir = new File("logs/");
        try {
            dir.mkdirs();
            OUT.createNewFile();
        } catch (IOException ex) {
            throw new ExceptionInInitializerError(ex);
        }

        Loggers.setDefault(OUT);
        main = Loggers.getThreadLogger();
    }

    public static void main(String[] args) throws InterruptedException {
        AnsiConsole.systemInstall();
        main.info("Logging started");

        main.info("deserializing configurations");
        config.deserialize().onSuccess(unused -> main.success("Configuration deserialized"))
                .onFail((result, throwable) -> {
                    main.error("Failed to load config: ", throwable);
                    main.warn("This is probably normal if this is your first time running the bot, or if the configuration structure changed.");
                }).throwUnchecked();
        main.info("deserializing database");
        database.deserialize().onSuccess(unused -> main.success("Database deserialized"))
                .onFail((result, throwable) -> {
                    main.error("Failed to load database: ", throwable);
                    main.warn("This is probably normal if this is your first time running the bot, or if the database structure changed.");
                }).throwUnchecked();
        database.cleanup();

        main.info("hooking shutdown threads...");
        Runtime.getRuntime().addShutdownHook(new Thread(config::serialize));
        Runtime.getRuntime().addShutdownHook(new Thread(database::serialize));

        main.info("preparing bot instance...");
        bot.create(config.getToken()).expect(InvalidTokenException.class, t -> {
            main.error("Invalid token! Have you set it up in config.json yet?");
            main.warn("Provided token: " + config.getToken());
        }).throwUnchecked();

        CommandRegistrar.register(bot.getJDA());

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(Main.database::update, 5, 1, TimeUnit.SECONDS);

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            ILogger logger = Loggers.getThreadLogger();
            logger.info("Saving...");
            database.serialize().onFail(((result, throwable) -> {
                logger.error("Failed to serialize: " + result, throwable);
            }));
            logger.success("Saved.");
        }, 1, 1, TimeUnit.MINUTES);

    }

    private static File determineOutputFile() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh_mm_ss");
        String base = "logs/" + formatter.format(new Date()) + ".log";
        File value = new File(base);
        if(!value.exists()) return value;
        int trace = 1;
        while(value.exists()) {
            value = new File(base + trace++);
        }

        return value;
    }
}
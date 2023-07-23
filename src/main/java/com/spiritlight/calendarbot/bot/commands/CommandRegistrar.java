package com.spiritlight.calendarbot.bot.commands;

import com.spiritlight.calendarbot.utils.Weekday;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class CommandRegistrar {

    public static void register(JDA api) {
        api.addEventListener(
                new AddScheduleCommand(),
                new RemoveScheduleCommand(),
                new SetupCommand()
        );

        api.updateCommands()
                .addCommands(
                        Commands.slash(AddScheduleCommand.NAME, "Adds a new schedule")
                                .addOptions(
                                        new OptionData(OptionType.STRING, "title", "Title", true),
                                        new OptionData(OptionType.STRING, "description", "Description", true),
                                        new OptionData(OptionType.STRING, "weekday", "The day of week", true)
                                                .addChoice("Monday", Weekday.MONDAY.toString())
                                                .addChoice("Tuesday", Weekday.TUESDAY.toString())
                                                .addChoice("Wednesday", Weekday.WEDNESDAY.toString())
                                                .addChoice("Thursday", Weekday.THURSDAY.toString())
                                                .addChoice("Friday", Weekday.FRIDAY.toString())
                                                .addChoice("Saturday", Weekday.SATURDAY.toString())
                                                .addChoice("Sunday", Weekday.SUNDAY.toString()),
                                        new OptionData(OptionType.INTEGER, "hour", "The hour (0-23)", false)
                                                .setRequiredRange(0, 23),
                                        new OptionData(OptionType.INTEGER, "minute", "The minute (0-59)", false)
                                                .setRequiredRange(0, 59),
                                        new OptionData(OptionType.INTEGER, "second", "The second (0-59)", false)
                                                .setRequiredRange(0, 59),
                                        new OptionData(OptionType.BOOLEAN, "repeat", "Whether this repeats", false)
                                ),
                        Commands.slash(RemoveScheduleCommand.NAME, "Removes a schedule by ID")
                                        .addOption(OptionType.INTEGER, "id", "Schedule ID"),
                        Commands.slash(SetupCommand.NAME, "Sets this channel as the broadcast channel")
                ).queue();
    }
}

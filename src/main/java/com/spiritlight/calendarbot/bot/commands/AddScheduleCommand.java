package com.spiritlight.calendarbot.bot.commands;

import com.spiritlight.calendarbot.Main;
import com.spiritlight.calendarbot.data.GuildDetails;
import com.spiritlight.calendarbot.time.Time;
import com.spiritlight.calendarbot.time.TimeEvent;
import com.spiritlight.calendarbot.utils.Weekday;
import com.spiritlight.fishutils.objects.ObjectUtils;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;

public class AddScheduleCommand extends ListenerAdapter {
    public static final String NAME = "addschedule";

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if(!NAME.equals(event.getName())) return;

        Weekday day = Weekday.fromString(event.getOption("weekday").getAsString());
        String title = event.getOption("title").getAsString();
        String desc = event.getOption("description").getAsString();
        int hour = ObjectUtils.evaluateOrDefault(event.getOption("hour"), 0, OptionMapping::getAsInt);
        int min = ObjectUtils.evaluateOrDefault(event.getOption("minute"), 0, OptionMapping::getAsInt);
        int sec = ObjectUtils.evaluateOrDefault(event.getOption("second"), 0, OptionMapping::getAsInt);
        boolean every = ObjectUtils.evaluateOrDefault(event.getOption("repeat"), false, OptionMapping::getAsBoolean);

        GuildDetails detail = Main.database.getDetail(event.getGuild());

        if(detail == null) {
            event.reply("This server hasn't been configured yet! Configure via /setup.\n" +
                    "If this is in DMs, note that this bot does not work in direct messages!").queue();
            return;
        }

        Time time = every ? Time.every(day, hour, min, sec) : Time.once(day, hour, min, sec);
        TimeEvent ev = new TimeEvent(title, desc, time);
        int id = detail.addEvent(ev);
        event.reply("Registered event! Event ID: " + id).setEphemeral(true).queue();
    }
}

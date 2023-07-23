package com.spiritlight.calendarbot.bot.commands;

import com.spiritlight.calendarbot.Main;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class RemoveScheduleCommand extends ListenerAdapter {

    public static final String NAME = "removeschedule";

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if(!NAME.equals(event.getName())) return;

        int id = event.getOption("id").getAsInt();

        if(!Main.database.guildRegistered(event.getGuild())) {
            event.reply("Please first set up this server by /setup.").setEphemeral(true).queue();
            return;
        }

        boolean b = Main.database.getDetail(event.getGuild()).getEvents().removeIf(ev -> ev.getId() == id);
        if(b) {
            event.reply("Successfully removed this event!").setEphemeral(true).queue();
        } else {
            event.reply("This event does not exist!").setEphemeral(true).queue();
        }
    }
}

package com.spiritlight.calendarbot.bot.commands;

import com.spiritlight.calendarbot.Main;
import com.spiritlight.calendarbot.data.GuildDetails;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class SetupCommand extends ListenerAdapter {
    public static final String NAME = "setup";

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if(!NAME.equals(event.getName())) return;

        if(event.getGuild() == null) {
            event.reply("This command is not supported in DMs.").setEphemeral(true).queue();
            return;
        }
        if(!(event.getChannel() instanceof TextChannel)) {
            event.reply("This is only available in a text channel!").setEphemeral(true).queue();
            return;
        }

        GuildDetails details = new GuildDetails(event.getGuild(), (TextChannel) event.getChannel());
        if(Main.database.addDetail(details)) {
            event.reply("Successfully set-up!").setEphemeral(true).queue();
        } else {
            Main.database.getDetail(event.getGuild()).updateChannel((TextChannel) event.getChannel());
            event.reply("This server has already been set-up! Updating it instead...").setEphemeral(true).queue();
        }
    }
}

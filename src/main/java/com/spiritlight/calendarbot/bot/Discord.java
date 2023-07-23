package com.spiritlight.calendarbot.bot;

import com.spiritlight.fishutils.action.ActionResult;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;

import java.lang.reflect.Field;

public class Discord {
    private final JDA jda;

    private final boolean created;

    public Discord() {
        this.jda = null;
        this.created = false;
    }

    public Discord(String token) throws InterruptedException {
        this.jda = JDABuilder.createDefault(token)
                .setAutoReconnect(true)
                .build().awaitReady();
        this.created = true;
    }

    public ActionResult<Void> create(String token) throws InterruptedException {
        try {
            Field f = this.getClass().getDeclaredField("jda");
            Field created = this.getClass().getDeclaredField("created");
            f.setAccessible(true);
            created.setAccessible(true);
            JDA jda = JDABuilder.createDefault(token).setAutoReconnect(true).build().awaitReady();
            f.set(this, jda);
            created.setBoolean(this, true);
            return ActionResult.success();
        } catch (ReflectiveOperationException ex) {
            throw new RuntimeException(ex);
        } catch (InvalidTokenException ex) {
            return ActionResult.fail(ex);
        }
    }

    public JDA getJDA() {
        return jda;
    }
}

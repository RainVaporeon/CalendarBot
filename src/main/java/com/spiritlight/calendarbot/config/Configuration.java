package com.spiritlight.calendarbot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spiritlight.fishutils.action.ActionResult;

import java.io.File;
import java.io.IOException;

public abstract class Configuration {

    protected abstract File file();

    public ActionResult<Void> serialize() {
        try {
            if(!file().exists()) {
                file().createNewFile();
            }
            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter().writeValue(file(), this);
            return ActionResult.success();
        } catch (IOException ex) {
            return ActionResult.fail(ex);
        }
    }

    public ActionResult<Void> deserialize() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.readerForUpdating(this).readValue(file());
            return ActionResult.success();
        } catch (IOException ex) {
            return ActionResult.fail(ex);
        }
    }
}

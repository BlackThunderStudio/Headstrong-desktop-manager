package com.headstrongpro.desktop.core.connection;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Rajmund Staniek on 22-May-17.
 */
public abstract class Configurable {
    protected JSONObject parseJsonConfig(String config, String root) {
        JSONParser parser = new JSONParser();
        try {
            Object file = parser.parse(
                    new InputStreamReader(
                            new FileInputStream(
                                    getClass().getResource(config).getFile())));
            JSONObject fullJSON = (JSONObject) file;
            JSONObject json = (JSONObject) fullJSON.get(root);
            return json;
        } catch (ParseException | IOException e) {
            System.err.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        return null;
    }

    protected abstract List<Object> getConfig();
}

package com.headstrongpro.desktop.core.connection;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Configurable
 */
public abstract class Configurable {
    protected JSONObject parseJsonConfig(String configName, String root) {
        JSONParser parser = new JSONParser();
        try {
            String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
            path = path.substring(1, path.lastIndexOf('/')) + "/cfg";
            Object file = parser.parse(
                    new InputStreamReader(
                            /*getClass().getResourceAsStream(configName)*/
                            new FileInputStream(path + configName)
                    )
            );
            JSONObject fullJSON = (JSONObject) file;
            return (JSONObject) fullJSON.get(root);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected abstract List<Object> getConfig();
}

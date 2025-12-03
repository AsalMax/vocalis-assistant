package com.vocalis;

import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static final Properties props = new Properties();

    static {
        try (InputStream is = ConfigLoader.class.getResourceAsStream("/config.properties")) {
            props.load(is);
        } catch (Exception e) {
            System.err.println("config.properties not found – using defaults");
            props.setProperty("user.name", "User");
            props.setProperty("wake.word", "hey vocalis");
        }
    }

    public static Properties load() {
        return props;
    }
}

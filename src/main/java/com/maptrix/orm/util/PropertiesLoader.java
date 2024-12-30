package com.maptrix.orm.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesLoader {

    public static DatabaseConfig loadFromProperties(String filepath) throws IOException {
        Properties props = new Properties();
        props.load(new FileInputStream(filepath));

        DatabaseConfig config = new DatabaseConfig();
        config.setUrl(props.getProperty("database.url"));
        config.setUsername(props.getProperty("database.username"));
        config.setPassword(props.getProperty("database.password"));
        config.setDriverClassName(props.getProperty("database.driverClassName"));
        return config;
    }

}

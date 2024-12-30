package com.maptrix.orm.util;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class YamlLoader {

    public static DatabaseConfig loadFromYaml(String filepath) throws IOException {
        Yaml yaml = new Yaml();
        try (InputStream in = Files.newInputStream(Paths.get(filepath))) {
            Map<String, Object> yamlMaps = yaml.load(in);

            DatabaseConfig config = new DatabaseConfig();
            Map<String, String> dbSettings = (Map<String, String>) yamlMaps.get("database");
            config.setUrl(dbSettings.get("url"));
            config.setUsername(dbSettings.get("username"));
            config.setPassword(dbSettings.get("password"));
            config.setDriverClassName(dbSettings.get("driverClassName"));
            return config;
        }
    }
}

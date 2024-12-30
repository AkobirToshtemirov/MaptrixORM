package com.maptrix.orm.util;

import java.io.IOException;

public class Configurator {
    public static void initialize(String configFilePath) throws IOException {
        DatabaseConfig config;
        if (configFilePath.endsWith(".yml")) {
            config = YamlLoader.loadFromYaml(configFilePath);
        } else if (configFilePath.endsWith(".properties")) {
            config = PropertiesLoader.loadFromProperties(configFilePath);
        } else {
            throw new IllegalArgumentException("Unsupported configuration file type.");
        }

        DataSourceUtil.configureDataSource(config);
    }
}

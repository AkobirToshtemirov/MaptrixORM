package com.maptrix.orm.util;

import com.maptrix.orm.annotations.Entity;
import com.maptrix.orm.exceptions.DataAccessException;
import com.maptrix.orm.meta.MetaModel;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

public class DataSourceUtil {
    private static HikariDataSource dataSource;

    public static void configureDataSource(DatabaseConfig config) {
        setupDataSource(config);
        autoInitializeDatabaseSchema();
    }

    private static void setupDataSource(DatabaseConfig config) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(config.getUrl());
        hikariConfig.setUsername(config.getUsername());
        hikariConfig.setPassword(config.getPassword());
        hikariConfig.setDriverClassName(config.getDriverClassName());

        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        dataSource = new HikariDataSource(hikariConfig);
    }

    private static void autoInitializeDatabaseSchema() {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forJavaClassPath())
                .setScanners(new SubTypesScanner(false), new TypeAnnotationsScanner()));

        Set<Class<?>> entityClasses = reflections.getTypesAnnotatedWith(Entity.class);

        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement()) {
            for (Class<?> clazz : entityClasses) {
                MetaModel<?> metaModel = MetaModel.of(clazz);
                String sql = metaModel.generateCreateTableSQL();
                System.out.println("Executing SQL: " + sql);
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to initialize database schema automatically", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource is not initialized. Call configureDataSource first.");
        }
        return dataSource.getConnection();
    }
}

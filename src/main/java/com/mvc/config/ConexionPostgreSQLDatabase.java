package com.mvc.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.Properties;

public class ConexionPostgreSQLDatabase {
    
    private static final HikariDataSource dataSource;

    static {
        Properties props = new Properties();

        try(FileInputStream configuracion = new FileInputStream(new File("config.properties"))) {
            props.load(configuracion);
        } catch(IOException e) {
            System.err.println("FALLO CRÍTICO: No se pudo cargar config.properties. " +e.getMessage());
        }

        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(props.getProperty("db.url").trim());
        config.setUsername(props.getProperty("db.user").trim());
        config.setPassword(props.getProperty("db.password").trim());

        config.setMaximumPoolSize(3);
        config.setMinimumIdle(1);

        // Tiempo máximo en ms que se espera para obtener una conexión del pool
        config.setConnectionTimeout(30000);

        // Tiempo máximo en ms que una conexión puede estar inactiva antes de cerrarse
        config.setIdleTimeout(600000);

        // Tiempo máximo de vida de una conexión en ms (10 minutos)
        // Evita usar conexiones que Neon haya cerrado por inactividad
        config.setMaxLifetime(600000);

        // Query liviana para verificar que la conexión sigue activa
        config.setConnectionTestQuery("SELECT 1");

        config.setPoolName("NeonPool");

        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();         
    }
}
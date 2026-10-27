package com.mcq.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * DBConnection.java
 * Provides centralized, robust database connection management for CODE-ARENA.
 * 
 * Supports dynamic configuration via:
 *  1. Environment Variables (e.g. MYSQL_PASSWORD, MYSQL_USER, MYSQL_URL)
 *  2. External Configuration File: config/db.properties
 *  3. Classpath resource: /db.properties
 *  4. Fallback defaults
 */
public class DBConnection {

    private static String dbUrl;
    private static String dbUser;
    private static String dbPassword;
    private static boolean isConfigLoaded = false;

    // Static block to initialize JDBC Driver
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("[DBConnection Error] MySQL JDBC Driver not found in classpath!");
            System.err.println("Ensure lib/mysql-connector-j-8.3.0.jar is present in your classpath.");
        }
        loadConfiguration();
    }

    /**
     * Loads database configuration safely from properties files and environment variables.
     */
    private static synchronized void loadConfiguration() {
        if (isConfigLoaded) return;

        Properties props = new Properties();

        // 1. Try loading from file: config/db.properties or ./db.properties
        File configFile = new File("config/db.properties");
        if (!configFile.exists()) {
            configFile = new File("../config/db.properties");
        }

        if (configFile.exists() && configFile.canRead()) {
            try (InputStream in = new FileInputStream(configFile)) {
                props.load(in);
            } catch (IOException e) {
                System.err.println("[DBConnection Warning] Could not read " + configFile.getAbsolutePath() + ": " + e.getMessage());
            }
        } else {
            // Try loading from classpath
            try (InputStream in = DBConnection.class.getResourceAsStream("/db.properties")) {
                if (in != null) {
                    props.load(in);
                }
            } catch (IOException e) {
                System.err.println("[DBConnection Warning] Could not read db.properties from classpath: " + e.getMessage());
            }
        }

        // Host, port, name resolution
        String host = getPropertyOrEnv(props, "db.host", "MYSQL_HOST", "localhost");
        String port = getPropertyOrEnv(props, "db.port", "MYSQL_PORT", "3306");
        String dbName = getPropertyOrEnv(props, "db.name", "MYSQL_DATABASE", "online_mcq_quiz");
        String params = getPropertyOrEnv(props, "db.params", "MYSQL_PARAMS", "useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");

        // URL construction with override check
        String customUrl = getPropertyOrEnv(props, "db.url", "MYSQL_URL", null);
        if (customUrl != null && !customUrl.trim().isEmpty()) {
            dbUrl = customUrl.trim();
        } else {
            dbUrl = String.format("jdbc:mysql://%s:%s/%s?%s", host, port, dbName, params);
        }

        // Credentials resolution
        dbUser = getPropertyOrEnv(props, "db.user", "MYSQL_USER", "root");
        dbPassword = getPropertyOrEnv(props, "db.password", "MYSQL_PASSWORD", "Pranav@mysql");

        isConfigLoaded = true;
    }

    /**
     * Helper to retrieve value from Properties with fallback to System.getenv() and default.
     */
    private static String getPropertyOrEnv(Properties props, String propKey, String envKey, String defaultValue) {
        // Priority 1: Environment variable
        String envVal = System.getenv(envKey);
        if (envVal != null && !envVal.trim().isEmpty()) {
            return envVal.trim();
        }

        // Priority 2: Properties file
        String propVal = props.getProperty(propKey);
        if (propVal != null && !propVal.trim().isEmpty()) {
            return propVal.trim();
        }

        // Priority 3: Default fallback
        return defaultValue;
    }

    /**
     * Obtains an active connection to the MySQL database.
     * 
     * @return java.sql.Connection object
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        if (!isConfigLoaded) {
            loadConfiguration();
        }
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    /**
     * Tests database connectivity.
     * 
     * @return true if connection succeeds, false otherwise
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("[DBConnection Test Failed] URL: " + dbUrl + " | User: " + dbUser + " | Reason: " + e.getMessage());
            return false;
        }
    }

    /**
     * Re-reads configuration (useful when switching credentials during test execution).
     */
    public static synchronized void reloadConfiguration() {
        isConfigLoaded = false;
        loadConfiguration();
    }
}

package me.leon.lobby.database;

import me.leon.lobby.Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQL {

    private final Main plugin;
    private Connection connection;

    private String host;
    private int port;
    private String database;
    private String username;
    private String password;

    public MySQL(Main plugin) {
        this.plugin = plugin;

        this.host = plugin.getConfig().getString("mysql.host", "localhost");
        this.port = plugin.getConfig().getInt("mysql.port", 3306);
        this.database = plugin.getConfig().getString("mysql.database", "minecraft");
        this.username = plugin.getConfig().getString("mysql.username", "root");
        this.password = plugin.getConfig().getString("mysql.password", "");
    }

    public void connect() {
        try {
            if (connection != null && !connection.isClosed()) {
                plugin.getLogger().info("MySQL ist bereits verbunden!");
                return;
            }

            plugin.getLogger().info("Lade MySQL Treiber...");

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                plugin.getLogger().info("MySQL Connector 8.x Treiber erfolgreich geladen!");
            } catch (ClassNotFoundException e) {
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    plugin.getLogger().info("MySQL Connector 5.x Treiber erfolgreich geladen!");
                } catch (ClassNotFoundException e2) {
                    plugin.getLogger().severe("§cMySQL Treiber konnte nicht geladen werden!");
                    plugin.getLogger().severe("Stelle sicher, dass mysql-connector-java im JAR enthalten ist!");
                    e2.printStackTrace();
                    return;
                }
            }

            String url = "jdbc:mysql://" + host + ":" + port + "/" + database
                    + "?autoReconnect=true"
                    + "&useSSL=false"
                    + "&allowPublicKeyRetrieval=true"
                    + "&serverTimezone=UTC"
                    + "&characterEncoding=utf8"
                    + "&useUnicode=true";

            plugin.getLogger().info("Versuche MySQL Verbindung zu: " + host + ":" + port + "/" + database);

            connection = DriverManager.getConnection(url, username, password);

            if (connection != null && !connection.isClosed()) {
                plugin.getLogger().info("§aMySQL erfolgreich verbunden!");
                createTables();
            } else {
                plugin.getLogger().severe("§cVerbindung fehlgeschlagen - Connection ist null!");
            }

        } catch (SQLException e) {
            plugin.getLogger().severe("=============================================");
            plugin.getLogger().severe("§cMySQL Verbindung fehlgeschlagen!");
            plugin.getLogger().severe("Host: " + host);
            plugin.getLogger().severe("Port: " + port);
            plugin.getLogger().severe("Database: " + database);
            plugin.getLogger().severe("Username: " + username);
            plugin.getLogger().severe("=============================================");
            plugin.getLogger().severe("Mögliche Ursachen:");
            plugin.getLogger().severe("1. MySQL Server läuft nicht");
            plugin.getLogger().severe("2. Falsche Zugangsdaten in der config.yml");
            plugin.getLogger().severe("3. Datenbank existiert nicht");
            plugin.getLogger().severe("4. Firewall blockiert Port 3306");
            plugin.getLogger().severe("=============================================");

            String errorMsg = e.getMessage();
            if (errorMsg != null) {
                if (errorMsg.contains("Access denied")) {
                    plugin.getLogger().severe("§c>> ZUGRIFF VERWEIGERT!");
                    plugin.getLogger().severe("§c>> Prüfe Username und Passwort in config.yml");
                } else if (errorMsg.contains("Unknown database")) {
                    plugin.getLogger().severe("§c>> DATENBANK EXISTIERT NICHT!");
                    plugin.getLogger().severe("§c>> Erstelle die Datenbank mit: CREATE DATABASE " + database + ";");
                } else if (errorMsg.contains("Communications link failure")) {
                    plugin.getLogger().severe("§c>> VERBINDUNG ZUM SERVER FEHLGESCHLAGEN!");
                    plugin.getLogger().severe("§c>> Ist MySQL gestartet? Läuft es auf Port " + port + "?");
                }
            }

            plugin.getLogger().severe("=============================================");
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                plugin.getLogger().info("§eMySQL getrennt!");
            }
        } catch (SQLException e) {
            plugin.getLogger().warning("Fehler beim Trennen der MySQL Verbindung!");
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed() && connection.isValid(2);
        } catch (SQLException e) {
            return false;
        }
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed() || !connection.isValid(2)) {
                plugin.getLogger().warning("MySQL Verbindung verloren - Reconnecting...");
                connect();
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Fehler beim Prüfen der MySQL Verbindung!");
            e.printStackTrace();
            connect();
        }
        return connection;
    }

    private void createTables() {
        if (!isConnected()) {
            plugin.getLogger().warning("Kann Tabellen nicht erstellen - Keine Verbindung!");
            return;
        }

        try {
            PreparedStatement ps = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS warps (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "server_name VARCHAR(50), " +
                            "warp_name VARCHAR(50), " +
                            "world VARCHAR(50), " +
                            "x DOUBLE, " +
                            "y DOUBLE, " +
                            "z DOUBLE, " +
                            "yaw FLOAT, " +
                            "pitch FLOAT, " +
                            "created_by VARCHAR(36), " +
                            "created_at BIGINT, " +
                            "UNIQUE KEY unique_warp (server_name, warp_name), " +
                            "INDEX idx_server (server_name)" +
                            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4"
            );
            ps.executeUpdate();
            ps.close();

            ps = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS shop_purchases (" +
                            "uuid VARCHAR(36), " +
                            "item_id VARCHAR(50), " +
                            "purchased_at BIGINT, " +
                            "PRIMARY KEY (uuid, item_id), " +
                            "INDEX idx_uuid (uuid)" +
                            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4"
            );
            ps.executeUpdate();
            ps.close();

            ps = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS rocket_clicks (" +
                            "uuid VARCHAR(36) PRIMARY KEY, " +
                            "clicks INT DEFAULT 0, " +
                            "last_click BIGINT, " +
                            "total_coins INT DEFAULT 0" +
                            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4"
            );
            ps.executeUpdate();
            ps.close();

            ps = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS player_stats (" +
                            "uuid VARCHAR(36) PRIMARY KEY, " +
                            "total_jumps INT DEFAULT 0, " +
                            "total_distance DOUBLE DEFAULT 0, " +
                            "gadgets_used INT DEFAULT 0, " +
                            "warps_used INT DEFAULT 0, " +
                            "updated_at BIGINT" +
                            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4"
            );
            ps.executeUpdate();
            ps.close();

            plugin.getLogger().info("§aLobbyPlugin Tabellen erfolgreich erstellt!");

        } catch (SQLException e) {
            plugin.getLogger().severe("§cFehler beim Erstellen der LobbyPlugin Tabellen!");
            e.printStackTrace();
        }
    }

    public void update(String qry) {
        if (!isConnected()) {
            plugin.getLogger().warning("Kann Query nicht ausführen - Keine Verbindung!");
            return;
        }

        try {
            PreparedStatement ps = connection.prepareStatement(qry);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            plugin.getLogger().severe("Fehler beim Ausführen von Query: " + qry);
            e.printStackTrace();
        }
    }
}
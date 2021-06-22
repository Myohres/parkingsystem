package com.parkit.parkingsystem.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Properties;


public class DataBaseConfig {

    /**
     * Log parameter.
     */
    private static final Logger LOGGER = LogManager.getLogger("DataBaseConfig");
    /**
     * Path name for config.properties file.
     */
    private static final String DB_LOG_CONNEXION = "config.properties";

    /**
     * Connection to DB parking System.
     * @return parameters connection to DB
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public Connection getConnection()
            throws ClassNotFoundException, SQLException {

        FileInputStream fis = null;
        Properties dbProperties = new Properties();
        String host;
        String login;
        String password;
        Connection con = null;

        try {
            fis = new FileInputStream(DB_LOG_CONNEXION);
            dbProperties.load(fis);
            host = dbProperties.getProperty("db.prod.host");
            login = dbProperties.getProperty("db.prod.login");
            password = dbProperties.getProperty("db.prod.password");
            LOGGER.info("Create DB connection");
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(host, login, password);
        } catch (FileNotFoundException e) {
            System.err.println("Error Reading" + DB_LOG_CONNEXION + "file");
        } catch (IOException e) {
            System.err.println("Error load Properties from" + DB_LOG_CONNEXION);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException ignored) { }
        }
        return con;
    }

    /**
     * Close the DB Parking System Connection.
     * @param con  Connection con
     */
    public void closeConnection(final Connection con) {
        if (con != null) {
            try {
                con.close();
                LOGGER.info("Closing DB connection");
            } catch (SQLException e) {
                LOGGER.error("Error while closing connection", e);
            }
        }
    }

    /**
     * Close the PreparedStatement.
     * @param ps PreparedStatement ps
     */
    public void closePreparedStatement(final PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
                LOGGER.info("Closing Prepared Statement");
            } catch (SQLException e) {
                LOGGER.error("Error while closing prepared statement", e);
            }
        }
    }

    /**
     * Close the ResultSet.
     * @param rs ResultSet rs
     */
    public void closeResultSet(final ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
                LOGGER.info("Closing Result Set");
            } catch (SQLException e) {
                LOGGER.error("Error while closing result set", e);
            }
        }
    }
}

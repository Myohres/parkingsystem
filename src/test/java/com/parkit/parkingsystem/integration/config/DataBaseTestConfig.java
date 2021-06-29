package com.parkit.parkingsystem.integration.config;

import com.parkit.parkingsystem.config.DataBaseConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DataBaseTestConfig extends DataBaseConfig {

    private static final Logger LOGGER = LogManager.getLogger("DataBaseTestConfig");
    private static final String DB_LOG_CONNEXION = "config.properties";

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        FileInputStream fis = null;
        Properties dbProperties = new Properties();
        String host;
        String login;
        String password;
        Connection con = null;

        try {
            fis = new FileInputStream(DB_LOG_CONNEXION);
            dbProperties.load(fis);
            host = dbProperties.getProperty("db.test.host");
            login = dbProperties.getProperty("db.test.login");
            password = dbProperties.getProperty("db.test.password");
            LOGGER.info("Create DB connection");
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(host, login, password);
        } catch (FileNotFoundException e) {
            System.err.println("Error Reading" + DB_LOG_CONNEXION + "file");
        } catch (IOException e) {
            System.err.println("Error load Properties from" + DB_LOG_CONNEXION);
        } finally {
            try { if (fis != null) { fis.close(); } } catch (IOException e) { }
        }
        return con;
    }

    public void closeConnection(Connection con){
        if(con!=null){
            try {
                con.close();
                LOGGER.info("Closing DB connection");
            } catch (SQLException e) {
                LOGGER.error("Error while closing connection",e);
            }
        }
    }

    public void closePreparedStatement(PreparedStatement ps) {
        if(ps!=null){
            try {
                ps.close();
                LOGGER.info("Closing Prepared Statement");
            } catch (SQLException e) {
                LOGGER.error("Error while closing prepared statement",e);
            }
        }
    }

    public void closeResultSet(ResultSet rs) {
        if(rs!=null){
            try {
                rs.close();
                LOGGER.info("Closing Result Set");
            } catch (SQLException e) {
                LOGGER.error("Error while closing result set",e);
            }
        }
    }
}

package ru.itmo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class Database {
    private static final Logger logger = LoggerFactory.getLogger(Database.class);
    private static final String DATASOURCE = "java:comp/env/jdbc/cars_db";
    private static final DataSource dataSource;

    static {
        try {
            Context initContext = new InitialContext();
            dataSource = (DataSource) initContext.lookup(DATASOURCE);
            logger.info("DataSource initialized successfully");
        } catch (NamingException e) {
            logger.error("Could not find DataSource: {}", DATASOURCE, e);
            throw new RuntimeException("Failed to initialize DataSource", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        logger.debug("Getting database connection");
        return dataSource.getConnection();
    }
}
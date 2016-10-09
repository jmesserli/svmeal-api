package nu.peg.svmeal;

import com.mashape.unirest.http.Unirest;

import nu.peg.svmeal.model.RestaurantDto;
import nu.peg.svmeal.model.SvRestaurant;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class AppInitializer implements ServletContextListener {

    public static Connection dbConnection;
    public static Logger logger;
    public static List<SvRestaurant> restaurants;
    public static List<RestaurantDto> restaurantDtos;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        logger = Logger.getLogger("svmeal");

        try {
            Path tempDb = Files.createTempFile("svmeal-cache.", ".db");
            logger.fine(String.format("SVMeal Cache File: %s%n", tempDb.toAbsolutePath().toString()));

            Class.forName("org.sqlite.JDBC");
            dbConnection = DriverManager.getConnection(String.format("jdbc:sqlite:%s", tempDb.toAbsolutePath().toString()));

        } catch (Exception e) {
            logger.severe("Application initialization failed");
            throw new RuntimeException(e);
        }

        logger.info("SVMeal initialized.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        try {
            dbConnection.close();
            logger.fine("DbConnection closed.");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            Unirest.shutdown();
            logger.fine("Shut down Unirest thread.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info("Servlet destroy complete");
    }
}

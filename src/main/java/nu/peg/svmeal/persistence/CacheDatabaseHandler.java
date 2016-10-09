package nu.peg.svmeal.persistence;

import nu.peg.svmeal.AppInitializer;
import nu.peg.svmeal.model.MealPlanResponse;
import nu.peg.svmeal.model.Restaurant;

import java.io.*;
import java.sql.*;
import java.util.logging.Level;

import static nu.peg.svmeal.AppInitializer.logger;

public class CacheDatabaseHandler {

    private Connection connection;

    public CacheDatabaseHandler() {
        try {
            init();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Cannot initialize cache database", e);
        }
    }

    private void init() throws SQLException {
        connection = AppInitializer.dbConnection;

        Statement stmt = connection.createStatement();
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS cache (dayoffset INT NOT NULL, restaurant INT NOT NULL, response BLOB NOT NULL, timestamp INT NOT NULL) ");
    }

    public void add(int dayOffset, Restaurant restaurant, MealPlanResponse response) {
        // Serialize response

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(response);
            oos.flush();
        } catch (IOException e) {
            logger.log(Level.SEVERE, String.format("Cannot serialize object %s", response), e);
        }

        byte[] responseBytes = baos.toByteArray();

        try {
            // remove old matches
            String query = "DELETE FROM cache WHERE dayoffset = ? AND restaurant = ?";
            PreparedStatement stmt = getConnection().prepareStatement(query);
            stmt.setInt(1, dayOffset);
            stmt.setInt(2, restaurant.ordinal());
            stmt.execute();

            query = "INSERT INTO cache (dayoffset, restaurant, response, timestamp) VALUES (?, ?, ?, ?)";
            stmt = getConnection().prepareStatement(query);
            stmt.setInt(1, dayOffset);
            stmt.setInt(2, restaurant.ordinal());
            stmt.setBytes(3, responseBytes);
            stmt.setLong(4, System.currentTimeMillis());

            stmt.execute();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Cannot insert blob into cache database", e);
        }
    }

    public MealPlanResponse get(int dayOffset, Restaurant restaurant) {
        String query = "SELECT response, timestamp FROM cache WHERE dayoffset = ? AND restaurant = ? ORDER BY timestamp DESC LIMIT 1";
        try {
            PreparedStatement stmt = getConnection().prepareStatement(query);
            stmt.setInt(1, dayOffset);
            stmt.setInt(2, restaurant.ordinal());

            ResultSet results = stmt.executeQuery();
            if (!results.next()) return null;

            byte[] bytes = results.getBytes(1);
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));

            return (MealPlanResponse) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @param maxAge Maximum age in seconds
     */
    public void cleanOld(int maxAge) {
        String query = "DELETE FROM cache WHERE (? - timestamp) / 1000 > ?";
        try {
            PreparedStatement stmt = getConnection().prepareStatement(query);
            stmt.setLong(1, System.currentTimeMillis());
            stmt.setInt(2, maxAge);
            stmt.execute();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Cannot clean old cache objects", e);
        }
    }

    private Connection getConnection() {
        return connection;
    }
}

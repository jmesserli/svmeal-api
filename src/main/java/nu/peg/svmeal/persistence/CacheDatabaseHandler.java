package nu.peg.svmeal.persistence;

import nu.peg.svmeal.model.MealPlanResponse;
import nu.peg.svmeal.model.Restaurant;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CacheDatabaseHandler {

    private Connection connection;

    public CacheDatabaseHandler() {
        try {
            Class.forName("org.sqlite.JDBC");
            init();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void init() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:cache.db");

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
        } catch (Exception e) {
            e.printStackTrace();
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
        } catch (Exception e) {
            e.printStackTrace();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() {
        return connection;
    }
}

package kz.university.gym.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresDBManager implements IDBManager {
    private static final String URL = "jdbc:postgresql://localhost:5432/gym-membership-system";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";

    @Override
    public Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Error with connection with DB", e);
        }
    }
}

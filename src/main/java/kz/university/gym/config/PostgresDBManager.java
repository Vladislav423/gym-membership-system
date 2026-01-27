package kz.university.gym.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresDBManager implements IDBManager {
    private static PostgresDBManager INSTANCE;

    private static final String URL = "jdbc:postgresql://localhost:5432/gym-membership-system";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";

    private PostgresDBManager() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static synchronized PostgresDBManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PostgresDBManager();
        }
        return INSTANCE;
    }

    @Override
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Error with connection with DB", e);
        }
    }
}

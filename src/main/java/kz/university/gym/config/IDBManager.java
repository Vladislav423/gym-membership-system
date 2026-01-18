package kz.university.gym.config;

import java.sql.Connection;

public interface IDBManager {
    Connection getConnection();
}

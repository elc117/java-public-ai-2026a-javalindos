package com.recomendador.repositories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private static final String URL = "jdbc:sqlite:backend/src/main/resources/database.db";

    public static Connection getConnection() throws SQLException {
        try {
            // Garante que o driver JDBC do SQLite está carregado
            Class.forName("org.xerial.sqlite-jdbc");
            return DriverManager.getConnection(URL);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver SQLite não encontrado.", e);
        }
    }
}
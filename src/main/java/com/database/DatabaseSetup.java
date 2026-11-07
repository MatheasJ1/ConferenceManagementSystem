package com.database;


import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;
import java.io.InputStream;

public class DatabaseSetup {

    public static void main(String[] args) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            runScript(conn, "schema.sql");
            runScript(conn, "seed.sql");
        }

        System.out.println("Database setup complete.");
    }

    private static void runScript(Connection conn, String filename)
            throws Exception {

        try (InputStream input = DatabaseSetup.class
                .getClassLoader().getResourceAsStream(filename)) {

            if (input == null) {
                throw new IllegalStateException(
                        "Cannot find " + filename
                );
            }

            String script = new String(
                    input.readAllBytes(),
                    StandardCharsets.UTF_8
            );

            try (Statement stmt = conn.createStatement()) {
                for (String sql : script.split(";")) {
                    if (!sql.trim().isEmpty()) {
                        stmt.execute(sql);
                    }
                }
            }
        }
    }
}
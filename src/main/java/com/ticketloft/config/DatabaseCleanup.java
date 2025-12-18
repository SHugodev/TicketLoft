package com.ticketloft.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

@Component
public class DatabaseCleanup implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== FORCE DatabaseCleanup STARTED ===");

        try {
            // 1. Disable Foreign Key Checks to avoid "needed in foreign key" errors
            jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");
            System.out.println("Disabled Foreign Key Checks");

            // FIX: Initialize VERSION columns to 0 if they are null (Fixes "current is
            // null" NPE)
            try {
                jdbcTemplate.execute("UPDATE tipos_entrada SET version = 0 WHERE version IS NULL");
                System.out.println("Initialized NULL versions in tipos_entrada");
            } catch (Exception e) {
            }

            try {
                jdbcTemplate.execute("UPDATE eventos SET version = 0 WHERE version IS NULL");
                System.out.println("Initialized NULL versions in eventos");
            } catch (Exception e) {
            }

            // FIX: Increase column size for imagen_url (Fixes "Data too long")
            try {
                jdbcTemplate.execute("ALTER TABLE eventos MODIFY imagen_url VARCHAR(2048)");
                System.out.println("Increased size of imagen_url column");
            } catch (Exception e) {
                System.out.println("Error resizing imagen_url: " + e.getMessage());
            }

            // 2. Find ALL Unique Constraints on 'reservas'
            List<Map<String, Object>> constraints = jdbcTemplate.queryForList(
                    "SELECT CONSTRAINT_NAME FROM information_schema.TABLE_CONSTRAINTS " +
                            "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'reservas' AND CONSTRAINT_TYPE = 'UNIQUE'");

            for (Map<String, Object> row : constraints) {
                String constraintName = (String) row.get("CONSTRAINT_NAME");
                System.out.println("FOUND RESOURCE TO DROP: " + constraintName);

                try {
                    jdbcTemplate.execute("ALTER TABLE reservas DROP INDEX " + constraintName);
                    System.out.println("DROPPED INDEX: " + constraintName);
                } catch (Exception e) {
                    System.err.println("Error dropping index " + constraintName + ": " + e.getMessage());
                }
            }

            // 3. Re-enable Foreign Key Checks
            jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
            System.out.println("Re-enabled Foreign Key Checks");

        } catch (Exception e) {
            e.printStackTrace();
            // Ensure we try to re-enable checks even if something failed
            try {
                jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
            } catch (Exception ex) {
            }
        }

        System.out.println("=== FORCE DatabaseCleanup FINISHED ===");
    }
}

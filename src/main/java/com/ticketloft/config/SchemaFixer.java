package com.ticketloft.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

@Component
public class SchemaFixer implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        try {
            System.out.println("Running SchemaFixer to check for stale constraints...");

            // Query for unique constraints on the 'reservas' table
            String sqlCheck = "SELECT CONSTRAINT_NAME " +
                    "FROM information_schema.TABLE_CONSTRAINTS " +
                    "WHERE TABLE_SCHEMA = DATABASE() " +
                    "AND TABLE_NAME = 'reservas' " +
                    "AND CONSTRAINT_TYPE = 'UNIQUE'";

            List<Map<String, Object>> constraints = jdbcTemplate.queryForList(sqlCheck);

            for (Map<String, Object> row : constraints) {
                String constraintName = (String) row.get("CONSTRAINT_NAME");
                System.out.println("Found unique constraint: " + constraintName);

                // Drop the constraint
                String sqlDrop = "ALTER TABLE reservas DROP INDEX " + constraintName;
                jdbcTemplate.execute(sqlDrop);
                System.out.println("Dropped constraint: " + constraintName);
            }

        } catch (Exception e) {
            System.err.println("SchemaFixer error (might be already fixed): " + e.getMessage());
        }
    }
}

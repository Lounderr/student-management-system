package data;

import java.sql.*;

public class Database {
    public static Connection connection;
    public static boolean isInitialized = false;

    public static void init() {
        try {
            Class.forName("org.h2.Driver");

            connection = DriverManager.getConnection("jdbc:h2:~/test;AUTO_SERVER=TRUE", "sa", "");

            if (isInitialized) {
                return;
            }

            try (Statement stmt = connection.createStatement()) {
                // Drop existing tables if they exist
                stmt.execute("DROP TABLE IF EXISTS Grade;");
                stmt.execute("DROP TABLE IF EXISTS Major_Subject;");
                stmt.execute("DROP TABLE IF EXISTS Subject;");
                stmt.execute("DROP TABLE IF EXISTS Student;");
                stmt.execute("DROP TABLE IF EXISTS Major;");

                // Create Major table
                stmt.execute("""
                            CREATE TABLE Major (
                                id IDENTITY PRIMARY KEY,
                                name VARCHAR(80) NOT NULL UNIQUE CHECK (LENGTH(name) BETWEEN 2 AND 80),
                                total_years INT NOT NULL CHECK (total_years BETWEEN 1 AND 15)
                            );
                        """);

                // Create Subject table
                stmt.execute("""
                            CREATE TABLE Subject (
                                id IDENTITY PRIMARY KEY,
                                name VARCHAR(100) NOT NULL UNIQUE CHECK (LENGTH(name) BETWEEN 2 AND 100)
                            );
                        """);

                // Create Major_Subject join table
                stmt.execute("""
                            CREATE TABLE Major_Subject (
                                major_id INT NOT NULL,
                                subject_id INT NOT NULL,
                                PRIMARY KEY (major_id, subject_id),
                                FOREIGN KEY (major_id) REFERENCES Major(id) ON DELETE CASCADE,
                                FOREIGN KEY (subject_id) REFERENCES Subject(id) ON DELETE CASCADE
                            );
                        """);

                // Create Student table
                                stmt.execute("""
                            CREATE TABLE Student (
                                id INT IDENTITY PRIMARY KEY,
                                faculty_number CHAR(10) UNIQUE,
                                full_name VARCHAR(255) NOT NULL,
                                major_id INT NOT NULL,
                                year INT NOT NULL CHECK (year >= 1),
                                FOREIGN KEY (major_id) REFERENCES Major(id)
                            );
                        """);

                // Create Grade table
                stmt.execute("""
                            CREATE TABLE Grade (
                                id IDENTITY PRIMARY KEY,
                                student_id CHAR(10) NOT NULL,
                                subject_id INT NOT NULL,
                                grade INT NOT NULL CHECK (grade BETWEEN 2 AND 6),
                                FOREIGN KEY (student_id) REFERENCES Student(id) ON DELETE CASCADE,
                                FOREIGN KEY (subject_id) REFERENCES Subject(id) ON DELETE CASCADE,
                                UNIQUE (student_id, subject_id)
                            );
                        """);

                isInitialized = true;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database init failed", e);
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException("H2 Driver not found", e);
        }
    }

    public static void close() {
        try {
            if (connection != null && !connection.isClosed())
                connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addMockData() {
        if (isInitialized)
        {
            return;
        }

        try (Statement stmt = connection.createStatement()) {
            // Insert mock data into Major table
            stmt.execute("INSERT INTO Major (name, total_years) VALUES ('Computer Science', 4), ('Mathematics', 3);");

            // Insert mock data into Subject table
            stmt.execute("INSERT INTO Subject (name) VALUES ('Algorithms'), ('Linear Algebra'), ('Databases');");

            // Insert mock data into Major_Subject table
            stmt.execute("INSERT INTO Major_Subject (major_id, subject_id) VALUES (1, 1), (1, 3), (2, 2);");

            // Insert mock data into Student table
            stmt.execute("""
                        INSERT INTO Student (faculty_number, full_name, major_id, year) 
                        VALUES ('1234567890', 'Alice Johnson Smith', 1, 2),
                               ('0987654321', 'Bob Rogers Smith', 2, 1);
                    """);

            // Insert mock data into Grade table
            stmt.execute("""
                        INSERT INTO Grade (student_id, subject_id, grade) 
                        VALUES (1, 1, 5),
                               (1, 3, 4),
                               (2, 2, 6);
                    """);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

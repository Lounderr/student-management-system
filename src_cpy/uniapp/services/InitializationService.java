package uniapp.services;


import java.sql.Statement;

public class InitializationService {
    private final Statement stmt;
    private boolean isInitialized = false;

    public InitializationService(Statement stmt) {
        this.stmt = stmt;
    }

    public void initialize() throws Exception {
        if (isInitialized)
        {
            throw new Exception("Already initialized");
        }

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
                    id CHAR(10) PRIMARY KEY CHECK (id REGEXP '^[0-9]{10}$'),
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

        this.isInitialized = true;
    }
}

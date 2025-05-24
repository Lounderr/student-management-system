package data.model.repository.impl;

import data.Database;
import data.model.Grade;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GradeRepositoryOld {

    // Insert a new Grade
    public void save(Grade grade) throws SQLException {
        String sql = "INSERT INTO grades (id, subject_id, student_id, grade) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = Database.connection.prepareStatement(sql)) {
            stmt.setInt(1, grade.getId());
            stmt.setInt(2, grade.getSubjectId());
            stmt.setInt(3, grade.getStudentId());
            stmt.setInt(4, grade.getGrade());
            stmt.executeUpdate();
        }
    }

    // Find Grade by id
    public Grade findById(int id) throws SQLException {
        String sql = "SELECT * FROM grade WHERE id = ?";
        try (PreparedStatement stmt = Database.connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Grade(
                            rs.getInt("id"),
                            rs.getInt("subject_id"),
                            rs.getInt("student_id"),
                            rs.getInt("grade")
                    );
                }
                return null;
            }
        }
    }

    // Find by student id
    public List<Grade> findByStudentId(int studentId) throws SQLException {
        String sql = "SELECT * FROM grade WHERE student_id = ?";
        List<Grade> grades = new ArrayList<>();
        try (PreparedStatement stmt = Database.connection.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    grades.add(new Grade(
                            rs.getInt("id"),
                            rs.getInt("subject_id"),
                            rs.getInt("student_id"),
                            rs.getInt("grade")
                    ));
                }
            }
        }
        return grades;
    }

    // Update an existing Grade
    public void update(Grade grade) throws SQLException {
        String sql = "UPDATE grades SET subject_id = ?, student_id = ?, grade = ? WHERE id = ?";
        try (PreparedStatement stmt = Database.connection.prepareStatement(sql)) {
            stmt.setInt(1, grade.getSubjectId());
            stmt.setInt(2, grade.getStudentId());
            stmt.setInt(3, grade.getGrade());
            stmt.setInt(4, grade.getId());
            stmt.executeUpdate();
        }
    }

    // Delete a Grade by id
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM grade WHERE id = ?";
        try (PreparedStatement stmt = Database.connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}

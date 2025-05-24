package data.model.repository.impl;

import data.model.Grade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

public class GradesRepository implements IRepository<Grade> {
    private final Connection connection;

    public GradesRepository(Connection connection) {
        this.connection = connection;
    }

    public List<Grade> All() {
        List<Grade> grades = new ArrayList<>();
        try {
            var resultSet = connection.createStatement().executeQuery("SELECT id, subject_id, student_id, grade FROM Grade");
            while (resultSet.next()) {
                Grade grade = new Grade(
                        resultSet.getInt("id"),
                        resultSet.getInt("subject_id"),
                        resultSet.getInt("student_id"),
                        resultSet.getInt("grade")
                );
                grades.add(grade);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return grades;
    }

    public List<Grade> AllByStudentId(int studentId) {
        List<Grade> grades = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, subject_id, student_id, grade FROM Grade WHERE student_id = ?");
            preparedStatement.setInt(1, studentId);
            var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Grade grade = new Grade(
                        resultSet.getInt("id"),
                        resultSet.getInt("subject_id"),
                        resultSet.getInt("student_id"),
                        resultSet.getInt("grade")
                );
                grades.add(grade);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return grades;
    }

    public Grade FindById(int id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, subject_id, student_id, grade FROM Grade WHERE id = ?");
            preparedStatement.setInt(1, id);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Grade(
                        resultSet.getInt("id"),
                        resultSet.getInt("subject_id"),
                        resultSet.getInt("student_id"),
                        resultSet.getInt("grade")
                );
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean Add(Grade grade) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Grade (subject_id, student_id, grade) VALUES (?, ?, ?)");
            preparedStatement.setInt(1, grade.getSubjectId());
            preparedStatement.setInt(2, grade.getStudentId());
            preparedStatement.setInt(3, grade.getGrade());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean Update(Grade grade) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Grade SET subject_id = ?, student_id = ?, grade = ? WHERE id = ?");
            preparedStatement.setInt(1, grade.getSubjectId());
            preparedStatement.setInt(2, grade.getStudentId());
            preparedStatement.setInt(3, grade.getGrade());
            preparedStatement.setInt(4, grade.getId());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean Delete(int id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Grade WHERE id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public OptionalDouble getAverageForMajorAndYear(int majorId, int year) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT AVG(g.grade) as average " +
                "FROM Grade g " +
                "JOIN Student s ON g.student_id = s.id " +
                "WHERE s.major_id = ? AND s.year = ?"
            );
            preparedStatement.setInt(1, majorId);
            preparedStatement.setInt(2, year);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                double avg = resultSet.getDouble("average");
                return resultSet.wasNull() ? OptionalDouble.empty() : OptionalDouble.of(avg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OptionalDouble.empty();
    }
}

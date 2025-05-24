package uniapp.repositories;

import uniapp.model.Major;
import uniapp.model.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class StudentsRepository implements IRepository<Student> {
    private final Connection connection;

    public StudentsRepository(Connection connection) {
        this.connection = connection;
    }

    public List<Student> All() {
        List<Student> students = new ArrayList<>();
        try {
            var resultSet = connection.createStatement().executeQuery(
                    "SELECT s.id, s.faculty_number, s.full_name, s.major_id, s.year, m.name, m.total_years " +
                            "FROM Student s " +
                            "JOIN Major m ON s.major_id = m.id"
            );
            while (resultSet.next()) {
                Major major = new Major(
                        resultSet.getInt("major_id"),
                        resultSet.getString("name"),
                        resultSet.getInt("total_years")
                );
                Student student = new Student(
                        resultSet.getInt("id"),
                        resultSet.getString("faculty_number"),
                        resultSet.getString("full_name"),
                        major,
                        resultSet.getInt("year")
                );
                students.add(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }

    public Student FindById(int id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT s.id, s.faculty_number, s.full_name, s.major_id, s.year, m.name, m.total_years " +
                            "FROM Student s " +
                            "JOIN Major m ON s.major_id = m.id " +
                            "WHERE s.id = ?"
            );
            preparedStatement.setInt(1, id);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Major major = new Major(
                        resultSet.getInt("major_id"),
                        resultSet.getString("name"),
                        resultSet.getInt("total_years")
                );
                return new Student(
                        resultSet.getInt("id"),
                        resultSet.getString("faculty_number"),
                        resultSet.getString("full_name"),
                        major,
                        resultSet.getInt("year")
                );
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean Add(Student student) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO Student (faculty_number, full_name, major_id, year) VALUES (?, ?, ?, ?)"
            );
            preparedStatement.setString(1, student.getFacultyNumber());
            preparedStatement.setString(2, student.getFullName());
            preparedStatement.setInt(3, student.getMajor().getId());
            preparedStatement.setInt(4, student.getYear());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean Update(Student student) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE Student SET faculty_number = ?, full_name = ?, major_id = ?, year = ? WHERE id = ?"
            );
            preparedStatement.setString(1, student.getFacultyNumber());
            preparedStatement.setString(2, student.getFullName());
            preparedStatement.setInt(3, student.getMajor().getId());
            preparedStatement.setInt(4, student.getYear());
            preparedStatement.setInt(5, student.getId());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean Delete(int id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Student WHERE id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
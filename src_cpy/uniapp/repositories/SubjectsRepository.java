package uniapp.repositories;

import uniapp.model.Subject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class SubjectsRepository implements IRepository<Subject> {
    private final Connection connection;

    public SubjectsRepository(Connection connection) {
        this.connection = connection;
    }

    public List<Subject> All() {
        List<Subject> subjects = new ArrayList<>();
        try {
            var resultSet = connection.createStatement().executeQuery("SELECT id, name FROM Subject");
            while (resultSet.next()) {
                Subject subject = new Subject(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                );
                subjects.add(subject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return subjects;
    }

    public List<Subject> AllByStudentId(int studentId) {
        List<Subject> subjects = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, name FROM Subject WHERE id = ?");
            preparedStatement.setInt(1, studentId);
            var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Subject subject = new Subject(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                );
                subjects.add(subject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return subjects;
    }

    public Subject FindById(int id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, name FROM Subject WHERE id = ?");
            preparedStatement.setInt(1, id);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Subject(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                );
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean Add(Subject subject) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Subject (name) VALUES (?)");
            preparedStatement.setString(1, subject.getName());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean Update(Subject subject) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Subject SET name = ? WHERE id = ?");
            preparedStatement.setString(1, subject.getName());
            preparedStatement.setInt(2, subject.getId());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean Delete(int id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Subject WHERE id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
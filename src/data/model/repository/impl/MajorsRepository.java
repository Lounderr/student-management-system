package data.model.repository.impl;

import data.model.Major;
import data.model.Subject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class MajorsRepository implements IRepository<Major> {
    private final Connection connection;
    private final SubjectsRepository subjectsRepository;

    public MajorsRepository(Connection connection) {
        this.connection = connection;
        this.subjectsRepository = new SubjectsRepository(connection);
    }

    public List<Major> All() {
        List<Major> majors = new ArrayList<>();
        try {
            var resultSet = connection.createStatement().executeQuery("SELECT id, name, total_years FROM Major");
            while (resultSet.next()) {
                Major major = new Major(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("total_years")
                );
                majors.add(major);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return majors;
    }

    public List<Major> AllByStudentId(int studentId) {
        List<Major> majors = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, name, total_years FROM Major WHERE id = ?");
            preparedStatement.setInt(1, studentId);
            var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Major major = new Major(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("total_years")
                );
                majors.add(major);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return majors;
    }

    public Major FindById(int id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, name, total_years FROM Major WHERE id = ?");
            preparedStatement.setInt(1, id);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Major(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("total_years")
                );
            }
            else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean Add(Major major) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Major (name, total_years) VALUES (?, ?)");
            preparedStatement.setString(1, major.getName());
            preparedStatement.setInt(2, major.getTotalYears());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean Update(Major major) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Major SET name = ?, total_years = ? WHERE id = ?");
            preparedStatement.setString(1, major.getName());
            preparedStatement.setInt(2, major.getTotalYears());
            preparedStatement.setInt(3, major.getId());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean Delete(int id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Major WHERE id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public List<Subject> GetSubjectsForMajor(int majorId) {
        List<Subject> subjects = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT subject_id FROM Major_Subject WHERE major_id = ?"
            );
            preparedStatement.setInt(1, majorId);
            var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                var subject = subjectsRepository.FindById(resultSet.getInt("subject_id"));
                if (subject != null) {
                    subjects.add(subject);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return subjects;
    }
}

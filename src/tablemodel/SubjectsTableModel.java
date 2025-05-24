package tablemodel;

import data.model.*;
import data.model.repository.impl.*;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.Arrays;
import java.util.List;


public class SubjectsTableModel extends AbstractTableModel {
    private final List<Subject> subjects;
    private final List<Student> students;
    private final Major major;
    private final MajorsRepository majorsRepository;
    private final GradesRepository gradesRepository;
    private final SubjectsRepository subjectsRepository;

    private final String[] columnNames = {
            "Предмет",
    };

    public SubjectsTableModel(Major major, List<Student> students, MajorsRepository majorsRepository, GradesRepository gradesRepository) {
        this.major = major;
        this.majorsRepository = majorsRepository;
        this.gradesRepository = gradesRepository;
        this.subjectsRepository = new SubjectsRepository(majorsRepository.getConnection());
        this.subjects = majorsRepository.GetSubjectsForMajor(major.getId());
        this.students = students;
    }

    @Override
    public int getRowCount() {
        return subjects.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int column) {
        return Subject.class;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        // SQL SELECT
        return subjects.get(rowIndex);
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        // SQL UPDATE

        try {
            subjects.get(rowIndex).setName((String) value);

            fireTableCellUpdated(rowIndex, columnIndex);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Subject getSubject(int row) {
        return subjects.get(row);
    }

    public void addSubject(Subject subject) {
        insertSubject(getRowCount(), subject);
    }

    public void removeSubject(int row) {
        int confirmation = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to delete this subject?\n" +
                        "Deleting this subject will also delete all related grades.",
                "Confirmation", JOptionPane.OK_CANCEL_OPTION);

        if (confirmation == JOptionPane.OK_OPTION) {
            var subject = subjects.get(row);
            
            // First delete all grades for this subject
            for (Student student : students) {
                if (student.getMajor().getId() == this.major.getId()) {
                    var grades = gradesRepository.AllByStudentId(student.getId());
                    for (var grade : grades) {
                        if (grade.getSubjectId() == subject.getId()) {
                            gradesRepository.Delete(grade.getId());
                        }
                    }
                }
            }

            // Delete the subject-major relationship and the subject itself
            try {
                var connection = majorsRepository.getConnection();
                connection.setAutoCommit(false); // Start transaction
                
                try {
                    // Delete the subject-major relationship
                    var deleteRelationship = connection.prepareStatement("DELETE FROM Major_Subject WHERE major_id = ? AND subject_id = ?");
                    deleteRelationship.setInt(1, major.getId());
                    deleteRelationship.setInt(2, subject.getId());
                    deleteRelationship.executeUpdate();

                    // Delete the subject
                    if (!subjectsRepository.Delete(subject.getId())) {
                        throw new Exception("Failed to delete subject");
                    }

                    connection.commit(); // Commit transaction
                    subjects.remove(row);
                    fireTableRowsDeleted(row, row);
                } catch (Exception e) {
                    connection.rollback(); // Rollback on error
                    throw e;
                } finally {
                    connection.setAutoCommit(true); // Reset auto-commit
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to delete subject: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void insertSubject(int row, Subject subject) {
        try {
            // Check if subject name is unique
            boolean isSubjectNameUnique = subjects.stream().map(Subject::getName).noneMatch(n -> n.equalsIgnoreCase(subject.getName()));
            if (!isSubjectNameUnique) {
                JOptionPane.showMessageDialog(null, "Subject with this name already exists", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Add the subject and create the relationship in a transaction
            var connection = majorsRepository.getConnection();
            connection.setAutoCommit(false); // Start transaction
            
            try {
                // First add the subject and get its ID
                var insertSubject = connection.prepareStatement("INSERT INTO Subject (name) VALUES (?)", java.sql.Statement.RETURN_GENERATED_KEYS);
                insertSubject.setString(1, subject.getName());
                insertSubject.executeUpdate();

                // Get the generated ID
                var generatedKeys = insertSubject.getGeneratedKeys();
                if (!generatedKeys.next()) {
                    throw new Exception("Failed to get new subject ID");
                }
                int newSubjectId = generatedKeys.getInt(1);

                // Create a new subject with the ID
                Subject newSubject = new Subject(newSubjectId, subject.getName());

                // Create the subject-major relationship
                var insertRelationship = connection.prepareStatement("INSERT INTO Major_Subject (major_id, subject_id) VALUES (?, ?)");
                insertRelationship.setInt(1, major.getId());
                insertRelationship.setInt(2, newSubjectId);
                insertRelationship.executeUpdate();

                connection.commit(); // Commit transaction
                
                // Update the UI
                subjects.add(row, newSubject);
                fireTableRowsInserted(row, row);
            } catch (Exception e) {
                connection.rollback(); // Rollback on error
                throw e;
            } finally {
                connection.setAutoCommit(true); // Reset auto-commit
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to add subject: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}

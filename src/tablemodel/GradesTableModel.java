package tablemodel;

import data.model.*;
import data.model.repository.impl.*;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class GradesTableModel extends AbstractTableModel {
    private List<Grade> grades;
    private final String[] columnNames = {
            "Предмет",
            "Оценка"
    };
    private final Student student;
    private final GradesRepository gradesRepository;
    private final SubjectsRepository subjectsRepository;

    public GradesTableModel(Student student, GradesRepository gradesRepository, SubjectsRepository subjectsRepository) {
        this.student = student;
        this.gradesRepository = gradesRepository;
        this.subjectsRepository = subjectsRepository;
        refreshGrades();
    }

    public void refreshGrades() {
        this.grades = gradesRepository.AllByStudentId(student.getId());
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return grades.size();
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
        return switch (column) {
            case 0 -> Subject.class;
            case 1 -> Integer.class;
            default -> null;
        };
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return true;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        // SQL SELECT
        return switch (columnIndex) {
            case 0 -> subjectsRepository.FindById(grades.get(rowIndex).getSubjectId());
            case 1 -> grades.get(rowIndex).getGrade();
            default -> null;
        };
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        // SQL UPDATE
        try {
            var grade = grades.get(rowIndex);
            switch (columnIndex) {
                case 0 -> grade.setSubjectId(((Subject)value).getId());
                case 1 -> grades.get(rowIndex).setGrade((Integer)value);
            }

            gradesRepository.Update(grade);
            refreshGrades();
            fireTableCellUpdated(rowIndex, columnIndex);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Grade getGrade(int row) {
        return grades.get(row);
    }

    public void addGrade(Grade grade) {
        insertSubject(getRowCount(), grade);
    }

    public void removeGrade(int row) {
        // SQL REMOVE
        var grade = grades.get(row);
        gradesRepository.Delete(grade.getId());
        refreshGrades();
    }

    public void insertSubject(int row, Grade grade) {
        // SQL ADD
        try {
            // Check if a grade already exists for this subject
            var existingGrades = gradesRepository.AllByStudentId(student.getId());
            var existingGrade = existingGrades.stream()
                .filter(g -> g.getSubjectId() == grade.getSubjectId())
                .findFirst();

            if (existingGrade.isPresent()) {
                // Ask user if they want to update the existing grade
                int response = JOptionPane.showConfirmDialog(
                    null,
                    "A grade for this subject already exists. Do you want to update it?",
                    "Update Existing Grade",
                    JOptionPane.YES_NO_OPTION
                );

                if (response == JOptionPane.YES_OPTION) {
                    // Update the existing grade
                    var gradeToUpdate = existingGrade.get();
                    gradeToUpdate.setGrade(grade.getGrade());
                    if (gradesRepository.Update(gradeToUpdate)) {
                        refreshGrades();
                    } else {
                        throw new Exception("Failed to update grade in database");
                    }
                }
            } else {
                // Add new grade
                if (gradesRepository.Add(grade)) {
                    refreshGrades();
                } else {
                    throw new Exception("Failed to add grade to database");
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

package tablemodel;

import data.model.*;
import data.model.repository.impl.*;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class StudentsTableModel extends AbstractTableModel {
    private final StudentsRepository studentsRepository;
    private final MajorsRepository majorsRepository;
    private List<Student> students;

    private final String[] columnNames = {
            "Факултетен номер",
            "Име на студента",
            "Специалност",
            "Курс"
    };

    public StudentsTableModel(StudentsRepository studentsRepository, MajorsRepository majorsRepository) {
        this.studentsRepository = studentsRepository;
        this.majorsRepository = majorsRepository;
        refreshStudents();
    }

    public void refreshStudents() {
        this.students = studentsRepository.All();
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return students.size();
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
            case 2 -> Major.class; // Major
            case 3 -> Integer.class; // Years
            default -> String.class;
        };
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return switch (column) {
            case 0 -> false; // ID
            default -> true;
        };
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Student student = getStudent(rowIndex);

        return switch (columnIndex) {
            case 0 -> student.getFacultyNumber();
            case 1 -> student.getFullName();
            case 2 -> student.getMajor();
            case 3 -> student.getYear();
            default -> null;
        };
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        Student student = getStudent(rowIndex);

        try {
            switch (columnIndex) {
                case 1 -> student.setFullName((String) value);
                case 2 -> student.setMajor((Major)value);
                case 3 -> student.setYear((Integer) value);
                default -> throw new IllegalStateException("Unexpected value: " + columnIndex);
            }

            if (studentsRepository.Update(student)) {
                fireTableCellUpdated(rowIndex, columnIndex);
            } else {
                JOptionPane.showMessageDialog(null, "Failed to update student in database", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Student getStudent(int row) {
        return students.get(row);
    }

    public void addStudent(Student student) {
        if (studentsRepository.Add(student)) {
            refreshStudents();
        } else {
            JOptionPane.showMessageDialog(null, "Failed to add student to database", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void removeStudent(int row) {
        Student student = getStudent(row);
        if (studentsRepository.Delete(student.getId())) {
            refreshStudents();
        } else {
            JOptionPane.showMessageDialog(null, "Failed to delete student from database", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

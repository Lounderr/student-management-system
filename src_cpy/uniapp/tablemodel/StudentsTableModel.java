package uniapp.tablemodel;

import uniapp.model.Major;
import uniapp.model.Student;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class StudentsTableModel extends AbstractTableModel {
    private final List<Major> majors;
    private final List<Student> students;

    private final String[] columnNames = {
            "Факултетен номер",
            "Име на студента",
            "Специалност",
            "Курс"
    };

    public StudentsTableModel(List<Major> majors, List<Student> students) {
        this.majors = majors;
        this.students = students;
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
        // SQL SELECT
        Student student = getStudent(rowIndex);

        return switch (columnIndex) {
            case 0 -> student.getId();
            case 1 -> student.getFullName();
            case 2 -> student.getMajor();
            case 3 -> student.getYear();
            default -> null;
        };
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        // SQL UPDATE
        Student student = getStudent(rowIndex);

        try {
            switch (columnIndex) {
                case 1 -> student.setFullName((String) value);
                case 2 -> {
//                    if (majors.contains((Major) value)) {
//                        students.get(rowIndex).setMajor((Major)value);
//                    } else
//                        throw new IllegalStateException("Major does not exist");
                    students.get(rowIndex).setMajor((Major)value);
                }
                case 3 -> student.setYear((Integer) value);
                default -> throw new IllegalStateException("Unexpected value: " + columnIndex);
            }

            fireTableCellUpdated(rowIndex, columnIndex);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Student getStudent(int row) {
        return students.get(row);
    }

    public void addStudent(Student student) {
        insertStudent(getRowCount(), student);
    }

    public void removeStudent(int row) {
        // SQL REMOVE
        students.remove(row);
        fireTableRowsDeleted(row, row);
    }

    public void insertStudent(int row, Student student) {
        // SQL ADD
        try {
            // Can be improved by transforming to a dictionary
            boolean isStudentNameUnique = students.stream().map(Student::getId).noneMatch(id -> id.equals(student.getId()));
            if (isStudentNameUnique) {
                students.add(row, student);
                fireTableRowsInserted(row, row);
            } else
                throw new Exception("Student ID is already in use");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

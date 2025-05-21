package uniapp.tablemodel;

import uniapp.model.Grade;
import uniapp.model.Major;
import uniapp.model.Student;
import uniapp.model.Subject;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;


public class GradesTableModel extends AbstractTableModel {
    private final List<Grade> grades;

    private final String[] columnNames = {
            "Предмет",
            "Оценка"
    };
    private final Student student;

    public GradesTableModel(Student student) {
        this.student = student;
        this.grades = student.getGrades();
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
            case 0 -> grades.get(rowIndex).getSubject();
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
                case 0 -> grade.setSubject((Subject) value);
                case 1 -> grades.get(rowIndex).setGrade((Integer)value);
            }

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
        grades.remove(row);

        fireTableRowsDeleted(row, row);
    }

    public void insertSubject(int row, Grade grade) {
        // SQL ADD
        try {
            // Can be improved by transforming to a dictionary
            grades.add(row, grade);
            fireTableRowsInserted(row, row);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}

package uniapp.tablemodel;

import uniapp.model.Major;
import uniapp.model.Student;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;


public class MajorsTableModel extends AbstractTableModel {
    private final StudentsTableModel studentsTableModel;
    private final List<Major> majors;
    private final List<Student> students;

    private final String[] columnNames = {
            "Специалност",
            "Години на следване",
    };

    public MajorsTableModel(StudentsTableModel studentsTableModel, List<Major> majors, List<Student> students) {
        this.studentsTableModel = studentsTableModel;
        this.majors = majors;
        this.students = students;
    }

    @Override
    public int getRowCount() {
        return majors.size();
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
            case 0 -> String.class;
            case 1 -> Integer.class; // Total Years
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
        Major major = getMajor(rowIndex);

        return switch (columnIndex) {
            case 0 -> major.getName();
            case 1 -> major.getTotalYears();
            default -> null;
        };
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        // SQL UPDATE
        Major major = getMajor(rowIndex);

        try {
            switch (columnIndex) {
                case 0:
                    major.setName((String) value);
                    // WHEN A VALUE IS SET, YOU MUST CHANGE THE VALUES FOR STUDENTS TABLE MODEL TOO
                    studentsTableModel.fireTableDataChanged();

                    break;
                case 1: {
                    major.setTotalYears(Integer.parseInt((String) value));
                    break;
                }
                default:
                    throw new IndexOutOfBoundsException();
            }

            fireTableCellUpdated(rowIndex, columnIndex);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Major getMajor(int row) {
        return majors.get(row);
    }

    public void addMajor(Major major) {
        insertMajor(getRowCount(), major);
    }

    public void removeMajor(int row) {
        // SQL REMOVE
        var majorToRemove = majors.get(row);

        if (students.stream().noneMatch(s -> s.getMajor() == majorToRemove)) {
            majors.remove(row);
            fireTableRowsUpdated(row, row);
        } else {
            JOptionPane.showMessageDialog(null,
                    "Cannot remove major - already in use by students.\n" +
                            "To remove it, change the students' major",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public void insertMajor(int row, Major major) {
        // SQL ADD
        try {
            // Can be improved by transforming majors into a dictionary
            boolean isMajorNameUnique = majors.stream().map(Major::getName).noneMatch(n -> n.equalsIgnoreCase(major.getName()));
            if (isMajorNameUnique) {
                majors.add(row, major);
                fireTableRowsInserted(row, row);
            } else
                throw new Exception("Major already exists");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}

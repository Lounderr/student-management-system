package tablemodel;

import data.model.*;
import data.model.repository.impl.*;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class MajorsTableModel extends AbstractTableModel {
    private final StudentsTableModel studentsTableModel;
    private final MajorsRepository majorsRepository;
    private final StudentsRepository studentsRepository;
    private List<Major> majors;

    private final String[] columnNames = {
            "Специалност",
            "Години на следване",
    };

    public MajorsTableModel(StudentsTableModel studentsTableModel, MajorsRepository majorsRepository, StudentsRepository studentsRepository) {
        this.studentsTableModel = studentsTableModel;
        this.majorsRepository = majorsRepository;
        this.studentsRepository = studentsRepository;
        refreshMajors();
    }

    public void refreshMajors() {
        this.majors = majorsRepository.All();
        fireTableDataChanged();
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
        Major major = getMajor(rowIndex);

        return switch (columnIndex) {
            case 0 -> major.getName();
            case 1 -> major.getTotalYears();
            default -> null;
        };
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        Major major = getMajor(rowIndex);

        try {
            switch (columnIndex) {
                case 0:
                    major.setName((String) value);
                    break;
                case 1: {
                    major.setTotalYears((Integer) value);
                    break;
                }
                default:
                    throw new IndexOutOfBoundsException();
            }

            if (majorsRepository.Update(major)) {
                fireTableCellUpdated(rowIndex, columnIndex);
                studentsTableModel.refreshStudents();
            } else {
                JOptionPane.showMessageDialog(null, "Failed to update major in database", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Major getMajor(int row) {
        return majors.get(row);
    }

    public void addMajor(Major major) {
        if (majorsRepository.Add(major)) {
            refreshMajors();
        } else {
            JOptionPane.showMessageDialog(null, "Failed to add major to database", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void removeMajor(int row) {
        Major majorToRemove = getMajor(row);
        List<Student> students = studentsRepository.All();
        
        if (students.stream().noneMatch(s -> s.getMajor().getId() == majorToRemove.getId())) {
            if (majorsRepository.Delete(majorToRemove.getId())) {
                refreshMajors();
            } else {
                JOptionPane.showMessageDialog(null, "Failed to delete major from database", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null,
                    "Cannot remove major - already in use by students.\n" +
                            "To remove it, change the students' major",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}

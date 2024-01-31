package unisystem.tablemodel;

import unisystem.model.Major;
import tools.Dialog;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class MajorsTableModel extends AbstractTableModel {
    private final String[] columnNames = {
            "Специалност",
            "Години на следване",
    };

    private final List<Major> majors;

    public MajorsTableModel() {
        majors = new ArrayList<>();
    }

    public MajorsTableModel(List<Major> majors) {
        this.majors = new ArrayList<>(majors);
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
    public int getRowCount() {
        return majors.size();
    }

    @Override
    public Class getColumnClass(int column) {
        switch (column) {
            case 1:
                return Integer.class; // Total Years
            default:
                return String.class;
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        switch (column) {
            default:
                return true;
        }
    }

    @Override
    public Object getValueAt(int row, int column) {
        Major major = getMajor(row);

        return switch (column) {
            case 0 -> major.getName();
            case 1 -> major.getTotalYears();
            default -> null;
        };
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        Major major = getMajor(row);

        try {
            switch (column) {
                case 0:
                    major.setName((String) value);
                    break;
                case 1: {
                    major.setTotalYears(Integer.parseInt((String) value));
                    break;
                }
                default:
                    throw new IndexOutOfBoundsException();
            }
        } catch (Exception ex) {
            Dialog.showError(majorsDialog, ex);
        }

        fireTableCellUpdated(row, column);
    }

    public Major getMajor(int row) {
        return majors.get(row);
    }

    public void addMajor(Major major) {
        insertMajor(getRowCount(), major);
    }

    public void insertMajor(int row, Major major) {
        try {
            majors.add(row, major);
            fireTableRowsInserted(row, row);

        } catch (Exception ex) {
            Dialog.showError(majorsDialog, ex);
        }
    }

    public void removeMajor(int row) {
        majors.remove(row);
        fireTableRowsDeleted(row, row);
    }

    public List<Major> getMajors() {
        return majors.toList();
    }

//
//    private final ManageMajorsDialog manageMajorsDialog;
//
//    public MajorsTableModel(ManageMajorsDialog manageMajorsDialog) {
//        super(new String[]{"Специалност", "Години"}, 0);
//        this.manageMajorsDialog = manageMajorsDialog;
//    }
//
//    @Override
//    public void setValueAt(Object value, int row, int col) {
//        var major = (Major) this.getValueAt(row, 0);
//        try {
//            switch (col) {
//                case 0: {
//                    String name = (String) value;
//                    major.setName(name);
//                    break;
//                }
//
//                case 1: {
//                    int totalYears = (int) value;
//                    major.setTotalYears(totalYears);
//                    break;
//                }
//            }
//
//            super.setValueAt(value, row, col);
//        } catch (Exception ex) {
//            Dialog.showError(manageMajorsDialog, ex);
//        }
//    }
//
//    public void addRecord(String majorName, Integer totalYears) {
//        try {
//            Major major = new Major(majorName, totalYears);
//            App.getMajors().add(major);
//
//            Object[] record = {major, totalYears};
//            super.addRow(record);
//        } catch (Exception ex) {
//            Dialog.showError(manageMajorsDialog, ex);
//        }
//    }
//
//    public void removeRecord(int modelRow) {
//        Major major = (Major) this.getValueAt(modelRow, 0);
//
//        if (App.getStudents().stream().noneMatch(s -> s.getMajor() == major)) {
//            App.getMajors().remove(major.getName());
//            this.removeRow(modelRow);
//        } else {
//            Dialog.show(manageMajorsDialog, "There are students with this major. To remove it, change the major of those students");
//        }
//    }
}

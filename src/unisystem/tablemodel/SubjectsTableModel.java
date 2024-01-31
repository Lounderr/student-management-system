package unisystem.tablemodel;

import unisystem.model.Major;
import unisystem.model.Subject;
import tools.Dialog;
import tools.UniqueList;
import unisystem.view.SubjectsDialog;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class SubjectsTableModel extends AbstractTableModel {
    private final String[] columnNames = {
            "Предмети",
    };

    private final SubjectsDialog subjectsDialog;

    private final Major major;
    private final UniqueList<Subject> subjects;

    public SubjectsTableModel(SubjectsDialog subjectsDialog, Major major) {
        this.subjectsDialog = subjectsDialog;
        this.major = major;
        subjects = new UniqueList<>();
    }

    public SubjectsTableModel(SubjectsDialog subjectsDialog, Major major, List<Subject> subjects) {
        this.subjectsDialog = subjectsDialog;
        this.major = major;
        this.subjects = new UniqueList<>(subjects);
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
        return subjects.size();
    }

    @Override
    public Class getColumnClass(int column) {
        switch (column) {
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
        Subject subject = getSubject(row);

        return switch (column) {
            case 0 -> subject;
            default -> null;
        };
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        Subject subject = getSubject(row);

        try {
            switch (column) {
                case 0:
                    if (major.getSubjects().contains(subject))
                    subject.setName((String)value);
                    break;
                default:
                    throw new IndexOutOfBoundsException();
            }
        } catch (Exception ex) {
            Dialog.showError(subjectsDialog, ex);
        }

        fireTableCellUpdated(row, column);
    }

    public Subject getSubject(int row) {
        return subjects.get(row);
    }

    public void addSubject(Subject subject) {
        insertSubject(getRowCount(), subject);
    }

    public void insertSubject(int row, Subject subject) {
        try {
            subjects.add(row, subject);
            fireTableRowsInserted(row, row);
        } catch (Exception ex) {
            Dialog.showError(subjectsDialog, ex);
        }
    }

    public void removeMajor(int row) {
        subjects.remove(row);
        fireTableRowsDeleted(row, row);
    }

    public List<Subject> getMajors() {
        return subjects.toList();
    }
}

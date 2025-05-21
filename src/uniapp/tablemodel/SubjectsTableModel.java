package uniapp.tablemodel;

import uniapp.model.Grade;
import uniapp.model.Major;
import uniapp.model.Student;
import uniapp.model.Subject;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.Arrays;
import java.util.List;


public class SubjectsTableModel extends AbstractTableModel {
    private final List<Subject> subjects;
    private final List<Student> students;
    private final Major major;

    private final String[] columnNames = {
            "Предмет",
    };

    public SubjectsTableModel(Major major, List<Student> students) {
        this.major = major;
        this.subjects = major.getSubjects();
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
        // SQL REMOVE

        int confirmation = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to delete this subject?\n" +
                        "Deleting this subject will also delete all related grades.",
                "Confirmation", JOptionPane.OK_CANCEL_OPTION);

        if (confirmation == JOptionPane.OK_OPTION)
        {
            // ON SUBJECT DELETE, DELETE GRADES
            var subject = subjects.get(row);
            for (Student student : students) {
                List<Grade> grades = student.getGrades();

                for (int i = 0; i < grades.size(); i++) {
                    if (grades.get(i).getSubject() == subject && student.getMajor() == this.major)
                        grades.remove(grades.get(i));
                }
            }

            subjects.remove(row);
            fireTableRowsDeleted(row, row);
        }
    }

    public void insertSubject(int row, Subject subject) {
        // SQL ADD
        try {
            // Can be improved by transforming to a dictionary
            boolean isSubjectNameUnique = subjects.stream().map(Subject::getName).noneMatch(n -> n.equalsIgnoreCase(subject.getName()));
            if (isSubjectNameUnique)
            {
                subjects.add(row, subject);
                fireTableRowsInserted(row, row);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}

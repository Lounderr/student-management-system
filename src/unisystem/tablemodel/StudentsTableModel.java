package unisystem.tablemodel;

import unisystem.model.Major;
import unisystem.model.Student;
import tools.Dialog;
import tools.UniqueList;
import unisystem.view.MainFrame;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public final class StudentsTableModel extends AbstractTableModel {

    private final String[] columnNames = {
            "Факултетен номер",
            "Име на студента",
            "Специалност",
            "Курс"
    };

    private final MainFrame mainFrame;

    private final UniqueList<Student> students;
    private final MajorsTableModel majorsTableModel;

    public StudentsTableModel(MainFrame mainFrame, MajorsTableModel majorsTableModel) {
        this.mainFrame = mainFrame;
        students = new UniqueList<>();
        this.majorsTableModel = majorsTableModel;
    }

    public StudentsTableModel(MainFrame mainFrame, List<Student> students, MajorsTableModel majorsTableModel) {
        this.mainFrame = mainFrame;
        this.students = new UniqueList<>(students);
        this.majorsTableModel = majorsTableModel;
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
        return students.size();
    }

    @Override
    public Class getColumnClass(int column) {
        switch (column) {
            case 2:
                return Major.class; // Major
            case 3:
                return Integer.class; // Year
            default:
                return String.class;
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        switch (column) {
//            case 0:
//                return false; // ID
            default:
                return true;
        }
    }

    @Override
    public Object getValueAt(int row, int column) {
        Student student = getStudent(row);

        switch (column) {
            case 0:
                return student.getId();
            case 1:
                return student.getName();
            case 2:
                return student.getMajor();
            case 3:
                return student.getYear();
            default:
                return null;
        }
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        Student student = getStudent(row);

        try {
            switch (column) {
                case 1:
                    student.setName((String) value);
                    break;
                case 2: {
//                    // TODO: get major from from shared Majors list
//
//                    if (majorsTableModel.containsMajor((String)value))
//                        student.setMajor(major);
                    break;
                }
                case 3:
                    student.setYear(Integer.parseInt((String) value));
                    break;
                default:
                    throw new IndexOutOfBoundsException();
            }
        } catch (Exception ex) {
            Dialog.showError(mainFrame, ex);
        }

        fireTableCellUpdated(row, column);
    }

    public Student getStudent(int row) {
        return students.get(row);
    }

    public void addStudent(Student student) {
        insertStudent(getRowCount(), student);
    }

    public void insertStudent(int row, Student student) {

        try {
            students.add(row, student);
            fireTableRowsInserted(row, row);
        } catch (Exception ex) {
            Dialog.showError(mainFrame, ex);
        }
    }

    public void removePerson(int row) {
        students.remove(row);
        fireTableRowsDeleted(row, row);
    }

    public List<Student> getStudents() {
        return students.toList();
    }
}

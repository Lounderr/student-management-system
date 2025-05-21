package uniapp.gui;

import uniapp.model.Major;
import uniapp.model.Student;
import uniapp.tablemodel.StudentsTableModel;
import utility.TableOperations;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {
    private final StudentsTableModel studentsTableModel;
    private final List<Major> majors;
    private final List<Student> students;
    private JPanel contentPane;
    private JTextField studentNameField;
    private JButton addStudentBtn;
    public JTable studentsTable;
    private JTextField searchField;
    private JButton searchBtn;
    private JButton viewGradesBtn;
    private JButton averageBtn;
    private JButton removeBtn;
    private JTextField studentIdField;
    private JComboBox<Integer> studentYearComboBox;
    private JComboBox<Major> studentMajorComboBox;
    private JButton manageMajors;

    public MainFrame(List<Major> majors, List<Student> students) {
        this.majors = majors;
        this.students = students;

        studentsTableModel = new StudentsTableModel(majors, students);
        studentsTable.setModel(studentsTableModel);

        TableOperations.createStandardRowSorter(studentsTable);
        TableOperations.centerColumnClass(studentsTable, Integer.class);

        resizeColumnWidth();


        InitAddStudentComboBoxItems();
        TableOperations.createInlineTableComboBox(studentsTable, 2, majors);

        addFrameEventListeners();

        setTitle("Student Management System by Georgi Georgiev | 2301321048");
        setSize(1000, 600);
        setContentPane(contentPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void resizeColumnWidth() {
        final TableColumnModel columnModel = studentsTable.getColumnModel();
        for (int column = 0; column < studentsTable.getColumnCount(); column++) {
            int width = 35; // Min width
            for (int row = 0; row < studentsTable.getRowCount(); row++) {
                TableCellRenderer renderer = studentsTable.getCellRenderer(row, column);
                Component comp = studentsTable.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width +1 , width);
            }
            if(width > 300)
                width=300;
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }

    private void InitAddStudentComboBoxItems() {
        for (Major major : majors) {
            studentMajorComboBox.addItem(major);
        }
        studentMajorComboBox.setSelectedItem(null);
    }

    private void addFrameEventListeners() {
        viewGradesBtn.addActionListener((e) -> {
            int row = studentsTable.getSelectedRow();
            if (row >= 0) {
                int modelRow = studentsTable.convertRowIndexToModel(row);
                Student selectedStudent = students.get(modelRow);
                new GradesFrame(this, selectedStudent);
            }
        });

        studentMajorComboBox.addActionListener(e -> {
            Major selectedMajor = (Major) studentMajorComboBox.getSelectedItem();
            if (selectedMajor != null) {
                studentYearComboBox.removeAllItems();

                for (int i = 1; i <= selectedMajor.getTotalYears(); i++) {
                    studentYearComboBox.addItem(i);
                }
            }
        });

        addStudentBtn.addActionListener(e -> {
            String id = studentIdField.getText();
            String name = studentNameField.getText();
            Major major = (Major) studentMajorComboBox.getSelectedItem();
            Integer year = (Integer) studentYearComboBox.getSelectedItem();

            try {
                Student student = new Student(id, name, major, year);
                studentsTableModel.addStudent(student);

                studentNameField.setText("");
                studentIdField.setText("");
                studentMajorComboBox.setSelectedItem(null);
                studentYearComboBox.setSelectedItem(null);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        removeBtn.addActionListener(e -> {
            int selectedRow = studentsTable.getSelectedRow();

            if (studentsTable.getSelectedRow() >= 0) {
                int modelRow = studentsTable.convertRowIndexToModel(selectedRow);
                studentsTableModel.removeStudent(modelRow);
            }
        });

        // TODO: Replace all list.get() methods with table-model.get()

        averageBtn.addActionListener(e -> {
            new AvgReportFrame(this, majors, students);
        });

        manageMajors.addActionListener(e -> {
            new MajorsFrame(this, majors, students);
        });

        searchField.addActionListener((e) -> searchRows());
        searchBtn.addActionListener((e) -> searchRows());
    }

    private void searchRows() {
        RowFilter<StudentsTableModel, Object> rf = null;
        //If current expression doesn't parse, don't update.
        try {
            rf = RowFilter.regexFilter(searchField.getText(), 0);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        var sorter = (TableRowSorter<StudentsTableModel>) studentsTable.getRowSorter();

        sorter.setRowFilter(rf);
    }
}

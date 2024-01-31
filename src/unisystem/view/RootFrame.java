package unisystem.view;

import controller.MajorsTableModel;
import controller.StudentsTableModel;
import unisystem.App;
import unisystem.model.*;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.util.ArrayList;
import java.util.List;

public class RootFrame extends JFrame {
    private StudentsTableModel studentsTableModel;
    private MajorsTableModel majorsTableModel;

    private JPanel contentPane;
    private JTextField studentNameField;
    private JButton addStudentBtn;
    private JTable studentsTable;
    private JTextField searchField;
    private JButton searchBtn;
    private JButton viewGradesBtn;
    private JButton averageBtn;
    private JButton removeBtn;
    private JTextField studentIdField;
    private JComboBox<Integer> studentYearComboBox;
    private JComboBox<Major> studentMajorComboBox;
    private JButton manageMajors;

    public RootFrame() {
        studentsTableModel = new StudentsTableModel(this, majorsTableModel);
        studentsTable.setModel(studentsTableModel);

        CreateTableRowSorter();
        SeedStudentsData();
        InitAddStudentComboBoxItems();
        AddFrameEventListeners();

        setTitle("Student Management System by Georgi Georgiev | 2301321048");
        setSize(1000, 600);
        setContentPane(contentPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void CreateTableRowSorter() {
        List<RowSorter.SortKey> sortKeys = new ArrayList<>(1);
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        studentsTable.getRowSorter().setSortKeys(sortKeys);
    }

    private void InitAddStudentComboBoxItems() {
        for (Major major : App.getMajors()) {
            studentMajorComboBox.addItem(major);
        }
        studentMajorComboBox.setSelectedItem(null);
    }

    private void SeedStudentsData() {
        for (var student : studentsTableModel.getStudents()) {
            studentsTableModel.addStudent(student);
        }
    }

    private void AddFrameEventListeners() {
        viewGradesBtn.addActionListener((e) -> {
            int row = studentsTable.getSelectedRow();
            if (row >= 0) {
                int modelRow = studentsTable.convertRowIndexToModel(row);

                Student selectedStudent = (Student) studentsTableModel.getValueAt(modelRow, 0);
                List<Grade> grades = selectedStudent.getGrades();

                GradesDialog gradesDialog = new GradesDialog(this, selectedStudent, grades);
            }
        });

        studentMajorComboBox.addActionListener(e -> {
            Major selectedMajor = (Major)studentMajorComboBox.getSelectedItem();
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

            studentsTableModel.addRecord(id, name, major, year);

            studentNameField.setText("");
            studentIdField.setText("");
            studentMajorComboBox.setSelectedItem(null);
            studentYearComboBox.setSelectedItem(null);
        });

        removeBtn.addActionListener(e -> {
            int row = studentsTable.getSelectedRow();
            if (row >= 0) {
                studentsTableModel.removeRecord(studentsTable.convertRowIndexToModel(row));
            }
        });

        averageBtn.addActionListener(e -> {
            AverageReportDialog averageReportDialog = new AverageReportDialog(this);
        });

        manageMajors.addActionListener(e -> {
            ManageMajorsDialog manageMajorsFrame = new ManageMajorsDialog(this);
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

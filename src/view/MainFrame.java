package view;

import data.model.*;
import data.model.repository.impl.*;
import tablemodel.*;
import utility.TableOperations;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class MainFrame extends JFrame {
    private final StudentsTableModel studentsTableModel;
    private final StudentsRepository studentsRepository;
    private final MajorsRepository majorsRepository;
    private final GradesRepository gradesRepository;
    private final SubjectsRepository subjectsRepository;
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

    public MainFrame(MajorsRepository majorsRepository, StudentsRepository studentsRepository, GradesRepository gradesRepository, SubjectsRepository subjectsRepository) {
        this.majorsRepository = majorsRepository;
        this.studentsRepository = studentsRepository;
        this.gradesRepository = gradesRepository;
        this.subjectsRepository = subjectsRepository;

        studentsTableModel = new StudentsTableModel(studentsRepository, majorsRepository);
        studentsTable.setModel(studentsTableModel);

        TableOperations.createStandardRowSorter(studentsTable);
        TableOperations.centerColumnClass(studentsTable, Integer.class);

        resizeColumnWidth();

        InitAddStudentComboBoxItems();
        TableOperations.createInlineTableComboBox(studentsTable, 2, majorsRepository.All());

        addFrameEventListeners();

        setTitle("Student Management System by Georgi Georgiev | 2301321048");
        setSize(1000, 600);
        setContentPane(contentPane);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
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
        for (Major major : majorsRepository.All()) {
            studentMajorComboBox.addItem(major);
        }
        studentMajorComboBox.setSelectedItem(null);
    }

    private void addFrameEventListeners() {
        viewGradesBtn.addActionListener((e) -> {
            int selectedRow = studentsTable.getSelectedRow();

            if (selectedRow >= 0) {
                var modelRow = studentsTable.convertRowIndexToModel(selectedRow);
                var student = studentsTableModel.getStudent(modelRow);
                new GradesFrame(this, student, gradesRepository, subjectsRepository, majorsRepository);
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
                Student student = new Student(0, id, name, major, year);
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

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Моля, изберете студент", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            var student = studentsTableModel.getStudent(selectedRow);

            int result = JOptionPane.showConfirmDialog(this, "Сигурни ли сте, че искате да изтриете " + student.getFullName() + "?", "Confirm", JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                try {
                    studentsRepository.Delete(student.getId());
                    studentsTableModel.refreshStudents();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        averageBtn.addActionListener(e -> {
            new AvgReportFrame(this, majorsRepository, studentsRepository, gradesRepository);
        });

        manageMajors.addActionListener(e -> {
            new MajorsFrame(this, majorsRepository, studentsRepository, gradesRepository);
        });

        searchField.addActionListener((e) -> searchRows());
        searchBtn.addActionListener((e) -> searchRows());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int result = JOptionPane.showConfirmDialog(MainFrame.this, "Сигурни ли сте, че искате да излезете?", "Confirm", JOptionPane.YES_NO_OPTION);

                if (result == JOptionPane.YES_OPTION) {
                    dispose();
                    System.exit(0);
                }
            }
        });
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

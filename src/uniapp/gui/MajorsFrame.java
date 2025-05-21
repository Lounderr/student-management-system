package uniapp.gui;

import javax.swing.*;

import uniapp.model.Major;
import uniapp.model.Student;
import uniapp.tablemodel.*;
import utility.TableOperations;

import java.util.List;

public class MajorsFrame extends JDialog {
    private final MajorsTableModel majorsTableModel;
    private final List<Major> majors;
    private JPanel contentPane;
    private JTable majorsTable;
    private JTextField majorField;
    private JSpinner yearsSpinner;
    private JButton addBtn;
    private JButton subjectsBtn;
    private JButton removeBtn;
    private JButton doneBtn;
    private List<Student> students;

    public MajorsFrame(MainFrame owner, List<Major> majors, List<Student> students) {
        this.majors = majors;
        this.students = students;

        StudentsTableModel studentsTableModel = (StudentsTableModel)owner.studentsTable.getModel();

        majorsTableModel = new MajorsTableModel(studentsTableModel, majors, students);
        majorsTable.setModel(majorsTableModel);

        TableOperations.createStandardRowSorter(majorsTable);
        TableOperations.centerColumnClass(majorsTable, Integer.class);

        addFrameEventListeners();

        yearsSpinner.setValue(1);

        setTitle("Специалности");
        setContentPane(contentPane);
        setModal(true);
        pack();
        setLocationRelativeTo(owner);
        setVisible(true);
    }

    private void addFrameEventListeners() {
        addBtn.addActionListener(e -> {
            String majorName = majorField.getText();
            int totalYears = (Integer) yearsSpinner.getValue();

            try {
                Major major = new Major(majorName, totalYears);
                majorsTableModel.addMajor(major);

                majorField.setText("");
                yearsSpinner.setValue(1);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        removeBtn.addActionListener(e -> {
            int selectedRow = majorsTable.getSelectedRow();

            if (majorsTable.getSelectedRow() >= 0) {
                int modelRow = majorsTable.convertRowIndexToModel(selectedRow);
                majorsTableModel.removeMajor(modelRow);
            }
        });

        subjectsBtn.addActionListener(e -> {
            int selectedRow = majorsTable.getSelectedRow();

            if (majorsTable.getSelectedRow() >= 0) {
                int modelRow = majorsTable.convertRowIndexToModel(selectedRow);

                new SubjectsFrame(this, majors.get(modelRow), students);
            }
        });


        doneBtn.addActionListener((e) -> dispose());
    }
}

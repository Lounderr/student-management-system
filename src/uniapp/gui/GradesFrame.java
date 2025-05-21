package uniapp.gui;


import uniapp.model.Grade;
import uniapp.model.Student;
import uniapp.model.Subject;
import uniapp.tablemodel.GradesTableModel;
import utility.TableOperations;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GradesFrame extends JDialog {
    private JPanel contentPane;
    private JTable gradesTable;
    private JButton doneBtn;
    private JButton removeBtn;
    private JButton addGradeBtn;
    private JComboBox<Subject> subjectComboBox;
    private ButtonGroup gradeBtnGroup;

    private final Student student;
    private final List<Grade> grades;
    private GradesTableModel gradesTableModel;

    public GradesFrame(Component owner, Student student) {
        this.student = student;
        this.grades = student.getGrades();

        gradesTableModel = new GradesTableModel(student);
        gradesTable.setModel(gradesTableModel);

        initAddGradeComboBox();

        TableOperations.createInlineTableComboBox(gradesTable, 0, student.getMajor().getSubjects());
        TableOperations.createStandardRowSorter(gradesTable);
        TableOperations.centerColumnClass(gradesTable, Integer.class);

        addFrameEventListeners();

        setTitle("Оценките на " + student.getId());
        setContentPane(contentPane);
        setModal(true);
        pack();
        setLocationRelativeTo(owner);
        setVisible(true);
    }

    private void initAddGradeComboBox() {
        for (var subject : student.getMajor().getSubjects()) {
            subjectComboBox.addItem(subject);
        }
        subjectComboBox.setSelectedItem(null);
    }

    private void addFrameEventListeners() {
        addGradeBtn.addActionListener((e) -> {
            Subject subject = (Subject) subjectComboBox.getSelectedItem();

            int selectedGrade = Integer.parseInt(gradeBtnGroup.getSelection().getActionCommand());

            try {
                Grade grade = new Grade(subject, selectedGrade);
                gradesTableModel.addGrade(grade);

                subjectComboBox.setSelectedItem(null);
                gradeBtnGroup.clearSelection();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        removeBtn.addActionListener((e) -> {
            int selectedRow = gradesTable.getSelectedRow();

            if (selectedRow >= 0) {
                var modelRow = gradesTable.convertRowIndexToModel(selectedRow);

                gradesTableModel.removeGrade(modelRow);
            }
        });

        subjectComboBox.addActionListener(e -> gradeBtnGroup.clearSelection());

        doneBtn.addActionListener((e) -> dispose());
    }
}
package view;

import data.model.*;
import data.model.repository.impl.*;
import tablemodel.*;
import utility.TableOperations;

import javax.swing.*;
import java.awt.Component;
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
    private final GradesRepository gradesRepository;
    private final SubjectsRepository subjectsRepository;
    private final MajorsRepository majorsRepository;
    private GradesTableModel gradesTableModel;

    public GradesFrame(Component owner, Student student, GradesRepository gradesRepository, SubjectsRepository subjectsRepository, MajorsRepository majorsRepository) {
        this.student = student;
        this.gradesRepository = gradesRepository;
        this.subjectsRepository = subjectsRepository;
        this.majorsRepository = majorsRepository;

        gradesTableModel = new GradesTableModel(student, gradesRepository, subjectsRepository);
        gradesTable.setModel(gradesTableModel);

        initAddGradeComboBox();

        TableOperations.createInlineTableComboBox(gradesTable, 0, majorsRepository.GetSubjectsForMajor(student.getMajor().getId()));
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
        for (var subject : majorsRepository.GetSubjectsForMajor(student.getMajor().getId())) {
            subjectComboBox.addItem(subject);
        }
        subjectComboBox.setSelectedItem(null);
    }

    private void addFrameEventListeners() {
        addGradeBtn.addActionListener((e) -> {
            Subject subject = (Subject) subjectComboBox.getSelectedItem();

            int selectedGrade = Integer.parseInt(gradeBtnGroup.getSelection().getActionCommand());

            try {
                Grade grade = new Grade(0, subject.getId(), student.getId(), selectedGrade);
                gradesRepository.Add(grade);
                gradesTableModel.refreshGrades();

                subjectComboBox.setSelectedItem(null);
                gradeBtnGroup.clearSelection();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        removeBtn.addActionListener((e) -> {
            int selectedRow = gradesTable.getSelectedRow();

            if (selectedRow >= 0) {
                var modelRow = gradesTable.convertRowIndexToModel(selectedRow);
                var grade = gradesTableModel.getGrade(modelRow);
                gradesRepository.Delete(grade.getId());
                gradesTableModel.refreshGrades();
            }
        });

        subjectComboBox.addActionListener(e -> gradeBtnGroup.clearSelection());

        doneBtn.addActionListener((e) -> dispose());
    }
}
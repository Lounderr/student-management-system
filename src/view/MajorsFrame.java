package view;

import javax.swing.*;

import data.model.*;
import data.model.repository.impl.*;
import tablemodel.*;
import utility.TableOperations;

import java.util.List;

public class MajorsFrame extends JDialog {
    private final MajorsTableModel majorsTableModel;
    private final MajorsRepository majorsRepository;
    private final StudentsRepository studentsRepository;
    private final GradesRepository gradesRepository;
    private JPanel contentPane;
    private JTable majorsTable;
    private JTextField majorField;
    private JSpinner yearsSpinner;
    private JButton addBtn;
    private JButton subjectsBtn;
    private JButton removeBtn;
    private JButton doneBtn;

    public MajorsFrame(MainFrame owner, MajorsRepository majorsRepository, StudentsRepository studentsRepository, GradesRepository gradesRepository) {
        this.majorsRepository = majorsRepository;
        this.studentsRepository = studentsRepository;
        this.gradesRepository = gradesRepository;

        StudentsTableModel studentsTableModel = (StudentsTableModel)owner.studentsTable.getModel();

        majorsTableModel = new MajorsTableModel(studentsTableModel, majorsRepository.All(), studentsRepository.All());
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
                Major major = new Major(0, majorName, totalYears);
                if (majorsRepository.Add(major)) {
                    majorsTableModel.addMajor(major);
                    majorField.setText("");
                    yearsSpinner.setValue(1);
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to add major to database", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        removeBtn.addActionListener(e -> {
            int selectedRow = majorsTable.getSelectedRow();

            if (majorsTable.getSelectedRow() >= 0) {
                int modelRow = majorsTable.convertRowIndexToModel(selectedRow);
                Major majorToRemove = majorsRepository.All().get(modelRow);
                if (majorsRepository.Delete(majorToRemove.getId())) {
                    majorsTableModel.removeMajor(modelRow);
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to remove major from database", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        subjectsBtn.addActionListener(e -> {
            int selectedRow = majorsTable.getSelectedRow();

            if (majorsTable.getSelectedRow() >= 0) {
                int modelRow = majorsTable.convertRowIndexToModel(selectedRow);
                new SubjectsFrame(this, majorsRepository.All().get(modelRow), studentsRepository.All(), majorsRepository, gradesRepository);
            }
        });

        doneBtn.addActionListener((e) -> dispose());
    }
}

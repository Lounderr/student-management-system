package unisystem.view;

import controller.MajorsTableModel;
import unisystem.App;
import unisystem.model.Major;
import tools.Dialog;

import javax.swing.*;

public class ManageMajorsDialog extends JDialog {
    private final MajorsTableModel majorsTableModel;
    private JPanel contentPane;
    private JTable majorsTable;
    private JTextField majorField;
    private JSpinner yearsSpinner;
    private JButton addBtn;
    private JButton subjectsBtn;
    private JButton removeBtn;
    private JButton doneBtn;

    public ManageMajorsDialog(RootFrame ownerFrame) {
        majorsTableModel = new MajorsTableModel(this);
        majorsTable.setModel(majorsTableModel);

        InsertExistingMajors();

        AddFrameEventListeners();

        yearsSpinner.setValue(1);

        setTitle("Специалности");
        setContentPane(contentPane);
        setModal(true);
        pack();
        setLocationRelativeTo(ownerFrame);
        setVisible(true);
    }

    private void InsertExistingMajors() {
        for(var major : App.getMajors())
        {
            majorsTableModel.addRow(new Object[] {major, major.getTotalYears()});
        }
    }

    private void AddFrameEventListeners() {
        addBtn.addActionListener(e -> {

            try {
                String majorName = majorField.getText();
                int totalYears = (Integer) yearsSpinner.getValue();

                majorsTableModel.addRecord(majorName, totalYears);

                majorField.setText("");
                yearsSpinner.setValue(1);
            } catch (Exception ex) {
                Dialog.showError(this, ex);
            }
        });

        removeBtn.addActionListener(e -> {
            int selectedRow = majorsTable.getSelectedRow();

            if (majorsTable.getSelectedRow() >= 0) {
                int modelRow = majorsTable.convertRowIndexToModel(selectedRow);
                majorsTableModel.removeRecord(modelRow);
            }
        });

        subjectsBtn.addActionListener(e -> {
            int selectedRow = majorsTable.getSelectedRow();
            if (selectedRow >= 0)
            {
                Major major = (Major) majorsTableModel.getValueAt(selectedRow, 0);

                SubjectsDialog subjectsDialog = new SubjectsDialog(this, major);
            }

        });

        doneBtn.addActionListener((e) -> dispose());
    }
}

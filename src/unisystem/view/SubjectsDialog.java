package unisystem.view;

import controller.SubjectsTableModel;
import unisystem.model.Major;
import tools.Dialog;

import javax.swing.*;

public class SubjectsDialog extends JDialog {
    private JPanel contentPane;
    private JButton doneBtn;
    private JTable subjectsTable;
    private JTextField subjectField;
    private JButton addBtn;
    private JButton removeBtn;
    private final SubjectsTableModel subjectsTableModel;

    public SubjectsDialog(ManageMajorsDialog ownerFrame, Major major) {
        subjectsTableModel = new SubjectsTableModel(this, major);
        subjectsTable.setModel(subjectsTableModel);

        getSubjectsForMajor(major);
        AddFrameEventListeners();

        setTitle("Предмети");
        setContentPane(contentPane);
        setModal(true);
        pack();
        setLocationRelativeTo(ownerFrame);
        setVisible(true);
    }

    private void getSubjectsForMajor(Major major) {
        for(var subject : major.getSubjects())
        {
            subjectsTableModel.addRow(new Object[] {subject});
        }
    }

    private void AddFrameEventListeners() {
        addBtn.addActionListener(e -> {

            try {
                String subjectName = subjectField.getText();

                subjectsTableModel.addRecord(subjectName);

                subjectField.setText("");
            } catch (Exception ex) {
                Dialog.showError(this, ex);
            }
        });

        removeBtn.addActionListener((e) -> {
            int selectedRow = subjectsTable.getSelectedRow();

            if (subjectsTable.getSelectedRow() >= 0) {
                int modelRow = subjectsTable.convertRowIndexToModel(selectedRow);
                subjectsTableModel.removeRecord(modelRow);
            }
        });

        doneBtn.addActionListener((e) -> dispose());
    }
}

package uniapp.gui;


import uniapp.model.Major;
import uniapp.model.Student;
import uniapp.model.Subject;
import uniapp.tablemodel.SubjectsTableModel;
import utility.TableOperations;

import javax.swing.*;
import java.awt.Component;
import java.util.List;

public class SubjectsFrame extends JDialog {
    private JPanel contentPane;
    private JButton doneBtn;
    private JTable subjectsTable;
    private JTextField subjectField;
    private JButton addBtn;
    private JButton removeBtn;
    private final SubjectsTableModel subjectsTableModel;

    public SubjectsFrame(Component owner, Major major, List<Student> students) {
        subjectsTableModel = new SubjectsTableModel(major, students);
        subjectsTable.setModel(subjectsTableModel);

        TableOperations.createStandardRowSorter(subjectsTable);

        addFrameEventListeners();

        setTitle("Предмети");
        setContentPane(contentPane);
        setModal(true);
        pack();
        setLocationRelativeTo(owner);
        setVisible(true);
    }

    // Extract so it is reused by all frames


    private void addFrameEventListeners() {
        addBtn.addActionListener(e -> {
            String subjectName = subjectField.getText();

            try {
                Subject subject = new Subject(subjectName);
                subjectsTableModel.addSubject(subject);
                subjectField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        removeBtn.addActionListener((e) -> {
            int selectedRow = subjectsTable.getSelectedRow();

            if (subjectsTable.getSelectedRow() >= 0) {
                int modelRow = subjectsTable.convertRowIndexToModel(selectedRow);
                subjectsTableModel.removeSubject(modelRow);
            }
        });

        doneBtn.addActionListener((e) -> dispose());
    }
}

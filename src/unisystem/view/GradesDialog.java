package unisystem.view;

import tools.Dialog;
import unisystem.model.Grade;
import unisystem.model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class GradesDialog extends JDialog {
    private JPanel contentPane;
    private JTable gradesTable;
    private JButton doneBtn;
    private JTextField subjectField;
    private JButton removeBtn;
    private JButton addGradeBtn;
    private ButtonGroup gradeBtnGroup;

    private final Student student;
    private final List<Grade> grades;
    private DefaultTableModel gradesTableModel;

    public GradesDialog(JFrame ownerFrame, Student student, List<Grade> grades) {
        this.student = student;
        this.grades = grades;

        CreateGradesTableModel();
        InsertStudentsTableModel();
        AddFrameEventListeners();

        setTitle("Оценките на " + student.getName());
        setContentPane(contentPane);
        setModal(true);
        pack();
        setLocationRelativeTo(ownerFrame);
        setVisible(true);
    }

    private void AddFrameEventListeners() {
        addGradeBtn.addActionListener((e) -> {
            String subject = subjectField.getText();
            int selectedGrade = Integer.parseInt(gradeBtnGroup.getSelection().getActionCommand());
            try {
                Grade grade = new Grade(student, subject, selectedGrade);
                grades.add(grade);

                String[] record = {grade.getSubject(), Integer.toString(grade.getGrade())};
                gradesTableModel.addRow(record);

                subjectField.setText("");
                gradeBtnGroup.clearSelection();
            } catch (Exception ex) {
                Dialog.showError(this, ex);
            }
        });

        removeBtn.addActionListener((e) -> {
            if (gradesTable.getSelectedRow() >= 0) {

                int selectedRow = gradesTable.getSelectedRow();
                var grade = grades.get(gradesTable.convertRowIndexToModel(selectedRow));
                grades.remove(grade);
                gradesTableModel.removeRow(selectedRow);
            }
        });

        doneBtn.addActionListener((e) -> dispose());
    }

    private void InsertStudentsTableModel() {
        for (var grade : grades) {
            gradesTableModel.addRow(new Object[]{grade.getSubject(), grade.getGrade()});
        }
    }

    private void CreateGradesTableModel() {
        String[] columnNames = {"Предмет", "Оценка"};
        gradesTableModel = new DefaultTableModel(columnNames, 0);

        gradesTable.setModel(gradesTableModel);
    }
}
package unisystem.view;

import tools.Dialog;
import unisystem.App;
import unisystem.model.*;

import javax.swing.*;
import java.util.OptionalDouble;

public class AverageReportDialog extends JDialog {
    private JPanel contentPane;
    private JButton doneBtn;
    private JComboBox majorsComboBox;
    private JButton calculateBtn;
    private JComboBox yearComboBox;

    public AverageReportDialog(JFrame ownerFrame) {
        InitComboBoxes();
        AddFrameEventListeners();

        setContentPane(contentPane);
//        getRootPane().setDefaultButton(buttonOK);
        pack();
        setModal(true);
        setLocationRelativeTo(ownerFrame);
        setVisible(true);
    }

    private void InitComboBoxes() {
        for (Major major : App.getMajors()) {
            majorsComboBox.addItem(major);
        }
        majorsComboBox.setSelectedItem(null);

        // ActionListener is below
    }

    private void AddFrameEventListeners() {
        majorsComboBox.addActionListener(e -> {
            Major selectedMajor = (Major)majorsComboBox.getSelectedItem();
            if (selectedMajor != null) {
                yearComboBox.removeAllItems();

                for (int i = 1; i <= selectedMajor.getTotalYears(); i++) {
                    yearComboBox.addItem(i);
                }
            }
        });


        calculateBtn.addActionListener((e) -> {
            var optionalAvg = getAverage();

            if (getAverage().isPresent()) {
                double average = optionalAvg.getAsDouble();

                Dialog.show(this, String.format("Средният успех на учениците от " + majorsComboBox.getSelectedItem() +
                        ", курс " + yearComboBox.getSelectedItem() + " е " + "%.2f", average
                ));
            } else {
                Dialog.show(this, "Няма студенти, които покриват изискванията");
            }
        });

        doneBtn.addActionListener((e) -> {
            dispose();
        });
    }

    private OptionalDouble getAverage() {
        var filteredStudents = App.getStudents().stream()
                .filter(s ->
                        s.getMajor().equals(majorsComboBox.getSelectedItem())
                                && s.getYear() == (int) yearComboBox.getSelectedItem()
                ).toList();

        return filteredStudents.stream().flatMap(student -> student.getGrades().stream()).mapToDouble(Grade::getGrade).average();
    }
}

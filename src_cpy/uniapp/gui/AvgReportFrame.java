package uniapp.gui;

import uniapp.model.*;

import javax.swing.*;
import java.awt.Component;
import java.util.List;
import java.util.OptionalDouble;

public class AvgReportFrame extends JDialog {
    private final List<Major> majors;
    private final List<Student> students;
    private JPanel contentPane;
    private JButton doneBtn;
    private JComboBox<Major> majorsComboBox;
    private JButton calculateBtn;
    private JComboBox<Integer> yearComboBox;

    public AvgReportFrame(Component owner, List<Major> majors, List<Student> students) {
        this.majors = majors;
        this.students = students;
        initComboBoxes();
        addFrameEventListeners();

//        getRootPane().setDefaultButton(buttonOK);
        setContentPane(contentPane);
        setModal(true);
        pack();
        setLocationRelativeTo(owner);
        setVisible(true);
    }

    private void initComboBoxes() {
        for (var major : majors) {
            majorsComboBox.addItem(major);
        }
        majorsComboBox.setSelectedItem(null);
    }

    private void addFrameEventListeners() {
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

                JOptionPane.showMessageDialog(this, String.format("Средният успех на учениците от " + majorsComboBox.getSelectedItem() +
                        ", курс " + yearComboBox.getSelectedItem() + " е " + "%.2f", average
                ), "Average", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Няма студенти, които покриват изискванията", "Average", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        doneBtn.addActionListener((e) -> {
            dispose();
        });
    }

    private OptionalDouble getAverage() {
        var filteredStudents = students.stream()
                .filter(s ->
                        s.getMajor() == majorsComboBox.getSelectedItem()
                                && s.getYear() == (Integer) yearComboBox.getSelectedItem()
                ).toList();

        return filteredStudents.stream().flatMap(student -> student.getGrades().stream()).mapToDouble(Grade::getGrade).average();
    }
}

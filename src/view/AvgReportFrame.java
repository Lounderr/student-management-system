package view;

import data.model.*;
import data.model.repository.impl.*;

import javax.swing.*;
import java.awt.Component;
import java.util.List;
import java.util.OptionalDouble;

public class AvgReportFrame extends JDialog {
    private final MajorsRepository majorsRepository;
    private final StudentsRepository studentsRepository;
    private final GradesRepository gradesRepository;
    private JPanel contentPane;
    private JButton doneBtn;
    private JComboBox<Major> majorsComboBox;
    private JButton calculateBtn;
    private JComboBox<Integer> yearComboBox;

    public AvgReportFrame(Component owner, MajorsRepository majorsRepository, StudentsRepository studentsRepository, GradesRepository gradesRepository) {
        this.majorsRepository = majorsRepository;
        this.studentsRepository = studentsRepository;
        this.gradesRepository = gradesRepository;
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
        for (var major : majorsRepository.All()) {
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
        Major selectedMajor = (Major) majorsComboBox.getSelectedItem();
        Integer selectedYear = (Integer) yearComboBox.getSelectedItem();
        
        if (selectedMajor == null || selectedYear == null) {
            return OptionalDouble.empty();
        }
        
        return gradesRepository.getAverageForMajorAndYear(selectedMajor.getId(), selectedYear);
    }
}

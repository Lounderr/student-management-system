package uniapp;

import uniapp.gui.MainFrame;
import uniapp.model.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class UniApp {
    private List<Major> majors;
    private List<Student> students;

    public UniApp() {
        // SQL seed majors and students list
        this.majors = new ArrayList<>();
        this.students = new ArrayList<>();
    }

    public void run() {
        SwingUtilities.invokeLater(() ->
                new MainFrame(majors, students)
        );
    }

    public void useMockData() {
        try {


            Major major1 = new Major("Engineering", 4);
            Major major2 = new Major("Math", 3);

            Subject subject1 = new Subject("Algebra");
            Subject subject2 = new Subject("Java");

            major1.getSubjects().add(subject1);
            major1.getSubjects().add(subject2);
            major2.getSubjects().add(subject1);

            majors.add(major1);
            majors.add(major2);

            Student student1 = new Student("2301321048", "Georgi Georgiev Georgiev", major1, 1);
            Student student2 = new Student("2301321028", "Nikola Zaprinov Pepelov", major1, 1);
            Student student3 = new Student("2410101010", "Dimitar Stefanov Petrov", major2, 3);

            student1.getGrades().add(new Grade(subject1, 6));
            student2.getGrades().add(new Grade(subject1, 5));
            student3.getGrades().add(new Grade(subject1, 4));

            students.add(student1);
            students.add(student2);
            students.add(student3);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Seeding error: " + ex.getMessage(), "Seeding error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        var app = new UniApp();

//        if (args.length >= 1 && args[0].equals("use-mock-data"))
            app.useMockData();

        app.run();
    }
}
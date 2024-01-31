package unisystem;

import javax.swing.*;

import unisystem.model.Major;
import unisystem.view.RootFrame;

import java.util.*;

public final class App {
    private static final App INSTANCE = new App();
    private static final Map<String, Major> majors = new HashMap<>();
    private RootFrame rootFrame;

    private App() {
        run();
    }

    private void run() {
        SwingUtilities.invokeLater(() -> {
            rootFrame = new RootFrame();
        });
    }

//    public void seedMockData() {
//        try {
//            var engineeringMajor = new Major("Engineering", 4);
//            engineeringMajor.getSubjects().add("Algebra");
//            engineeringMajor.getSubjects().add("Java");
//            engineeringMajor.getSubjects().add("C++");
//
//            var mathMajor = new Major("Math", 3);
//            mathMajor.getSubjects().add("Algebra");
//            mathMajor.getSubjects().add("Geometry");
//
//            majors.add(engineeringMajor);
//            majors.add(mathMajor);
//
//            var student1 = new Student("2301321048", "Georgi Georgiev", engineeringMajor, 1);
//            var student2 = new Student("2301321058", "Dimitar Petrov", engineeringMajor, 2);
//            var student3 = new Student("2301321068", "Stefan Stambolov", mathMajor, 3);
//            var student4 = new Student("2301321078", "Vasil Iliev", mathMajor, 1);
//
//            students.add(student1);
//            students.add(student2);
//            students.add(student3);
//            students.add(student4);
//
//            for (var student : students) {
//                int randomGrade = new Random().nextInt(5) + 2;
//                Grade grade = new Grade(student, "Algebra", randomGrade);
//                student.getGrades().add(grade);
//            }
//
//        } catch (Exception ex) {
//            Dialog.showError(rootFrame, ex);
//        }
//    }

    public static App getInstance() {
        return INSTANCE;
    }

    public RootFrame getRootFrame() {
        return rootFrame;
    }

    public static Map<String, Major> getMajors() {
        return majors;
    }
}

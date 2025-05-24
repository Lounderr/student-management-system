import data.Database;
import data.model.repository.impl.*;
import view.MainFrame;

public class Main {
    public static void main(String[] args) {
        Database.isInitialized = true;
        Database.init();
        Database.addMockData();

        var majorsRepository = new MajorsRepository(Database.connection);
        var studentsRepository = new StudentsRepository(Database.connection);
        var gradesRepository = new GradesRepository(Database.connection);
        var subjectsRepository = new SubjectsRepository(Database.connection);

        var mainFrame = new MainFrame(majorsRepository, studentsRepository, gradesRepository, subjectsRepository);
        mainFrame.setVisible(true);

        // Add window listener to close database connection when application closes
        mainFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                Database.close();
                System.exit(0);
            }
        });
    }
}

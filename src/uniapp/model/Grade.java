package uniapp.model;

public class Grade {
    private Subject subject;
    private int grade;

    public Grade(Subject subject, int grade) throws Exception {
        setSubject(subject);
        setGrade(grade);
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) throws Exception {
        if (grade >= 2 && grade <= 6)
            this.grade = grade;
        else
            throw new Exception("Grade must be between 2 and 6");
    }

    @Override
    public String toString() {
        return Integer.toString(this.getGrade());
    }
}


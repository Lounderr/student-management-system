package data.model;

public class Grade extends BaseEntity {
    private int subjectId;
    private int studentId;
    private int grade;

    public Grade(int id, int subjectId, int studentId, int grade) {
        super(id);
        setSubjectId(subjectId);
        setStudentId(studentId);
        setGrade(grade);
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) throws IllegalArgumentException {
        if (grade >= 2 && grade <= 6)
            this.grade = grade;
        else
            throw new IllegalArgumentException("Grade must be between 2 and 6");
    }
}
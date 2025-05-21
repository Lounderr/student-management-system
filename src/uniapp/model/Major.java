package uniapp.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Major {
    private String name;
    private int totalYears;
    private final List<Subject> subjects;

    public Major(String name, int totalYears) throws Exception {
        setName(name);
        setTotalYears(totalYears);
        this.subjects = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws Exception {
        if (name == null)
            throw new Exception("Name is null");

        if (name.length() >= 2 && name.length() <= 80)
            this.name = name.toUpperCase();
        else
            throw new Exception("Name length must be between 2 and 80 characters");
    }

    public int getTotalYears() {
        return totalYears;
    }

    public void setTotalYears(Integer totalYears) throws Exception {
        if (totalYears == null)
            throw new Exception("Total years is null");

        if (totalYears >= 1 && totalYears <= 15)
            this.totalYears = totalYears;
        else
            throw new Exception("Total major years must between 1 and 15");
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

//    public int getSubjectsCount() {
//        return subjects.size();
//    }
//
//    public void insertSubject(int row, Subject subject) throws UnsupportedOperationException {
//        if (!subjects.contains(subject))
//            subjects.add(row, subject);
//        else
//            throw new UnsupportedOperationException("Subject already exists");
//    }
//
//    public void removeSubject(int index) {
//        subjects.remove(index);
//    }
//
//    public Subject getSubject(int index) throws IndexOutOfBoundsException {
//        return subjects.get(index);
//    }

    @Override
    public String toString() {
        return getName();
    }
}

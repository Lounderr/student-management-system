package unisystem.model;

import tools.StringOperations;

import java.util.ArrayList;
import java.util.List;

public class Student {
    private String id;
    private String name;
    private Major major;
    private int year;
    private final List<Grade> grades;

    public Student(String id, String name, Major major, Integer year) throws Exception {
        setId(id);
        setName(name);
        setMajor(major);
        setYear(year);
        this.grades = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) throws Exception {
        if (id == null)
            throw new Exception("Student ID is null");

        if (id.length() == 10 && StringOperations.isNumeric(id))
            this.id = id;
        else
            throw new Exception("Student ID must consist of 10 digits");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws Exception {
        if (name == null)
            throw new Exception("Student name is null");

        if (name.length() >= 2 && name.length() <= 160)
            this.name = name;
        else
            throw new Exception("Name length must be between 2 and 160 characters");
    }

    public Major getMajor() {
        return major;
    }

    public void setMajor(Major major) throws Exception {
        if (major == null)
            throw new Exception("Major is null");

        if (this.getYear() > major.getTotalYears()) {
            throw new Exception("Year is greater than major's total years");
        }

        this.major = major;
    }

    public int getYear() {
        return year;
    }

    public void setYear(Integer year) throws Exception {
        if (year == null)
            throw new Exception("Year is null");

        if (year < 1)
            throw new Exception("Year must be greater than 0");

        if (year <= this.getMajor().getTotalYears()) {
            this.year = year;
        } else
            throw new Exception("Year is greater than the total major years");
    }

    public List<Grade> getGrades() {
        return this.grades;
    }

    @Override
    public String toString() {
        return this.getId();
    }
}

package uniapp.model;

import utility.StringOperations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Student {
    private String id;
    private String fullName;
    private Major major;
    private int year;
    private final List<Grade> grades;

    public Student(String id, String fullName, Major major, Integer year) throws Exception {
        setId(id);
        setFullName(fullName);
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

        id = id.trim();

        if (id.length() == 10 && StringOperations.isNumeric(id))
            this.id = id;
        else
            throw new Exception("Student ID must consist of 10 digits");
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) throws Exception {
        if (fullName == null)
            throw new Exception("Name is null");



        List<String> names = Arrays.stream(fullName.split(" ")).filter(s -> !s.isEmpty()).toList();


        if (names.size() == 3) {
            for (var name : names) {
                if (name.length() < 2 || name.length() > 80)
                    throw new Exception("Each name string must be between 2 and 80 characters");

                if (!StringOperations.isAlphabetic(name))
                    throw new Exception("Name can only contain letters");
            }

            this.fullName = String.join(" ", names).toUpperCase();
        } else
            throw new Exception("Name must consist of three strings");
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

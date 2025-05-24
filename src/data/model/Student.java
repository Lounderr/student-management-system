package data.model;

import utility.StringOperations;

import java.util.Arrays;
import java.util.List;

public class Student extends BaseEntity {
    private String facultyNumber;
    private String fullName;
    private Major major;
    private int year;

    public Student(int id, String facultyNumber, String fullName, Major major, Integer year) {
        super(id);
        setFacultyNumber(facultyNumber);
        setFullName(fullName);
        setMajor(major);
        setYear(year);
    }

    public String getFacultyNumber() {
        return facultyNumber;
    }

    public void setFacultyNumber(String facultyNumber) {
        if (facultyNumber == null)
            throw new IllegalArgumentException("Student ID is null");

        facultyNumber = facultyNumber.trim();

        if (facultyNumber.length() == 10 && StringOperations.isNumeric(facultyNumber))
            this.facultyNumber = facultyNumber;
        else
            throw new IllegalArgumentException("Student ID must consist of 10 digits");
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        if (fullName == null)
            throw new IllegalArgumentException("Name is null");

        List<String> names = Arrays.stream(fullName.split(" ")).filter(s -> !s.isEmpty()).toList();

        if (names.size() == 3) {
            for (var name : names) {
                if (name.length() < 2 || name.length() > 80)
                    throw new IllegalArgumentException("Each name string must be between 2 and 80 characters");

                if (!StringOperations.isAlphabetic(name))
                    throw new IllegalArgumentException("Name can only contain letters");
            }

            this.fullName = String.join(" ", names).toUpperCase();
        } else
            throw new IllegalArgumentException("Name must consist of three strings");
    }

    public Major getMajor() {
        return major;
    }

    public void setMajor(Major major) {
        if (major == null)
            throw new IllegalArgumentException("Major is null");

        if (this.getYear() > major.getTotalYears()) {
            throw new IllegalArgumentException("Year is greater than major's total years");
        }

        this.major = major;
    }

    public int getYear() {
        return year;
    }

    public void setYear(Integer year) {
        if (year == null)
            throw new IllegalArgumentException("Year is null");

        if (year < 1)
            throw new IllegalArgumentException("Year must be greater than 0");

        if (year <= this.getMajor().getTotalYears()) {
            this.year = year;
        } else
            throw new IllegalArgumentException("Year is greater than the total major years");
    }
}

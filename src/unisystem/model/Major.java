package unisystem.model;

import tools.UniqueList;

public class Major {
    private String name;
    private int totalYears;
    private final UniqueList<Subject> subjects;

    public Major(String name, int totalYears) throws Exception {
        setName(name);
        setTotalYears(totalYears);
        this.subjects = new UniqueList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws Exception {
        if (name == null)
            throw new Exception("Name is null");

        if (name.length() >= 2 && name.length() <= 80)
            this.name = name;
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

    public UniqueList<Subject> getSubjects() {
        return subjects;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}

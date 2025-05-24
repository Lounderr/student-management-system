package data.model;

public class Major extends BaseEntity {
    private String name;
    private int totalYears;

    public Major(int id, String name, int totalYears) {
        super(id);
        setName(name);
        setTotalYears(totalYears);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null)
            throw new IllegalArgumentException("Name is null");

        if (name.length() >= 2 && name.length() <= 80)
            this.name = name.toUpperCase();
        else
            throw new IllegalArgumentException("Name length must be between 2 and 80 characters");
    }

    public int getTotalYears() {
        return totalYears;
    }

    public void setTotalYears(Integer totalYears) {
        if (totalYears == null)
            throw new IllegalArgumentException("Total years is null");

        if (totalYears >= 1 && totalYears <= 15)
            this.totalYears = totalYears;
        else
            throw new IllegalArgumentException("Total major years must between 1 and 15");
    }

    @Override
    public String toString() {
        return name;
    }
}

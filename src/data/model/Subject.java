package data.model;

public class Subject extends BaseEntity {
    private String name;

    public Subject(int id, String name) {
        super(id);
        setName(name);
    }

    public void setName(String name) {
        if (name == null)
            throw new IllegalArgumentException("Subject name is null");
        if (name.length() >= 2 && name.length() <= 100)
            this.name = name.toUpperCase();
        else
            throw new IllegalArgumentException("Name length must be between 2 and 100");
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return name;
    }
}

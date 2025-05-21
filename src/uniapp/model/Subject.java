package uniapp.model;

public class Subject {
    private String name;

    public Subject(String name) throws Exception {
        setName(name);
    }

    public void setName(String name) throws Exception {
        if (name == null)
            throw new Exception("Subject name is null");
        if (name.length() >= 2 && name.length() <= 100)
            this.name = name.toUpperCase();
        else
            throw new Exception("Name length must be between 2 and 100");
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return getName();
    }
}

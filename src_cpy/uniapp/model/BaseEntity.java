package model;

public abstract class BaseEntity {
    public BaseEntity(int id) {
        this.setId(id);
    }

    private int id;

    public int getId() {
        return id;
    }

    protected void setId(int id) {
        this.id = id;
    }
}

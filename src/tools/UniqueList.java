package tools;

import java.util.*;

public class UniqueList<E> {
    private final List<E> list;

    public UniqueList() {
        this.list = new ArrayList<>();
    }

    public UniqueList(UniqueList<E> list) {
        this.list = list.toList();
    }

    public UniqueList(List<E> list) {
        this.list = list.stream().distinct().toList();
    }

    public List<E> toList() {
        return list;
    }

    public int size() {
        return list.size();
    }

    public void add(E item) throws Exception {
        if (!list.contains(item))
            list.add(item);
        else
            throw new Exception("Item already exists");
    }

    public void add(int row, E item) throws Exception {
        if (!list.contains(item))
            list.add(row, item);
        else
            throw new Exception("Item already exists");
    }

    public void remove(int row) throws IndexOutOfBoundsException {
        if (list.get(row) != null)
            list.remove(row);
        else
            throw new IndexOutOfBoundsException("Item not present inside the collection");
    }

    public void remove(E item) throws Exception {
        if (list.contains(item))
            list.remove(item);
        else
            throw new Exception("Item not present inside the collection");
    }

    public boolean contains(E item) {
        return list.contains(item);
    }

    public E get(int row) {
        return list.get(row);
    }
}

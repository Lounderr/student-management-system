package uniapp.repositories;

import uniapp.model.Student;

import java.util.List;

public interface IRepository<TEntity> {
    public List<TEntity> All();
    public TEntity FindById(int id);
    public boolean Add(TEntity entity);
    public boolean Update(TEntity entity);
    public boolean Delete(int id);
}

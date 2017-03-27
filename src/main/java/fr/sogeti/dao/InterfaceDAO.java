package fr.sogeti.dao;

import java.util.List;

import fr.sogeti.domain.Element;

public interface InterfaceDAO<T extends Element> {

    public List<T> getAll();
    public T get(int id);
    public void save(T t);
    public void delete(int id);
    public void update(T t);
}

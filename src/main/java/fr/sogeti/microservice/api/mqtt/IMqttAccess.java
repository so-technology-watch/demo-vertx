package fr.sogeti.microservice.api.mqtt;

import java.util.Map;

/**
 *
 * @author fduneau
 */
public interface IMqttAccess<T> {
    public Map<Integer, T> getAll();
    public T get(int id);
    public T save(T t);
    public void update(T t);
    public void delete(int id);
}

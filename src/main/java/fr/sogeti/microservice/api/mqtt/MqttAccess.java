package fr.sogeti.microservice.api.mqtt;

import java.util.Map;

/**
 *
 * @author fduneau
 */
public class MqttAccess<T> implements IMqttAccess<T>{
    protected String route;
    
    public MqttAccess(String route){
        this.route = route;
    }
    
    @Override
    public Map<Integer, T> getAll() {
        return null;
    }

    @Override
    public T get(int id) {
        return null;
    }

    @Override
    public T save(T t) {
        return null;
    }

    @Override
    public void update(T t) {
    }
    
    @Override
    public void delete(int id) {
    }
}

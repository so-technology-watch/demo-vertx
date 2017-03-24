package fr.sogeti.microservice.api.mqtt;

import java.util.function.Consumer;

/**
 *
 * @author fduneau
 */
public interface IMqttAccess<T> {
    /**
     * 
     * @param callback the callack called with the json representation of a get all
     */
    public void getAll(Consumer<String> callback);
    /**
     * 
     * @param id the id we need to get
     * @param callback the callback called when the id as been retrieved
     */
    public void get(int id, Consumer<String> callback);
    /**
     * 
     * @param t the entity we need to save
     * @param callback the callback called with the new entity created, the id may change
     */
    public void save(T t, Consumer<String> callback);
    /**
     * 
     * @param t the entity to update, the id must be specified in the entity
     * @param callback the callback called when the update is done
     */
    public void update(T t, Consumer<String> callback);
    /**
     * 
     * @param id the id of the entity we want to delete
     * @param callback the callback called when the delete is done
     */
    public void delete(int id, Consumer<String> callback);
}

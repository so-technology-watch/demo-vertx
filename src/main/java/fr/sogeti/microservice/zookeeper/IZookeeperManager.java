package fr.sogeti.microservice.zookeeper;

import java.util.List;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;

/**
 *
 * @author fduneau
 */
public interface IZookeeperManager {
    
    /**
     * creates a node for the given path
     * @param path the node's path
     * @param data the data we want the put on the following node
     * @throws KeeperException
     * @throws InterruptedException 
     */
    void createNode(String path, byte[] data) throws KeeperException, InterruptedException;

    default void createNode(String path, String data) throws KeeperException, InterruptedException {
        createNode(path, data.getBytes());
    }
    
    /**
     * deletes a given node
     * @param path the node's path
     * @throws KeeperException
     * @throws InterruptedException 
     */
    void deleteNode(String path) throws KeeperException, InterruptedException;
    
    /**
     * update a given node, putting new datas
     * @param path the node's path
     * @param data the new datas
     * @throws KeeperException
     * @throws InterruptedException 
     */
    void updateNode(String path, byte[] data) throws KeeperException, InterruptedException;
    
    default void updateNode(String path, String data) throws KeeperException, InterruptedException {
        updateNode(path, data.getBytes());
    }
    
    /**
     * 
     * @param path the node's path
     * @return the stat that indicates if a node exist, otherwise null
     * @throws KeeperException
     * @throws InterruptedException 
     */
    Stat existsNode(String path) throws KeeperException, InterruptedException;
    
    /**
     * 
     * @param path the node's path
     * @return the String representation of the retreived data, in UTF-8 if possible
     * @throws KeeperException
     * @throws InterruptedException 
     */    
    String getData(String path) throws KeeperException, InterruptedException;
    
    /**
     * 
     * @param baseNode the base node's path
     * @return the list of available sub nodes for the given node
     * @throws KeeperException
     * @throws InterruptedException 
     */
    List<String> getListSubNodes(String baseNode) throws KeeperException, InterruptedException;
    
    /**
     * closes the zooKeeper
     * @throws InterruptedException 
     */
    void close() throws InterruptedException;

}

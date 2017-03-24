package fr.sogeti.microservice.zookeeper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 *
 * @author fduneau
 */
public class ZookeeperManager implements IZookeeperManager {
    private static final Logger LOG = Logger.getLogger(ZookeeperManager.class.getName());
    
    private final ZooKeeper zoo;
    private final CountDownLatch connectedSignal;
    
    /**
     * 
     * @param serverAddress 
     * @param timeout
     * @throws IOException
     * @throws InterruptedException 
     */
    public ZookeeperManager(String serverAddress, int timeout) throws IOException, InterruptedException {
        connectedSignal = new CountDownLatch(1);
        if(LOG.isLoggable(Level.INFO)) {
            LOG.log(Level.INFO, "Connecting to the zooKeepers on {0}", serverAddress);
        }
        zoo = new ZooKeeper(serverAddress, timeout, (watchedEvent) -> {
            if(watchedEvent.getState() == Watcher.Event.KeeperState.SyncConnected){
                if(LOG.isLoggable(Level.INFO)) {
                    LOG.log(Level.INFO, "Connected to the zooKeepers on {0}", serverAddress);
                }
                connectedSignal.countDown();
            }
        });
        connectedSignal.await();
    }
    
    @Override
    public void createNode(String path, byte[] data) throws KeeperException, InterruptedException {
      zoo.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
    }
    
    @Override
    public void updateNode(String path, byte[] data) throws KeeperException, InterruptedException {
        zoo.setData(path, data, existsNode(path).getVersion());
    }

    @Override
    public void deleteNode(String path) throws KeeperException, InterruptedException {
        zoo.delete(path, existsNode(path).getVersion());
    }
    
    @Override
    public Stat existsNode(String path) throws KeeperException, InterruptedException {
        return zoo.exists(path, true);
    }
    
    @Override
    public String getData(String path) throws KeeperException, InterruptedException {
        byte[] data = zoo.getData(path, true, existsNode(path));
        String result;
        try {
            result = new String(data, "UTF-8");
        }catch(UnsupportedEncodingException e){
            result = new String(data);
        }
        return result;
    }
    
    @Override
    public void close() throws InterruptedException {
        zoo.close();
    }
}

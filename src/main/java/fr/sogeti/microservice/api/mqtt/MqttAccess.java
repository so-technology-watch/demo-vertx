package fr.sogeti.microservice.api.mqtt;

import com.google.gson.Gson;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 *
 * @author fduneau
 */
public class MqttAccess<T> implements IMqttAccess<T>{
    private static final Logger LOG = Logger.getLogger(MqttAccess.class.getName());
    
    protected final String route;
    protected final IClientMqtt clientMqtt;
    private final String publishRoute;
    private final String deliverRoute;
    private final String idClient;
    
    
    public MqttAccess(String url, String route, String idClient) throws MqttException {
        this.route = route;
        this.idClient = idClient;
        publishRoute = "publish"+route;
        deliverRoute = "deliver"+route;
        try{
            clientMqtt = new ClientMqtt(url, idClient);
            clientMqtt.connect();
        }catch(MqttException e){
            if(LOG.isLoggable(Level.SEVERE)){
                LOG.log(Level.SEVERE, "Unable to instanciate the Mqtt client : {0}", e.getMessage());
            }
            throw e;
        }
    }
    
    @Override
    public void getAll(Consumer<String> callback) {
        String pubSub = publishRoute+"/GETALL";
        String delSub = deliverRoute+"/GETALL";

        clientMqtt.subscribe(delSub);
        clientMqtt.sendMessage("", pubSub , 2);
        
        clientMqtt.setCallback(new MessageCallback( response -> {
            callback.accept(response);
            clientMqtt.unsubscribe(pubSub, delSub);
        }, error -> {
            callback.accept("ERROR");
            clientMqtt.unsubscribe(pubSub, delSub);
        }));
    }

    @Override
    public void get(int id, Consumer<String> callback) {
        String pubSub = publishRoute+"/GET/"+id;
        String delSub = deliverRoute+"/GET/"+id;

        clientMqtt.subscribe(delSub);
        
        clientMqtt.setCallback(new MessageCallback( response -> {
            callback.accept(response);
            clientMqtt.unsubscribe(pubSub, delSub);
        }, error -> {
            callback.accept("ERROR");
            clientMqtt.unsubscribe(pubSub, delSub);
        }));
        clientMqtt.sendMessage(""+id, pubSub , 2);
    }

    @Override
    public void save(T t, Consumer<String> callback) {
        Gson gson = new Gson();
        String pubSub = publishRoute+"/POST/"+idClient;
        String delSub = deliverRoute+"/POST/"+idClient;
        
        clientMqtt.subscribe(delSub);
        
        clientMqtt.setCallback(new MessageCallback( response -> {
            callback.accept(response);
            clientMqtt.unsubscribe(pubSub, delSub);
        }, error -> {
            callback.accept("ERROR");
            clientMqtt.unsubscribe(pubSub, delSub);
        }));
        clientMqtt.sendMessage(gson.toJson(t), pubSub , 2);
    }

    @Override
    public void update(T t, Consumer<String> callback) {
        Gson gson = new Gson();
        String pubSub = publishRoute+"/PUT/"+idClient;
        String delSub = deliverRoute+"/PUT/"+idClient;
        
        clientMqtt.subscribe(delSub);
        
        clientMqtt.setCallback(new MessageCallback( response -> {
            callback.accept(response);
            clientMqtt.unsubscribe(delSub, pubSub);
        }, error -> {
            callback.accept("ERROR");
            clientMqtt.unsubscribe(pubSub, delSub);
        }));
        clientMqtt.sendMessage(gson.toJson(t), pubSub , 2);
    }
    
    @Override
    public void delete(int id, Consumer<String> callback) {
        String pubSub = publishRoute+"/DELETE/"+id;
        String delSub = deliverRoute+"/DELETE/"+id;
        
        clientMqtt.subscribe(delSub);
        
        clientMqtt.setCallback(new MessageCallback( response -> {
            callback.accept(response);
            clientMqtt.unsubscribe(delSub, pubSub);
        }, error -> {
            callback.accept("ERROR");
            clientMqtt.unsubscribe(pubSub, delSub);
        }));
        clientMqtt.sendMessage(""+id, pubSub , 2);
    }
    
}

package fr.sogeti.mqtt;

import io.vertx.core.AbstractVerticle;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class SubscriberMqtt extends AbstractVerticle{
	
	private String clientId;
	private String topic;
	private MemoryPersistence persistence = new MemoryPersistence();
    private static final Logger LOG = Logger.getLogger(SubscriberMqtt.class.getName());
	private final String brokerUrl;
    private final ClientMqtt client;
    
    
    public SubscriberMqtt(String clientId, String topic, String brokerUrl) throws MqttException{

    	this.clientId = clientId;
    	this.topic = topic;
    	this.brokerUrl = brokerUrl;
        
        client = new ClientMqtt(brokerUrl, clientId, topic);
    }
	
    
    @Override
	public void start(){
        try{
            initClient();
        }catch(MqttException e){
            if(LOG.isLoggable(Level.SEVERE)){
                LOG.log(Level.SEVERE, "Client failed to start {0}", e.getMessage());
            }
        }
	}
    
    private void initClient() throws MqttException{
    	
    	//C'est un peu du bricolage à amélorer
        client.getClient().setCallback(new MessageCallback( vo -> {
        	new Thread(() -> {
        		client.disconnect();
        	}).start();
        	return null; 
    	} ));
        
        client.connect();
        client.getClient().subscribe(topic, 0);
        LOG.log(Level.INFO, "SUBSCRIBER Client subscribed to the topic : {0}", topic);
        
    }

}

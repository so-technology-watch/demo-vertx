package fr.sogeti.mqtt;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import io.vertx.core.AbstractVerticle;

public class SubscriberMqtt extends AbstractVerticle{
	

	private String broker = "tcp://localhost:1883";
	private String clientId;
	private String topic;
	private MemoryPersistence persistence = new MemoryPersistence();
    private static final Logger LOG = Logger.getLogger(ServerMqtt.class.getName());
	
    
    
    public SubscriberMqtt(String clientId, String topic) {

    	this.clientId = clientId;
    	this.topic = topic;
    	
    	
    }
	
	public void start(){
		
		try {
			MqttClient client = new MqttClient(broker, clientId, persistence);
			MqttConnectOptions connectOptions = new MqttConnectOptions();
			connectOptions.setCleanSession(true);
			LOG.log(Level.INFO, "Connecting to broker: {0}", broker);
			client.connect();
			LOG.log(Level.INFO, "Connected to broker");
			client.subscribe(topic);
			LOG.log(Level.INFO, "Subscibed to: {0}", topic);
			client.setCallback(new MessageCallback());
			client .disconnect();
			LOG.log(Level.INFO, "Disconnected from broker");
			
		} catch (MqttException e) {
			
			LOG.log(Level.INFO, "Something went wrong: {0}", e);
		}
	}

}

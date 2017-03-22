package fr.sogeti.mqtt;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import io.vertx.core.AbstractVerticle;

public class PublisherMqtt extends AbstractVerticle{
	

	private String broker = "tcp://localhost:1883";
	private String clientId;
	private String topic;
	private String message;
	private MemoryPersistence persistence = new MemoryPersistence();
    private static final Logger LOG = Logger.getLogger(PublisherMqtt.class.getName());
    
    public PublisherMqtt(String clientId, String topic, String message) {
    	
    	this.clientId = clientId;
    	this.message = message;
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
			LOG.log(Level.INFO, "Publishing message: {0}", message);
			MqttMessage msg = new MqttMessage(message.getBytes());
			msg.setQos(1);
			Thread.sleep(1000);
			client.publish(topic, msg);
			LOG.log(Level.INFO, "The message has been published");
			client .disconnect();
			LOG.log(Level.INFO, "Disconnected from broker");
			
		} catch (MqttException | InterruptedException e) {
			
			LOG.log(Level.INFO, "Something went wrong: {0}", e);
		}
    	
    }


}

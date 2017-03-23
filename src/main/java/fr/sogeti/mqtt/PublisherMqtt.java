package fr.sogeti.mqtt;

import io.vertx.core.AbstractVerticle;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.paho.client.mqttv3.MqttException;

public class PublisherMqtt extends AbstractVerticle{


	private static final Logger LOG = Logger.getLogger(PublisherMqtt.class.getName());
    private final ClientMqtt client;
    private String message;

	public PublisherMqtt(String clientId, String topic, String message, String brokerUrl) throws MqttException{
        this.message = message;
		client = new ClientMqtt(brokerUrl, clientId, topic);
	}

    @Override
	public void start(){

		try {
            initClient();
		} catch (MqttException e) {
			if(LOG.isLoggable(Level.SEVERE)){
                LOG.log(Level.SEVERE, "Client failed to start {0}", e.getMessage());
            }
		}

	}

    private void initClient() throws MqttException {
        client.connect();
        client.sendMessage(message, 0);
        client.disconnect();
    }
    
    
}

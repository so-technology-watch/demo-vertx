package fr.sogeti.mqtt;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MessageCallback implements MqttCallback{
	
    private static final Logger LOG = Logger.getLogger(MessageCallback.class.getName());


	@Override
	public void connectionLost(Throwable arg0) {
		LOG.log(Level.INFO, "MessageCallback: connectionLost {0}", arg0);
		
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		LOG.log(Level.INFO, "MessageCallback: deliveryComplete: {0}", arg0);

	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		LOG.log(Level.INFO, "MessageCallback: Message recieved: {0}", message);
	}

}

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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		

	}

	@Override
	public void messageArrived(String arg0, MqttMessage arg1) throws Exception {

		System.out.println("Message reçu: " +  arg1);

	}

}

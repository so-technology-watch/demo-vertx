package fr.sogeti.microservice.api.mqtt;

import io.vertx.core.buffer.Buffer;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MessageCallback implements MqttCallback{
	
    private static final Logger LOG = Logger.getLogger(MessageCallback.class.getName());
    private final Consumer<String> callbackMessageArrived;
    
    public MessageCallback(Consumer<String> callbackMessageArrived) {
    	this.callbackMessageArrived = callbackMessageArrived;
	}
    
	@Override
	public void connectionLost(Throwable error) {
		LOG.log(Level.INFO, "MessageCallback: connectionLost {0}", error.getMessage());
		error.printStackTrace();
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken delivery) {
        try{
            if(LOG.isLoggable(Level.INFO)){
                MqttMessage message = delivery.getMessage();
                if(message != null){
                    LOG.log(Level.INFO, "MessageCallback: deliveryComplete: {0}", message.getPayload());
                }else{
                    LOG.log(Level.INFO, "MessageCallback: deliveryComplete: received an empty message");
                }
            }
        }catch(MqttException e){
            e.printStackTrace();
        }
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
        String messageStr = Buffer.buffer(message.getPayload()).toString();
        if(LOG.isLoggable(Level.INFO)){
            LOG.log(Level.INFO, "MessageCallback: Message recieved: {0}", messageStr);
        }
		callbackMessageArrived.accept(messageStr);
	}

}

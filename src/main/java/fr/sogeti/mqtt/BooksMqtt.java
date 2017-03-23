package fr.sogeti.mqtt;

import java.util.logging.Logger;

import org.eclipse.paho.client.mqttv3.MqttException;

import fr.sogti.main.VerticleBooks;

public class BooksMqtt extends ClientMqtt{
	

	private final String PUBLISH_TOPIC = "publish/books";
	private final String DELIVER_TOPIC = "deliver/books";
	private String serverURI;
	private String clientId;
    private static final Logger LOG = Logger.getLogger(VerticleBooks.class.getName());
	
	public BooksMqtt(String brokerUrl, String clientId, String topic) throws MqttException {
		super(brokerUrl, clientId, topic);
	}
	
	
	

	public void launch(){
		
		
		setCallBack(new MessageRecievedCallback(this));
		connect();
		subscribe(PUBLISH_TOPIC);
			
		
	}

	public String getPUBLISH_TOPIC() {
		return PUBLISH_TOPIC;
	}


	public String getDELIVER_TOPIC() {
		return DELIVER_TOPIC;
	}


}

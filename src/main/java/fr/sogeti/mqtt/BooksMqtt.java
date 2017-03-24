package fr.sogeti.mqtt;

import org.eclipse.paho.client.mqttv3.MqttException;

public class BooksMqtt extends ClientMqtt {

    // Paths to the topics
    public static final String PUBLISH_TOPIC = "publish/books";
    public static final String DELIVER_TOPIC = "deliver/books";

    public BooksMqtt(String brokerUrl, String clientId) throws MqttException {
	super(brokerUrl, clientId);
    }

    /**
     * Launches the MQTT client
     */
    public void launch() {

	setCallBack(new MessageRecievedCallback(this));
	connect();
	subscribe(PUBLISH_TOPIC + "/#");

    }

}

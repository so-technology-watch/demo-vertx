package fr.sogeti.mqtt;

import org.eclipse.paho.client.mqttv3.MqttException;

import fr.sogeti.domain.Element;

public class ElementsMqtt<T extends Element> extends ClientMqtt {

    // Paths to the topics
    private final String publishTopic;
    private final String deliverTopic;
    private Class<T> clazz;

    public ElementsMqtt(String brokerUrl, String clientId, Class<T> clazz) throws MqttException {
	super(brokerUrl, clientId);
	this.clazz = clazz;
	this.publishTopic = "publish/" + clazz.getSimpleName().toLowerCase() + "s";
	this.deliverTopic = "deliver/" + clazz.getSimpleName().toLowerCase() + "s";
    }

    /**
     * Launches the MQTT client
     */
    public void launch() {

	setCallBack(new MessageRecievedCallback<T>(this, clazz));
	connect();
	subscribe(publishTopic + "/#");

    }

    public String getPublishTopic() {

	return publishTopic;
    }

    public String getDeliverTopic() {

	return deliverTopic;
    }

}
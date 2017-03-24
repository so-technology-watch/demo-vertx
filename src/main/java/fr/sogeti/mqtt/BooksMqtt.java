package fr.sogeti.mqtt;

import java.util.logging.Logger;

import org.eclipse.paho.client.mqttv3.MqttException;

import fr.sogti.main.VerticleBooks;
import io.vertx.core.cli.CLI;

public class BooksMqtt extends ClientMqtt {

    private final String PUBLISH_TOPIC = "publish/books";
    private final String DELIVER_TOPIC = "deliver/books";
    private static final Logger LOG = Logger.getLogger(VerticleBooks.class.getName());

    public BooksMqtt(String brokerUrl, String clientId) throws MqttException {
	super(brokerUrl, clientId);
    }

    public void launch() {

	setCallBack(new MessageRecievedCallback(this));
	connect();
	subscribe(PUBLISH_TOPIC + "/#");
	System.out.println();

    }

    public String getPUBLISH_TOPIC() {
	return PUBLISH_TOPIC;
    }

    public String getDELIVER_TOPIC() {
	return DELIVER_TOPIC;
    }

}

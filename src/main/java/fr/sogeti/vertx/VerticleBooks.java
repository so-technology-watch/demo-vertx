package fr.sogeti.vertx;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.paho.client.mqttv3.MqttException;

import fr.sogeti.mqtt.BooksMqtt;
import io.vertx.core.AbstractVerticle;

public class VerticleBooks extends AbstractVerticle {

    BooksMqtt booksMqtt;
    private static final Logger LOG = Logger.getLogger(VerticleBooks.class.getName());

    public VerticleBooks(String serverURI, String clientId) {

	try {
	    this.booksMqtt = new BooksMqtt(serverURI, clientId);
	} catch (MqttException e) {

	    LOG.log(Level.INFO, "An error occured while creating the MQTT client, ", e.getMessage());
	}
    }

    public void start() {

	booksMqtt.launch();

    }

}

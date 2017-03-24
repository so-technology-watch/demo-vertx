package fr.sogeti.microservice;

import fr.sogeti.domain.Book;
import fr.sogeti.microservice.api.rest.RestVerticle;
import io.vertx.core.Vertx;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 *
 * @author fduneau
 */
public class Main {
    public static void main(String[] args) throws MqttException {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new RestVerticle<>("tcp://10.226.159.191:1883", "/books", 8080, "clientID", Book.class));
    }
}

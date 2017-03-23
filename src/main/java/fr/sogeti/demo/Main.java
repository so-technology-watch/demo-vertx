package fr.sogeti.demo;

import fr.sogeti.mqtt.PublisherMqtt;
import fr.sogeti.mqtt.SubscriberMqtt;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
public class Main extends AbstractVerticle  {

    public static final String BROKER_URL = "tcp://localhost:1883";

    public static void main(String[] args) throws Exception {
        Vertx vertx = Vertx.vertx();
        //vertx.deployVerticle(new ServerREST());
        //vertx.deployVerticle(new ClientREST());
        //vertx.deployVerticle(new ServerMqtt());
        
        vertx.deployVerticle(new SubscriberMqtt("Sub1", "topic", BROKER_URL));
        vertx.deployVerticle(new PublisherMqtt("pub1", "topic", "message1", BROKER_URL));

    }
    
}

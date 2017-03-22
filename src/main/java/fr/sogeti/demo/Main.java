package fr.sogeti.demo;

import fr.sogeti.mqtt.PublisherMqtt;
import fr.sogeti.mqtt.ServerMqtt;
import fr.sogeti.mqtt.SubscriberMqtt;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.impl.launcher.commands.VertxIsolatedDeployer;
public class Main extends AbstractVerticle  {
    
    public static void main(String[] args){
        Vertx vertx = Vertx.vertx();
        //vertx.deployVerticle(new ServerREST());
        //vertx.deployVerticle(new ClientREST());
        vertx.deployVerticle(new ServerMqtt());
        vertx.deployVerticle(new PublisherMqtt("pub1", "topic1", "message1"));
        vertx.deployVerticle(new SubscriberMqtt("Sub1", "topic"));
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.sogeti.mqtt;

import io.vertx.core.AbstractVerticle;
import io.vertx.mqtt.MqttServer;
import io.vertx.mqtt.MqttServerOptions;
import io.vertx.mqtt.MqttWill;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fduneau
 */
public class ServerMqtt extends AbstractVerticle {

    private static final Logger LOG = Logger.getLogger(ServerMqtt.class.getName());
    
    @Override
    public void start(){
        MqttServerOptions options = new MqttServerOptions();
        MqttServer mqttServer = MqttServer.create(vertx, options);
        
        mqttServer
            .endpointHandler( endpoint -> {
                if(LOG.isLoggable(Level.INFO))
                    LOG.log(Level.INFO, "client handled with id {0}", endpoint.clientIdentifier());
                MqttWill will = endpoint.will();
                if(will != null){
                    LOG.log(Level.INFO, String.format("[will topic = %s msg = %s QoS = %s isRetain = %s]",
                        will.willTopic(), will.willMessage(),will.willQos(),will.isWillRetain()));
                }
                endpoint.accept(false);
            } )
            .listen( ar -> {
                if(ar.succeeded()){
                    if(LOG.isLoggable(Level.INFO))
                        LOG.log(Level.INFO, "MQTT Server start listening on port {0}", ar.result().actualPort());
                }else{
                    if(LOG.isLoggable(Level.SEVERE)){
                        LOG.log(Level.SEVERE, "Failed to start the mqtt server");
                        ar.cause().printStackTrace();
                    }
                }
            } );
    }
}

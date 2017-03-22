/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.sogeti.mqtt;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.buffer.Buffer;
import io.vertx.mqtt.MqttEndpoint;
import io.vertx.mqtt.MqttServer;
import io.vertx.mqtt.MqttServerOptions;
import io.vertx.mqtt.MqttTopicSubscription;
import io.vertx.mqtt.messages.MqttPublishMessage;
import io.vertx.mqtt.messages.MqttSubscribeMessage;
import io.vertx.mqtt.messages.MqttUnsubscribeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
                
                endpoint.publishHandler( message -> {
                    
                    handlePublishHandler(message, endpoint);
                    
                } ).publishReleaseHandler( messageId -> {
                    
                    endpoint.publishComplete(messageId);
                    
                }).subscribeHandler( subscribe -> {
                    
                    handleSubscribe(subscribe, endpoint);
                    
                }).unsubscribeHandler( unsubscribe -> {
                
                    handleUnsubscribe(unsubscribe, endpoint);
                    
                } );
                
                endpoint.accept(false);
            } )
            .listen( ar -> {
                handleListenResult(ar);
            } );
    }

    private void handlePublishHandler(MqttPublishMessage message, MqttEndpoint endpoint) {
        int id = message.messageId();
        LOG.log(Level.INFO, "message received {0}", message.payload());
        switch( message.qosLevel() ){
            case AT_LEAST_ONCE:
                endpoint.publishAcknowledge(id);
                break;
            case EXACTLY_ONCE:
                endpoint.publishRelease(id);
                break;
        }
        if(message.qosLevel() != MqttQoS.FAILURE)
            sendMessage(message.topicName(), message.payload(), message.qosLevel(), message.isDup(), message.isRetain(), endpoint);
    }
    
    private void sendMessage(String topicName, Buffer buferedfMessage, MqttQoS mqttQoS, boolean isDup, boolean isRetain, MqttEndpoint endpoint){
        endpoint.publish(topicName, buferedfMessage, mqttQoS, isDup, isRetain);
        if(LOG.isLoggable(Level.INFO))
            LOG.log(Level.INFO, "sending message : {0}", buferedfMessage);
    }

    private void handleListenResult(AsyncResult<MqttServer> ar) {
        if(ar.succeeded()){
            if(LOG.isLoggable(Level.INFO))
                LOG.log(Level.INFO, "MQTT Server start listening on port {0}", ar.result().actualPort());
        }else{
            if(LOG.isLoggable(Level.SEVERE)){
                LOG.log(Level.SEVERE, "Failed to start the mqtt server");
                ar.cause().printStackTrace();
            }
        }
    }

    private void handleSubscribe(MqttSubscribeMessage subscribe, MqttEndpoint endpoint) {
        if(LOG.isLoggable(Level.INFO))
            LOG.log(Level.INFO, "sucribing message received with id {0}", subscribe.messageId());
        List<MqttQoS> grantedQosLevels = new ArrayList<>();
        subscribe
            .topicSubscriptions()
            .stream()
            .map(MqttTopicSubscription::qualityOfService)
            .forEach(grantedQosLevels::add);
        
        endpoint.subscribeAcknowledge(subscribe.messageId(), grantedQosLevels);
    }

    private void handleUnsubscribe(MqttUnsubscribeMessage unsubscribe, MqttEndpoint endpoint) {
        if(LOG.isLoggable(Level.INFO)){
            String topics = unsubscribe.topics().stream().collect(Collectors.joining());
            LOG.log(Level.INFO, "unsubscribe message received for topics : ", topics);
        }
        endpoint.unsubscribeAcknowledge(unsubscribe.messageId());
    }
}

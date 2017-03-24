/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.sogeti.microservice.api.mqtt;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 *
 * @author fduneau
 */
public class ClientMqtt implements IClientMqtt {
    private static final Logger LOG = Logger.getLogger(ClientMqtt.class.getName());

    private final String brokerUrl;
	private final MemoryPersistence persistence;
    private final MqttClient client;

    public ClientMqtt(String brokerUrl, String clientId) throws MqttException{
        this.brokerUrl = brokerUrl;
        persistence = new MemoryPersistence();
        client = new MqttClient(brokerUrl, clientId, persistence);
    }
    
    @Override
    public void sendMessage(String message, String topic, int qos) {
        reconnect();
        if(qos > 3 || qos < 0){
            throw new IllegalArgumentException("Invalid qos");
        }
        MqttMessage msg = new MqttMessage(message.getBytes());
        msg.setQos(qos);
        try{
            client.publish(topic, msg);
        }catch(MqttException e){
            if(LOG.isLoggable(Level.SEVERE)){
                LOG.log(Level.SEVERE, "Unable to send a message to the broker : {0}", e.getMessage());
            }
        }
    }
    
    @Override
    public void setCallback(MessageCallback messageCallback){
        client.setCallback(messageCallback);
    }
    
    @Override
    public void connect(){
        try{
            MqttConnectOptions connectOptions = new MqttConnectOptions();
            connectOptions.setCleanSession(true);
            if(!client.isConnected()){
                client.connect(connectOptions);
                if(LOG.isLoggable(Level.INFO)){
                    LOG.log(Level.INFO, "Client connected : {0}", client.getClientId());
                    LOG.log(Level.INFO, "Connected to {0}", brokerUrl);
                }
            }
        }catch(MqttException e){
            if(LOG.isLoggable(Level.SEVERE)){
                LOG.log(Level.SEVERE, "Unable to connect to the broker {0}", e.getMessage());
            }
        }
    }
    
    @Override
    public void disconnect() {
        try{
            if(client.isConnected()){
                client.disconnect();
                if(LOG.isLoggable(Level.INFO)){
                    LOG.log(Level.INFO, "Client disconnected : {0}", client.getClientId());
                }
            }
        }catch(MqttException e){
            if(LOG.isLoggable(Level.SEVERE)){
                LOG.log(Level.SEVERE, "Failed to stop client {0}", e.getMessage());
            }
        }
    }
    
    @Override
    public void subscribe(String topic) {
        reconnect();
        try{
            client.subscribe(topic);
        }catch(MqttException e){
            if(LOG.isLoggable(Level.SEVERE)){
                LOG.log(Level.SEVERE, "Failed to subscribe client to a topic {0}", e.getMessage());
            }
        }
    }
    
    @Override
    public void unsubscribe(String ... topics){
        for(String topic : topics){
            try{
                client.unsubscribe(topic);
            }catch(MqttException e){
                if(LOG.isLoggable(Level.SEVERE)){
                    LOG.log(Level.SEVERE, "Failed to unsubscribe client to a topic {0}", e.getMessage());
                }    
            }
        }
    }
    
    private void reconnect(){
        if(!client.isConnected()){
            try{
                client.reconnect();
            }catch(MqttException e){
                if(LOG.isLoggable(Level.SEVERE)){
                    LOG.log(Level.SEVERE, "Unable to reconnect the client {0}", e.getMessage());
                }
            }
        }
    }
}

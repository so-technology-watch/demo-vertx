package fr.sogeti.microservice.api.mqtt;

/**
 *
 * @author fduneau
 */
public interface IClientMqtt {

    void connect();

    void disconnect();

    void sendMessage(String message, String topic, int qos);

    void setCallback(MessageCallback messageCallback);
        
    void subscribe(String topic);
    
    void unsubscribe(String ... topics);
}

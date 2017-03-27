package fr.sogeti.mqtt;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import fr.sogeti.dao.DAO;
import fr.sogeti.domain.Book;
import fr.sogeti.domain.Element;
import fr.sogeti.vertx.VerticleBooks;

public class MessageRecievedCallback<T extends Element> implements MqttCallback {

    private ElementsMqtt<T> elementsMqtt;
    private DAO<T> dao;
    private Class<T> clazz;
    private Gson gson = new Gson();
    private final Logger LOG = Logger.getLogger(VerticleBooks.class.getName());
    // Paths to the different topics under publishing.
    private final String getPublishingPath;
    private final String getallPublishingPath;
    private final String postPublishingPath;
    private final String putPublishingPath;
    private final String deletePublishingPath;
    // Paths to the different topics under delivery.
    private final String getDeliveryPath;
    private final String getallDeliveryPath;
    private final String postDeliveryPath;
    private final String putDeliveryPath;
    private final String deleteDeliveryPath;
    private final String voidDeliveryPath;

    public MessageRecievedCallback(ElementsMqtt<T> elementsMqtt, Class<T> clazz, DAO<T> dao) {

	this.elementsMqtt = elementsMqtt;
	this.clazz = clazz;
	this.dao = dao;
	dao.init();
	getPublishingPath = elementsMqtt.getPublishTopic() + "/GET/.+";
	getallPublishingPath = elementsMqtt.getPublishTopic() + "/GETALL";
	postPublishingPath = elementsMqtt.getPublishTopic() + "/POST/.+";
	putPublishingPath = elementsMqtt.getPublishTopic() + "/PUT/.+";
	deletePublishingPath = elementsMqtt.getPublishTopic() + "/DELETE/.+";
	getallDeliveryPath = elementsMqtt.getDeliverTopic() + "/GETALL";
	getDeliveryPath = elementsMqtt.getDeliverTopic() + "/GET/.+";
	postDeliveryPath = elementsMqtt.getDeliverTopic() + "/POST/.+";
	putDeliveryPath = elementsMqtt.getDeliverTopic() + "/PUT/.+";
	deleteDeliveryPath = elementsMqtt.getDeliverTopic() + "/DELETE/.+";
	voidDeliveryPath = elementsMqtt.getDeliverTopic();

    }

    @Override
    public void connectionLost(Throwable arg0) {

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken arg0) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {

	LOG.log(Level.INFO, "Message sent on: {0}", topic);
	LOG.log(Level.INFO, "Message: {0}", new String(message.getPayload()));

	if (topic.matches(getPublishingPath)) {

	    handleGet(extractId(topic));

	} else if (topic.equals(getallPublishingPath)) {

	    handleGetAll();

	} else if (topic.matches(postPublishingPath)) {

	    handlePost(message, extractId(topic));

	} else if (topic.matches(putPublishingPath)) {

	    handlePut(extractId(topic), message);

	} else if (topic.matches(deletePublishingPath)) {

	    handleDelete(extractId(topic));

	} else {

	    handleVoid();

	}

    }

    /**
     * Handles messages coming on the GET subtopic by sending back the requested
     * item.
     * 
     * @param id
     */
    public void handleGet(int id) {

	try {
	    System.out.println(gson.toJson(dao.get(id)));
	    elementsMqtt.sendMessage(gson.toJson(dao.get(id)), 2, elementsMqtt.getDeliverTopic() + "/GET/" + id);

	} catch (MqttException e) {

	    LOG.log(Level.SEVERE, "{0}", e.getMessage());
	}

	LOG.log(Level.INFO, "REQUEST GET HAST BEEN HANDLED.");

    }

    /**
     * Handles messages coming on the GETALL subtopic by sending back the
     * requested items.
     */
    public void handleGetAll() {

	Type listType = new TypeToken<List<Book>>() {
	}.getType();
	try {
	    elementsMqtt.sendMessage(gson.toJson(dao.getAll(), listType), 2, getallDeliveryPath);
	} catch (MqttException e) {
	    e.printStackTrace();
	}

	LOG.log(Level.INFO, "REQUEST GETALL HAST BEEN HANDLED.");

    }

    /**
     * Handles messages coming on the POST subtopic by adding the new item and
     * send back DONE when done.
     * 
     * @param message
     * @param id
     */

    public void handlePost(MqttMessage message, int id) {

	dao.save(gson.fromJson(new String(message.getPayload(), StandardCharsets.UTF_8), clazz));
	try {
	    elementsMqtt.sendMessage("DONE", 2, elementsMqtt.getDeliverTopic() + "/POST/" + id);
	} catch (MqttException e) {
	    e.printStackTrace();
	}

	LOG.log(Level.INFO, "REQUEST POST HAST BEEN HANDLED.");

    }

    /**
     * Handles messages coming on the PUT subtopic by updating the requested
     * items, sends back "DONE" when done.
     * 
     * @param id
     * @param message
     */
    public void handlePut(int id, MqttMessage message) {

	T updatedBook = gson.fromJson(new String(message.getPayload(), StandardCharsets.UTF_8), clazz);
	dao.update(updatedBook);
	try {
	    elementsMqtt.sendMessage("DONE", 2, elementsMqtt.getDeliverTopic() + "/PUT/" + id);
	} catch (MqttException e) {
	    e.printStackTrace();
	}

	LOG.log(Level.INFO, "REQUEST PUT HAST BEEN HANDLED.");

    }

    /**
     * Handles messages coming on the DELETE subtopic by deleting the requested
     * items, sends back "DONE" when done.
     * 
     * @param id
     */
    public void handleDelete(int id) {

	dao.delete(id);
	try {
	    elementsMqtt.sendMessage("DONE", 2, elementsMqtt.getDeliverTopic() + "/DELETE/" + id);
	} catch (MqttException e) {
	    e.printStackTrace();
	}

	LOG.log(Level.INFO, "REQUEST DELETE HAS BEEN HANDLED.");

    }

    /**
     * Handles messages coming directly on the root of the topic by sending back
     * an error to the user.
     */

    public void handleVoid() {

	try {
	    elementsMqtt.sendMessage("UNKNOWN PATH", 2, voidDeliveryPath);
	    LOG.log(Level.INFO, "REQUEST HAS NOT BEEN HANDLED");

	} catch (MqttException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    /**
     * Extracts the id from the the topic.
     * 
     * @param topic
     * @return
     */

    private int extractId(String topic) {

	String[] splitedTopic = topic.split("/");

	return Integer.parseInt(splitedTopic[splitedTopic.length - 1]);

    }
}

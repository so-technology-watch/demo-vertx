package fr.sogeti.mqtt;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import fr.sogeti.dao.BookDAO;
import fr.sogeti.domain.Book;
import fr.sogeti.vertx.VerticleBooks;

public class MessageRecievedCallback implements MqttCallback {

    private BooksMqtt booksMqtt;
    private BookDAO bookDAO;
    private Gson gson = new Gson();
    private static final Logger LOG = Logger.getLogger(VerticleBooks.class.getName());
    // Paths to the different topics under publishing.
    private static final String GET_PUBLISHING_PATH = BooksMqtt.PUBLISH_TOPIC + "/GET/.+";
    private static final String GETALL_PUBLISHING_PATH = BooksMqtt.PUBLISH_TOPIC + "/GETALL";
    private static final String POST_PUBLISHING_PATH = BooksMqtt.PUBLISH_TOPIC + "/POST/.+";
    private static final String PUT_PUBLISHING_PATH = BooksMqtt.PUBLISH_TOPIC + "/PUT/.+";
    private final String DELETE_PUBLISHING_PATH = BooksMqtt.PUBLISH_TOPIC + "/DELETE/.+";
    // Paths to the different topics under delivery.
    private static final String GET_DELIVERY_PATH = BooksMqtt.DELIVER_TOPIC + "/GET/";
    private static final String GETALL_DELIVERY_PATH = BooksMqtt.DELIVER_TOPIC + "/GETALL";
    private static final String POST_DELIVERY_PATH = BooksMqtt.DELIVER_TOPIC + "/POST/";
    private static final String PUT_DELIVERY_PATH = BooksMqtt.DELIVER_TOPIC + "/PUT/";
    private static final String DELETE_DELIVETY_PATH = BooksMqtt.DELIVER_TOPIC + "/DELETE/";
    private static final String VOID_DELIVETY_PATH = BooksMqtt.DELIVER_TOPIC;

    public MessageRecievedCallback(BooksMqtt booksMqtt) {

	this.booksMqtt = booksMqtt;
	this.bookDAO = new BookDAO();
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

	if (topic.matches(GET_PUBLISHING_PATH)) {

	    handleGet(extractId(topic));

	} else if (topic.equals(GETALL_PUBLISHING_PATH)) {

	    handleGetAll();

	} else if (topic.matches(POST_PUBLISHING_PATH)) {

	    handlePost(message, extractId(topic));

	} else if (topic.matches(PUT_PUBLISHING_PATH)) {

	    handlePut(extractId(topic), message);

	} else if (topic.matches(DELETE_PUBLISHING_PATH)) {

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
	    System.out.println(gson.toJson(bookDAO.get(id)));
	    booksMqtt.sendMessage(gson.toJson(bookDAO.get(id)), 2, GET_DELIVERY_PATH + id);

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

	Type listType = new TypeToken<Map<Integer, Book>>() {
	}.getType();
	try {
	    booksMqtt.sendMessage(gson.toJson(bookDAO.getAll(), listType), 2, GETALL_DELIVERY_PATH);
	} catch (MqttException e) {
	    e.printStackTrace();
	}

	LOG.log(Level.INFO, "REQUEST GETALL HAST BEEN HANDLED.");

    }

    /**
     * Handles messages coming on the POST subtopic by adding the new item and
     * sending back it's id.
     * 
     * @param message
     * @param id
     */

    public void handlePost(MqttMessage message, int id) {

	Book book = bookDAO.save(gson.fromJson(new String(message.getPayload(), StandardCharsets.UTF_8), Book.class));
	try {
	    booksMqtt.sendMessage(gson.toJson(book), 2, POST_DELIVERY_PATH + id);
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

	Book updatedBook = gson.fromJson(new String(message.getPayload(), StandardCharsets.UTF_8), Book.class);
	bookDAO.update(updatedBook);
	try {
	    booksMqtt.sendMessage("DONE", 2, PUT_DELIVERY_PATH + id);
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

	bookDAO.delete(id);
	try {
	    booksMqtt.sendMessage("DONE", 2, DELETE_DELIVETY_PATH + id);
	} catch (MqttException e) {
	    e.printStackTrace();
	}

	LOG.log(Level.INFO, "REQUEST DELETE HAST BEEN HANDLED.");

    }

    /**
     * Handles messages coming directly on the root of the topic by sending back
     * an error to the user.
     */

    public void handleVoid() {

	try {
	    booksMqtt.sendMessage("UNKNOWN PATH", 2, VOID_DELIVETY_PATH);
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

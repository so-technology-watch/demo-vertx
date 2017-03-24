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
import fr.sogti.main.VerticleBooks;

public class MessageRecievedCallback implements MqttCallback {

    private BooksMqtt booksMqtt;
    private BookDAO bookDAO;
    private Gson gson = new Gson();
    private static final Logger LOG = Logger.getLogger(VerticleBooks.class.getName());

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

	if (topic.matches("publish/books/GET/.+")) {

	    handleGet(extractId(topic));

	} else if (topic.equals("publish/books/GETALL")) {

	    handleGetAll();

	} else if (topic.matches("publish/books/POST/.+")) {

	    handlePost(message, extractId(topic));

	} else if (topic.matches("publish/books/PUT/.+")) {

	    handlePut(extractId(topic), message);

	} else if (topic.matches("publish/books/DELETE/.+")) {

	    handleDelete(extractId(topic));

	} else {

	    handleVoid();

	}

    }

    public void handleGet(int id) {

	try {
	    System.out.println(gson.toJson(bookDAO.get(id)));
	    booksMqtt.sendMessage(gson.toJson(bookDAO.get(id)), 2, booksMqtt.getDELIVER_TOPIC() + "/GET/" + id);

	} catch (MqttException e) {

	    LOG.log(Level.SEVERE, "{0}", e.getMessage());
	}

	LOG.log(Level.INFO, "REQUEST GET HAST BEEN HANDLED.");

    }

    public void handleGetAll() {

	Type listType = new TypeToken<Map<Integer, Book>>() {
	}.getType();
	try {
	    booksMqtt.sendMessage(gson.toJson(bookDAO.getAll(), listType), 2, booksMqtt.getDELIVER_TOPIC() + "/GETALL");
	} catch (MqttException e) {
	    e.printStackTrace();
	}

	LOG.log(Level.INFO, "REQUEST GETALL HAST BEEN HANDLED.");

    }

    public void handlePost(MqttMessage message, int id) {

	Book book = bookDAO.save(gson.fromJson(new String(message.getPayload(), StandardCharsets.UTF_8), Book.class));
	try {
	    booksMqtt.sendMessage(gson.toJson(book), 2, booksMqtt.getDELIVER_TOPIC() + "/POST/" + id);
	} catch (MqttException e) {
	    e.printStackTrace();
	}

	LOG.log(Level.INFO, "REQUEST POST HAST BEEN HANDLED.");

    }

    public void handlePut(int id, MqttMessage message) {

	Book updatedBook = gson.fromJson(new String(message.getPayload(), StandardCharsets.UTF_8), Book.class);
	bookDAO.update(updatedBook);
	try {
	    booksMqtt.sendMessage("DONE", 2, booksMqtt.getDELIVER_TOPIC() + "/PUT/" + id);
	} catch (MqttException e) {
	    e.printStackTrace();
	}

	LOG.log(Level.INFO, "REQUEST PUT HAST BEEN HANDLED.");

    }

    public void handleDelete(int id) {

	bookDAO.delete(id);
	try {
	    booksMqtt.sendMessage("DONE", 2, booksMqtt.getDELIVER_TOPIC() + "/DELETE/" + id);
	} catch (MqttException e) {
	    e.printStackTrace();
	}

	LOG.log(Level.INFO, "REQUEST DELETe HAST BEEN HANDLED.");

    }

    public void handleVoid() {

	try {
	    booksMqtt.sendMessage("UNKNOWN PATH", 2, booksMqtt.getDELIVER_TOPIC() + "/GETALL");
	    System.out.println(booksMqtt.getDELIVER_TOPIC() + "/GETALL");
	    LOG.log(Level.INFO, "REQUEST HAS NOT BEEN HANDLED");

	} catch (MqttException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    private int extractId(String topic) {

	String[] splitedTopic = topic.split("/");

	return Integer.parseInt(splitedTopic[splitedTopic.length - 1]);

    }
}

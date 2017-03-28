package fr.sogeti.kafka;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class ProducerKafka {

    private static Properties properties;
    private Producer<String, String> producer;
    private static final Logger LOG = Logger.getLogger(ProducerKafka.class.getName());

    public ProducerKafka(Properties properties) {

	this.producer = new KafkaProducer<String, String>(properties);

    }

    public void sendMessage(String topic, String message) {

	LOG.log(Level.INFO, "Sending message on topic : {0}", topic);
	producer.send(new ProducerRecord<String, String>(topic, message));
	LOG.log(Level.INFO, "Message {0} has been sent", message);

    }

    public void close() {

	producer.close();
    }

    public static Properties getProperties() {

	return properties;
    }

    public static void setProperties(Properties properties) {

	ProducerKafka.properties = properties;
    }

    public Producer<String, String> getProducer() {

	return producer;
    }

    public void setProducer(Producer<String, String> producer) {

	this.producer = producer;
    }

}

package fr.sogeti.kafka;

import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;

public class ConsumerKafka {

    private Properties properties;
    private KafkaConsumer<String, String> consumer;
    private static final Logger LOG = Logger.getLogger(ConsumerKafka.class.getName());

    public ConsumerKafka(Properties properties) {

	this.properties = properties;
	consumer = new KafkaConsumer<>(properties);

    }

    public void subscribe(String topic) {

	consumer.assign(Arrays.asList(new TopicPartition(topic, 0)));
	LOG.log(Level.INFO, "Subscribing to : {0}", topic);
    }

    public void consumeAll() {

	new Thread(() -> {
	    try {
		while (true) {
		    LOG.log(Level.INFO, "Attempt to consume all the records 2");
		    ConsumerRecords<String, String> records = consumer.poll(1000);
		    LOG.info("after");

		    records.forEach(e -> {
			System.out.println("ça marche");
			System.out.printf("offset = %d, key = %s, value = %s\n", e.offset(), e.key(), e.value());
		    });

		    LOG.log(Level.INFO, "Displaying records DONE");

		}
	    } catch (WakeupException e) {

		e.printStackTrace();
	    } finally {
		consumer.close();
	    }

	}).start();
    }

    public void close() {

	consumer.close();
    }

    public Properties getProperties() {

	return properties;
    }

    public void setProperties(Properties properties) {

	this.properties = properties;
    }

    public KafkaConsumer<String, String> getConsumer() {

	return consumer;
    }

    public void setConsumer(KafkaConsumer<String, String> consumer) {

	this.consumer = consumer;
    }

}

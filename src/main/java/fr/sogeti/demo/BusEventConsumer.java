package fr.sogeti.demo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fduneau
 */
public class BusEventConsumer extends AbstractVerticle {

    private static final Logger LOG = Logger.getLogger(BusEventConsumer.class.getName());
    
    @Override
    public void start(){
        EventBus eb = vertx.eventBus();

        MessageConsumer<String> consumer = eb.consumer("news.uk.sport");
        consumer.handler(message -> {
          LOG.log(Level.INFO, "I have received a message consumer: {0}", message.body());
        });
    }
}

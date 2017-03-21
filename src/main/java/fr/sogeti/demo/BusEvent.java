/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.sogeti.demo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.ext.web.Router;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fduneau
 */
public class BusEvent extends AbstractVerticle{

    private static final Logger LOG = Logger.getLogger(BusEvent.class.getName());
    
    @Override    
    public void start(){
        Router router = Router.router(vertx);
        router.route().handler( context -> {
            LOG.log(Level.INFO, context.response().getStatusMessage());
        });
        
        EventBus eb = vertx.eventBus();

        eb.consumer("news.uk.sport", message -> {
          LOG.log(Level.INFO, "I have received a message: {0}", message.body());
        });
    }
}

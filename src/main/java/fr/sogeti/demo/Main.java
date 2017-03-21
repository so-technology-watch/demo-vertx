package fr.sogeti.demo;

import fr.sogeti.server.ServerREST;
import fr.sogeti.client.ClientREST;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
public class Main extends AbstractVerticle  {
    
    public static void main(String[] args){
        Vertx vertx = Vertx.vertx();
        //Verticle verticle = new Msg();
        //Verticle verticle = new Index();
        Verticle verticle = new ServerREST();
        vertx.deployVerticle(verticle);
        vertx.deployVerticle(new ClientREST());
        //vertx.deployVerticle(new BusEvent());
        /*
        EventBus eb = vertx.eventBus();
        eb.send("news.uk.sport", "Coucou message", (reply) -> {
            System.out.println(reply.result());
        });
        */
    }
    
}

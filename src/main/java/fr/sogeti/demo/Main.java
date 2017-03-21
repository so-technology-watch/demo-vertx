package fr.sogeti.demo;

import fr.sogeti.rest.ClientREST;
import fr.sogeti.rest.ServerREST;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
public class Main extends AbstractVerticle  {
    
    public static void main(String[] args){
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new ServerREST());
        vertx.deployVerticle(new ClientREST());
    }
    
}

package fr.sogeti.demo;

import fr.sogeti.rest.ClientREST;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
public class MainClient extends AbstractVerticle  {
    
    public static void main(String[] args){
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new ClientREST());
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.sogeti.server;

import com.google.gson.Gson;
import fr.sogeti.beans.Book;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fduneau
 */
public class ServerREST extends AbstractVerticle{
    private final Map<Integer, Book> books;
    private static final Logger LOG = Logger.getLogger(ServerREST.class.getName());
    
    public ServerREST(){
        LOG.log(Level.INFO, "init rest server");
        books = new HashMap<>();
        books.put(1, new Book("Le chien de baskeville", "S A. Conan Doyles"));
        books.put(2, new Book("Une étude en rouge", "S A. Conan Doyles"));
    }
    @Override
    public void start(){
        Router router = Router.router(vertx);
        router.get("/rest").handler( ctx -> {
            HttpServerResponse response = ctx.response();
            response.putHeader("content-type", "application/json");
            response.end(new Gson().toJson(books));
        });
        
        vertx.createHttpServer().requestHandler(router::accept).listen(8080);
        
        router.post("/rest").handler( ctx -> {
            HttpServerResponse response = ctx.response();
            response.putHeader("content-type", "application/json");
            LOG.log(Level.INFO, "data POSTED on the server {0}", ctx.getBody());
            //books.put(books.size() + 1, ctx.getBodyAsJson());
            response.end("success");
        });
        
        vertx.createHttpServer().requestHandler(router::accept).listen(8080);
    }
    
}

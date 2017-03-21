/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.sogeti.rest;

import com.google.gson.Gson;
import fr.sogeti.dao.BookDAO;
import fr.sogeti.domain.Book;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fduneau
 */
public class ServerREST extends AbstractVerticle{
    private static final Logger LOG = Logger.getLogger(ServerREST.class.getName());
    private BookDAO bookDAO = new BookDAO();
    
    public ServerREST(){
        LOG.log(Level.INFO, "init rest server");
    }
    
    @Override
    public void start(){
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        vertx.createHttpServer().requestHandler(router::accept).listen(8080);
        getAll(router);
        create(router);
        //get(router);
        //update(router);
        //delete(router);
    }
    
    protected void getAll(Router router) {
        Map<Integer, Book> books = bookDAO.getAll();
        router.get("/books").handler( ctx -> {
            HttpServerResponse response = ctx.response();
            response.putHeader("content-type", "application/json");
            response.end(new Gson().toJson(books));
        });
    }
    
    protected void create(Router router) {
        router.post("/books").handler( ctx -> {
            HttpServerResponse response = ctx.response();
            response.putHeader("content-type", "application/json");
            LOG.log(Level.INFO, "data POSTED on the server {0}", ctx.getBodyAsJson());
            //books.put(books.size() + 1, ctx.getBodyAsJson());
            response.end("success");
        });
    }
    
    
    
}

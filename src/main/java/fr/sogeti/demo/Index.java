/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.sogeti.demo;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

/**
 *
 * @author fduneau
 */
public class Index extends AbstractVerticle {
    @Override
    public void start(){
        
        Router router = Router.router(vertx);

        router.route().handler(StaticHandler.create().setIndexPage("/index.html"));
        
        vertx.createHttpServer().requestHandler(router::accept).listen(8080);
        
    }
}

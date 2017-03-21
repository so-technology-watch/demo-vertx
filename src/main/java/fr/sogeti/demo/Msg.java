/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.sogeti.demo;

import com.google.gson.Gson;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;

/**
 *
 * @author fduneau
 */
public class Msg extends AbstractVerticle{
    static class MsgToSend {
        private final String msg;
        private MsgToSend(String msg){
            this.msg = msg;
        }
    }
    @Override
    public void start(){
        Router router = Router.router(vertx);

        router.get().handler( context -> {
            HttpServerResponse response = context.response();
            response.putHeader("content-type", "application/json");
            response.end(new Gson().toJson(new MsgToSend("hello")));
        });
        
        vertx.createHttpServer().requestHandler(router::accept).listen(8080);
    }
}

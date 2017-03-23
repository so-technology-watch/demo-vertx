package fr.sogeti.microservice.api.rest;

import com.google.gson.Gson;
import fr.sogeti.microservice.api.mqtt.IMqttAccess;
import fr.sogeti.microservice.api.mqtt.MqttAccess;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fduneau
 * T the type object the objejct manipulated by this verticle
 */
public class RestVerticle<T> extends AbstractVerticle{
    public static final String JSON_MIME = "application/json";
    private static final Logger LOG = Logger.getLogger(RestVerticle.class.getName());

    private final IMqttAccess<T> accessMqtt;
    private final Class<T> clazz;
    public final String route;
    private final int port;

    public RestVerticle(String route, int port, Class<T> clazz){
        this.route = route;
        this.port = port;
        this.clazz = clazz;
        accessMqtt = new MqttAccess<>(route);
    }
    
    @Override
    public void start(){
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        
        router.get(route)
            .produces(JSON_MIME)
            .consumes(JSON_MIME)
            .handler(this::handleGetRequest)
            .failureHandler(this::handleError);
        router.post(route)
            .produces(JSON_MIME)
            .consumes(JSON_MIME)
            .handler(this::handlePostRequest)
            .failureHandler(this::handleError);
        router.put(route)
            .produces(JSON_MIME)
            .consumes(JSON_MIME)
            .handler(this::handlePutRequest)
            .failureHandler(this::handleError);
        router.delete(route)
            .produces(JSON_MIME)
            .consumes(JSON_MIME)
            .handler(this::handleDeleteRequest)
            .failureHandler(this::handleError);
        
        vertx.createHttpServer().requestHandler(router::accept).listen(port);
    }
    
    private void handleGetRequest(RoutingContext requestCtx) {
        HttpServerRequest request = requestCtx.request();
        HttpServerResponse response = requestCtx.response();
        String idStr = request.getParam("id");
        Gson gson = new Gson();
        String jsonResponse = "";
        
        if(idStr == null){
            // if there is no id, getAll()
            jsonResponse = gson.toJson(accessMqtt.getAll());
        }else{
            if(isInteger(idStr)){
                int id = Integer.parseInt(idStr);
                T type = accessMqtt.get(id);
                if(accessMqtt != null){
                    jsonResponse = gson.toJson(type);
                }
            }
        }
        
        response.end(jsonResponse);
    }
    
    private void handlePostRequest(RoutingContext requestCtx) {
        HttpServerResponse response = requestCtx.response();
        Gson gson = new Gson();
        String jsonResponse = "";
        String bookJson = requestCtx.getBodyAsString();
        
        if(bookJson != null){
            T type = gson.fromJson(bookJson, clazz);
            type = accessMqtt.save(type);
            jsonResponse = gson.toJson(type);
        }
        
        response.end(jsonResponse);
    }
    
    private void handlePutRequest(RoutingContext requestCtx) {
        HttpServerResponse response = requestCtx.response();
        Gson gson = new Gson();
        String jsonResponse = "";
        String bookStr = requestCtx.getBodyAsString();
        
        if(bookStr != null){
            T book = gson.fromJson(bookStr, clazz);
            accessMqtt.update(book);
        }
        
        response.end(jsonResponse);
    }
    
    private void handleDeleteRequest(RoutingContext requestCtx) {
        HttpServerResponse response = requestCtx.response();
        HttpServerRequest request = requestCtx.request();
        
        String idStr = request.getParam("id");
        
        if(idStr != null && isInteger(idStr)){
            int id = Integer.parseInt(idStr);
            accessMqtt.delete(id);
        }
        
        response.setStatusCode(HttpResponseStatus.OK.code());
    }

    private void handleError(RoutingContext failure) {
        if(LOG.isLoggable(Level.WARNING)){
            LOG.log(Level.WARNING, "a request failed with the error : {0}", failure.failure().getMessage());
            LOG.log(Level.WARNING, "the request fail was on {0}", failure.currentRoute().getPath());
        }
    }
    
    private boolean isInteger(String idStr){
        return idStr.matches("\\d+");
    }
}

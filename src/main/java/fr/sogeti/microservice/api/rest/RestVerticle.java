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
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 *
 * @author fduneau
 * T the type object the objejct manipulated by this verticle
 */
public class RestVerticle<T> extends AbstractVerticle {
    public static final String JSON_MIME = "application/json";
    private static final Logger LOG = Logger.getLogger(RestVerticle.class.getName());

    private IMqttAccess<T> accessMqtt;
    private final Class<T> clazz;
    public final String route;
    private final int port;

    public RestVerticle(String url, String route, int port, String clientId, Class<T> clazz) throws MqttException{
        this.route = route;
        this.port = port;
        this.clazz = clazz;
        accessMqtt = new MqttAccess<>(url, route, clientId);
    }
    
    @Override
    public void start(){
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        
        router.get(route)
            .produces(JSON_MIME)
            .handler(this::handleGetRequest)
            .failureHandler(this::handleError);
        router.post(route)
            .produces(JSON_MIME)
            .handler(this::handlePostRequest)
            .failureHandler(this::handleError);
        router.put(route)
            .produces(JSON_MIME)
            .handler(this::handlePutRequest)
            .failureHandler(this::handleError);
        router.delete(route)
            .produces(JSON_MIME)
            .handler(this::handleDeleteRequest)
            .failureHandler(this::handleError);
        
        vertx.createHttpServer().requestHandler(router::accept).listen(port);
        if(LOG.isLoggable(Level.INFO)){
            LOG.log(Level.INFO, "Verticle started on route : {0}", route);
        }
    }
    
    private void handleGetRequest(RoutingContext requestCtx) {
        HttpServerRequest request = requestCtx.request();
        HttpServerResponse response = requestCtx.response();
        String idStr = request.getParam("id");
        Gson gson = new Gson();

        if(idStr == null){
            // if there is no id, getAll()
            accessMqtt.getAll( json -> {
                response.end(gson.toJson(json));
            } );
        }else{
            if(isInteger(idStr)){
                int id = Integer.parseInt(idStr);
                accessMqtt.get(id , json -> {
                    response.end(json);
                } );
            }
        }
    }
    
    private void handlePostRequest(RoutingContext requestCtx) {
        HttpServerResponse response = requestCtx.response();
        Gson gson = new Gson();
        String bookJson = requestCtx.getBodyAsString();
        
        if(bookJson != null){
            T type = gson.fromJson(bookJson, clazz);
            accessMqtt.save(type, json -> {
                response.end(json);
            });
        }
        
    }
    
    private void handlePutRequest(RoutingContext requestCtx) {
        HttpServerResponse response = requestCtx.response();
        Gson gson = new Gson();
        String bookStr = requestCtx.getBodyAsString();
        
        if(bookStr != null){
            T book = gson.fromJson(bookStr, clazz);
            accessMqtt.update(book, json -> {
                response.end(json);
            });
        }
    }
    
    private void handleDeleteRequest(RoutingContext requestCtx) {
        HttpServerResponse response = requestCtx.response();
        HttpServerRequest request = requestCtx.request();
        
        String idStr = request.getParam("id");
        
        if(idStr != null && isInteger(idStr)){
            int id = Integer.parseInt(idStr);
            accessMqtt.delete(id, json -> {
                response.setStatusCode(HttpResponseStatus.OK.code());
            });
        }
    }

    private void handleError(RoutingContext failure) {
        if(LOG.isLoggable(Level.WARNING)){
            LOG.log(Level.WARNING, "a request failed with the error : {0}", failure.failure().getMessage());
            LOG.log(Level.WARNING, "the request fail was on {0}", failure.currentRoute().getPath());
        }
        failure.response().end("ERROR");
    }
    
    private boolean isInteger(String idStr){
        return idStr.matches("\\d+");
    }
    
    protected Class<T> getType(){
        return clazz;
    }
}

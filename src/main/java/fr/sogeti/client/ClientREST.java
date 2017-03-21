package fr.sogeti.client;

import fr.sogeti.beans.Book;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fduneau
 */
public class ClientREST extends AbstractVerticle{

    private static final Logger LOG = Logger.getLogger(ClientREST.class.getName());
    
    @Override
    public void start(){
        WebClient client = WebClient.create(vertx);
        get(client);
        post(client);
        get(client);
    }
    private void post(WebClient client){
        client
          .post(8080, "localhost", "/rest")
          .sendJson(new Book("Un scandale en boème", "S. A. Conan Doyles"), ar -> {
            if (ar.succeeded()) {
                HttpResponse<Buffer> response = ar.result();
                LOG.log(Level.INFO, "Received response with status code (POST) {0}", response.statusCode());
                LOG.log(Level.INFO, "Response was : {0}", response.body());
            } else {
                LOG.log(Level.INFO, "Something went wrong {0}", ar.cause().getMessage());
            }
          });        
    }
    
    private void get(WebClient client){
        client
          .get(8080, "localhost", "/rest")
          .send(ar -> {
            if (ar.succeeded()) {
                HttpResponse<Buffer> response = ar.result();
                LOG.log(Level.INFO, "Received response with status code {0}", response.statusCode());
                LOG.log(Level.INFO, "Response was : {0}", response.body());
            } else {
                LOG.log(Level.INFO, "Something went wrong {0}", ar.cause().getMessage());
            }
          });
    }
}

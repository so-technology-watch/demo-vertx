package fr.sogeti.rest;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fr.sogeti.domain.Book;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.function.Function;
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
        //Book book = new Book("Un scandale en boème", "S. A. Conan Doyles");
        
        getAll((books) -> {
            System.out.println(books);
            
            get(1, (book) -> {
                System.out.println(book.getNom());
                
                Book bookTrAdd = new Book(10, "Dix petits nègres", "S A Conan Doyles");
                create(bookTrAdd, (addedBook) -> {
                    addedBook.setAuteur("Agatha Cristie");
                    update(addedBook);
                    
                    return null;
                });
                
                return null;
            });
            
            return null;
        });
    }
    
    private void getAll(Function<Map<Integer, Book>, Void> f){
        WebClient client = WebClient.create(vertx);
        client.get(8080, "localhost", "/books").send(ar -> {
            if (ar.succeeded()) {
                HttpResponse<Buffer> response = ar.result();
                if(LOG.isLoggable(Level.INFO)) {
                    LOG.log(Level.INFO, "Received response with status code {0}", response.statusCode());
                    LOG.log(Level.INFO, "Response was : {0}", response.body());
                }
                Type type = new TypeToken<Map<Integer, Book>>(){}.getType();
                Map<Integer, Book> books = new Gson().fromJson(response.bodyAsString(), type);
                f.apply(books);
            } else {
                LOG.log(Level.INFO, "Something went wrong {0}", ar.cause().getMessage());
            }
        });
    }
    
    private void get(Integer id, Function<Book, Void> f){
        WebClient client = WebClient.create(vertx);
        client.get(8080, "localhost", "/books").setQueryParam("id", id.toString()).send(ar -> {
            if (ar.succeeded()) {
                HttpResponse<Buffer> response = ar.result();
                if(LOG.isLoggable(Level.INFO)) {
                    LOG.log(Level.INFO, "Received response with status code {0}", response.statusCode());
                    LOG.log(Level.INFO, "Response was : {0}", response.bodyAsString());
                }
                Book book = new Gson().fromJson(response.bodyAsString(), Book.class);
                f.apply(book);
            } else {
                if(LOG.isLoggable(Level.INFO)) {
                    LOG.log(Level.INFO, "Something went wrong {0}", ar.cause().getMessage());
                }
            }
        });
    }
    
    private void create(Book book, Function<Book, Void> callbackCreated){
        WebClient client = WebClient.create(vertx);
        
        client
          .post(8080, "localhost", "/books")
          .sendJson(book, ar -> {
            if (ar.succeeded()) {
                HttpResponse<Buffer> response = ar.result();
                if(LOG.isLoggable(Level.INFO)) {
                    LOG.log(Level.INFO, "Received response with status code (POST) {0}", response.statusCode());
                    LOG.log(Level.INFO, "Response was : {0}", response.body());
                }
                Book newBook = new Gson().fromJson(response.bodyAsString(), Book.class);
                callbackCreated.apply(newBook);
            } else {
                if(LOG.isLoggable(Level.INFO)) {
                    LOG.log(Level.INFO, "Something went wrong {0}", ar.cause().getMessage());
                }
            }
          });
    }

    private void update(Book book){
        WebClient client = WebClient.create(vertx);
        
        client
          .put(8080, "localhost", "/books")
          .sendJson(book, ar -> {
            if (ar.succeeded()) {
                HttpResponse<Buffer> response = ar.result();
                if(LOG.isLoggable(Level.INFO)) {
                    LOG.log(Level.INFO, "Received response with status code (PUT) {0}", response.statusCode());
                    LOG.log(Level.INFO, "Response was : {0}", response.body());
                }
            } else {
                if(LOG.isLoggable(Level.INFO)) {
                    LOG.log(Level.INFO, "Something went wrong {0}", ar.cause().getMessage());
                }
            }
          });
    }
    



	public void delete(int id){

		WebClient client = WebClient.create(vertx);

		client.delete(8080, "localhost", "/books").send(ar -> {

			if(ar.succeeded()){

				HttpResponse<Buffer> response = ar.result();
				
				if(LOG.isLoggable(Level.INFO)){
					
					LOG.log(Level.INFO, "Received response with status code (DELETE) {0}", response.statusCode());
					LOG.log(Level.INFO, "Response was : {0}", response.body());
				}

			} else {
				
				if(LOG.isLoggable(Level.INFO)) {
					LOG.log(Level.INFO, "Something went wrong {0}", ar.cause().getMessage());
				}
			}

		});

	}


}

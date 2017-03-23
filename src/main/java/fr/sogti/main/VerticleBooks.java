package fr.sogti.main;

import java.util.logging.Logger;

import fr.sogeti.mqtt.BooksMqtt;
import io.vertx.core.AbstractVerticle;

public class VerticleBooks extends AbstractVerticle{

	private String serverURI;
	private String clientId;
	BooksMqtt booksMqtt;
    private static final Logger LOG = Logger.getLogger(VerticleBooks.class.getName());



	public VerticleBooks(String serverURI, String clientId) {

		this.serverURI = serverURI;
		this.clientId = clientId;
	}

	public void start(){

		booksMqtt.launch();
	
	}

}

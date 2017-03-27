package fr.sogeti.main;

import fr.sogeti.vertx.VerticleBooks;
import io.vertx.core.Vertx;

public class Main {

    public static final String BROKER_URL = "tcp://localhost:1883";

    public static void main(String[] args) {

	Vertx vertx = Vertx.vertx();
	VerticleBooks verticleBooks = new VerticleBooks(BROKER_URL, "microServiceMQTT");
	vertx.deployVerticle(verticleBooks);

    }

}

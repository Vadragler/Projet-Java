package org.example;


import org.example.Connection.Client;
import org.example.Connection.Server;

public class Main {
        public static void main(String[] args) {

            // Création et démarrage du serveur sur une thread
            Thread serverThread = new Thread(() -> {
                Server server = new Server();
                server.start(5000);
            });
            serverThread.start();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // Création et démarrage du client sur une autre thread
            Thread clientThread = new Thread(() -> {
                Client client = new Client();
                client.start("localhost", 5000);
            });
            clientThread.start();
        }
}
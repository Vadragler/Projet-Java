package org.example.Connection;

import org.example.Livre.Lecteur;
import org.example.Livre.Livre;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private Connection connection;
    private PreparedStatement insertLivreStatement;
    private PreparedStatement insertLecteurStatement;

    public void start(int port) {
        try {
            // Établissement de la connexion à la base de données MySQL
            String url = "jdbc:mysql://localhost:3306/mabd?useSSL=false";
            String user = "root";
            String password = "root";
            connection = DriverManager.getConnection(url, user, password);

            // Préparation des requêtes SQL pour insérer des Livres ou des Lecteurs dans la base de données
            insertLivreStatement = connection.prepareStatement("INSERT INTO Livres (titre, auteur) VALUES (?, ?)");
            insertLecteurStatement = connection.prepareStatement("INSERT INTO Lecteurs (nom, prenom) VALUES (?, ?)");

            // Démarrage du serveur sur le port spécifié
            serverSocket = new ServerSocket(port);
            System.out.println("Serveur démarré sur le port " + port);

            // Attente d'une connexion client
            clientSocket = serverSocket.accept();
            System.out.println("Client connecté : " + clientSocket.getInetAddress().getHostAddress());

            // Ouverture du flux d'entrée pour recevoir des objets envoyés par le client
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());

            // Boucle infinie pour recevoir des objets tant que le client est connecté
            while (true) {
                // Réception d'un objet envoyé par le client
                Object object = inputStream.readObject();

                if (object instanceof Livre) {
                    // Si l'objet est un Livre, on l'insère dans la table Livres de la base de données
                    Livre livre = (Livre) object;
                    insertLivreStatement.setString(1, livre.getTitre());
                    insertLivreStatement.setString(2, livre.getAuteur());
                    insertLivreStatement.executeUpdate();
                    System.out.println("Livre inséré : " + livre);
                } else if (object instanceof Lecteur) {
                    // Si l'objet est un Lecteur, on l'insère dans la table Lecteurs de la base de données
                    Lecteur lecteur = (Lecteur) object;
                    insertLecteurStatement.setString(1, lecteur.getNom());
                    insertLecteurStatement.setString(2, lecteur.getPrenom());
                    insertLecteurStatement.executeUpdate();
                    System.out.println("Lecteur inséré : " + lecteur);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Fermeture des ressources utilisées (connexion, requêtes préparées, sockets)
            try {
                if (insertLivreStatement != null) {
                    insertLivreStatement.close();
                }
                if (insertLecteurStatement != null) {
                    insertLecteurStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
                if (clientSocket != null) {
                    clientSocket.close();
                }
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

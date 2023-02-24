package org.example.Connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.*;
import java.util.List;
import java.util.Scanner;
import org.example.Livre.Lecteur;
import org.example.Livre.Livre;
import org.example.Livre.Requete;
import org.example.Livre.TypeRequete;

public class Client {
    private Socket socket;
    private ObjectOutputStream outputStream;

    public void start(String host, int port) {
        try {
            // Connexion au serveur
            socket = new Socket(host, port);
            System.out.println("Connecté au serveur " + host + " sur le port " + port);

            // Ouverture du flux de sortie pour envoyer des objets au serveur
            outputStream = new ObjectOutputStream(socket.getOutputStream());

            // Boucle infinie pour permettre à l'utilisateur de saisir des Livres ou des Lecteurs à envoyer au serveur
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("Que voulez-vous faire ? (L pour saisir un livre, C pour saisir un lecteur, V pour voir la base de données, Q pour quitter)");
                String choix = scanner.nextLine().toUpperCase();

                if (choix.equals("L")) {
                    // Saisie d'un Livre
                    System.out.println("Saisissez le titre du livre :");
                    String titre = scanner.nextLine();
                    System.out.println("Saisissez l'auteur du livre :");
                    String auteur = scanner.nextLine();

                    // Création d'un objet Livre à envoyer au serveur
                    Livre livre = new Livre(titre, auteur);

                    // Envoi de l'objet au serveur
                    outputStream.writeObject(livre);
                    System.out.println("Livre envoyé : " + livre.getTitre() + " - " + livre.getAuteur());
                } else if (choix.equals("C")) {
                    // Saisie d'un Lecteur
                    System.out.println("Saisissez le nom du lecteur :");
                    String nom = scanner.nextLine();
                    System.out.println("Saisissez le prénom du lecteur :");
                    String prenom = scanner.nextLine();

                    // Création d'un objet Lecteur à envoyer au serveur
                    Lecteur lecteur = new Lecteur(nom, prenom);

                    // Envoi de l'objet au serveur
                    outputStream.writeObject(lecteur);
                    System.out.println("Lecteur envoyé : " + lecteur.getPrenom() + " " + lecteur.getNom());
                } else if (choix.equals("V")) {
                    // Consultation de la base de données
                    // Création d'un objet Requete à envoyer au serveur

                    String sql1 = "SELECT * FROM Lecteurs";
                    try {
                        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mabd?useSSL=false", "root", "root");
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery(sql1);
                        System.out.println("Table des lecteurs :");
                        while (rs.next()) {
                            int id = rs.getInt("id");
                            String Prenom = rs.getString("Prenom");
                            String Nom = rs.getString("Nom");
                            System.out.println("id : " + id + ", Prenom : " + Prenom + ", Nom : " + Nom);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    String sql = "SELECT * FROM Livres";
                    try {
                        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mabd?useSSL=false", "root", "root");
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery(sql);
                        System.out.println("Table des livres :");
                        while (rs.next()) {
                            int id = rs.getInt("id");
                            String titre = rs.getString("titre");
                            String auteur = rs.getString("auteur");
                            System.out.println("id : " + id + ", titre : " + titre + ", auteur : " + auteur);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }


                    // Envoi de l'objet au serveur
                    outputStream.writeObject("select * from livre");
                    System.out.println("Demande de la table envoyée.");

                    // Lecture de la réponse du serveur
                    ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                    Object objet = inputStream.readObject();
                    if (objet instanceof List<?>) {
                        List<Livre> livres = (List<Livre>) objet;
                        if (livres.isEmpty()) {
                            System.out.println("La base de données est vide.");
                        } else {
                            System.out.println("Livres enregistrés dans la base de données :");
                            for (Livre livre : livres) {
                                System.out.println(livre.getTitre() + " - " + livre.getAuteur());
                            }
                        }
                    } else {
                        System.out.println("Erreur : réponse du serveur invalide");
                    }
                } else if (choix.equals("Q")) {
                    // Déconnexion du serveur
                    System.out.println("Déconnexion du serveur");

                } else {
// Choix invalide
                    System.out.println("Choix invalide, veuillez réessayer");
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("Erreur : hôte inconnu (" + e.getMessage() + ")");
        } catch (IOException e) {
            System.err.println("Erreur : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Erreur : classe non trouvée (" + e.getMessage() + ")");
        } finally {
            try {
                if (outputStream != null) outputStream.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
            }
        }
    }





}


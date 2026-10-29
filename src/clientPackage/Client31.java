package clientPackage;

import serveurPackage.Operation;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client31 {

    // nombre, opérateur (+ - * /), nombre ; espaces facultatifs
    static final Pattern SYNTAXE = Pattern.compile(
            "^\\s*(-?\\d+(?:\\.\\d+)?)\\s*([+\\-*/])\\s*(-?\\d+(?:\\.\\d+)?)\\s*$");

    public static void main(String[] args) {
        try {
            System.out.println("Je suis un client pas encore connecté…");
            Socket socket = new Socket("localhost", 1234);
            System.out.println("je suis un client connecté");


            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Scanner sc = new Scanner(System.in);
            Object bienvenue = ois.readObject();
            System.out.println(bienvenue);
            while (true) {
                System.out.print("Donnez une opération (ex : 55 * 25) ou quit : ");
                String saisie = sc.nextLine().trim();
                if (saisie.equals("quit")) {
                    break;
                }

                // Validation de la syntaxe AVANT l'envoi
                Matcher m = SYNTAXE.matcher(saisie);
                if (!m.matches()) {
                    System.out.println("Syntaxe invalide, rien n'est envoyé.");
                    continue;
                }

                double op1 = Double.parseDouble(m.group(1));
                String op = m.group(2);
                double op2 = Double.parseDouble(m.group(3));

                Operation operation = new Operation(op1, op, op2);
                oos.writeObject(operation);
                oos.flush();
                Object reponseObj = ois.readObject();
                if (reponseObj instanceof Operation) {
                    Operation repOperation = (Operation) reponseObj;
                    System.out.println("Résultat : " + repOperation.getResultat());
                }
            }
            System.out.println("Fin de la communication.");
            sc.close();
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erreur côté client : " + e.getMessage());
        }
    }
}
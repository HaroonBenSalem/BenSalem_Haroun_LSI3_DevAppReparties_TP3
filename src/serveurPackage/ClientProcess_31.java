package serveurPackage;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Un thread = un client. Contient le dialogue qui était dans main() du Server du TP2-1.
 */
public class ClientProcess_31 extends Thread {

    private Socket socket;
    private int numero;
    private AtomicInteger cg;

    public ClientProcess_31(Socket socket, int numero, AtomicInteger cg) {
        this.socket = socket;
        this.numero = numero;
        this.cg = cg;
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            // c) envoyer à chaque client son numéro d'ordre (1re ligne reçue par le client)
            oos.writeObject("Bienvenue, vous êtes le client n°" + numero);
            oos.flush();
            Object recu;
            while ((recu = ois.readObject()) != null) {
                if  (recu instanceof String && ((String) recu).equalsIgnoreCase("quit")) {
                    break;
                }
                if (recu instanceof Operation) {
                    Operation op = (Operation) recu;
                    System.out.println("[client n°" + numero + "] Opération reçue : "
                            + op.getOp1() + " " + op.getOperateur() + " " + op.getOp2());

                    String resultat = calculer(op.getOp1(), op.getOperateur(), op.getOp2());
                    op.setResultat(resultat);

                    int total = cg.incrementAndGet();
                    System.out.println("[Serveur] Compteur global des opérations = " + total);

                    oos.writeObject(op);
                    oos.flush();
                }
            }

            System.out.println("Client n°" + numero + " déconnecté (fin de la communication)");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erreur client n°" + numero + " : " + e.getMessage());
        }
        finally {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
    }

    static String calculer(double a, String operateur, double b) {
        double r;
        switch (operateur) {
            case "+":
                r = a + b;
                break;
            case "-":
                r = a - b;
                break;
            case "*":
                r = a * b;
                break;
            case "/":
                if (b == 0) {
                    return "Erreur : division par zéro impossible";
                }
                r = a / b;
                break;
            default:
                return "Erreur : opérateur inconnu";
        }
        return a + " " + operateur + " " + b + " = " + r;
    }

    static String formater(double v) {
        if (v == Math.rint(v) && !Double.isInfinite(v)) {
            return String.valueOf((long) v);
        }
        return String.valueOf(v);
    }
}
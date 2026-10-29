package serveurPackage;

import java.io.Serializable;

public class Operation implements Serializable {
    private static final long serialVersionUID = 1L;

    private double op1;
    private double op2;
    private String operateur;
    private String resultat;

    public Operation(double op1, String operateur, double op2) {
        this.op1 = op1;
        this.operateur = operateur;
        this.op2 = op2;
    }

    // Getters et Setters
    public double getOp1() { return op1; }
    public double getOp2() { return op2; }
    public String getOperateur() { return operateur; }

    public String getResultat() { return resultat; }
    public void setResultat(String resultat) { this.resultat = resultat; }
}
package Model;

import java.io.Serializable;

/**
 * Money received by the machine.
 * balance = income of the current run (reset when the app closes),
 * totally = income of all time.
 */
public class Finance implements Serializable {

    // keeps database.dat files created by older versions readable
    private static final long serialVersionUID = -5300355170956067267L;

    private double balance;
    private double totally;
    
    public Finance() {
        balance = 0;
        totally = 0;
    }
    
    public Finance(double balance, double totally) {
        this.balance = balance;
        this.totally = totally;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getTotally() {
        return totally;
    }

    public void setTotally(double totally) {
        this.totally = totally;
    }
    
    
}

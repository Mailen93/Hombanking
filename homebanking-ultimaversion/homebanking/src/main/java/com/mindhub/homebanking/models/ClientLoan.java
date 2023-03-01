package com.mindhub.homebanking.models;

import com.fasterxml.jackson.databind.node.FloatNode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class ClientLoan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private Integer payments;

    private double amount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "loan_id")
    private Loan loan;

    public ClientLoan(){}

    public ClientLoan(double amount, Integer payments) {
        this.payments = payments;
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public int getPayments() {
        return payments;
    }

    public void setPayments(Integer payments) {
        this.payments = payments;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Client getClient() { return client;}

    public void setClient(Client client) { this.client = client;}

    public Loan getLoan() { return loan;}

    public void setLoan(Loan loan) {this.loan = loan;}
}


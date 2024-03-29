package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(name = "native",strategy = "native")
    private Long id;
    private String number;
    private LocalDateTime creationDate;
    private Double balance;
    private AccountType accountType;
    private Boolean deleteAccount;
    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;

    @OneToMany(mappedBy="account", fetch= FetchType.EAGER)
    private Set<Transaction> transactions = new HashSet<>();

    public Account(){}
    public Account(String numb, LocalDateTime creationDate, Double balance, Boolean deleteAccount, AccountType  accountType){
        this.number = numb;
        this.creationDate = creationDate;
        this.deleteAccount = deleteAccount;
        this.accountType = accountType;
        this.balance = balance;}

    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public LocalDateTime getCreationDate(){ return creationDate;}
    public void setCreationDate(LocalDateTime creationDate){ this.creationDate = creationDate;}
    public Double getBalance () {return balance;}
    public void setBalance (Double balance){this.balance = balance;}
    public Client getClient() {return client;}
    public void setClient(Client client) {this.client = client;}
    public Long getId() {return id;}
    public Boolean getDeleteAccount() {return deleteAccount;}
    public void setDeleteAccount(Boolean deleteAccount) {this.deleteAccount = deleteAccount;}
    public AccountType getAccountType() {return accountType;}
    public void setAccountType(AccountType accountType) {this.accountType = accountType;}

    public void addTransaction(Transaction transaction){
        transaction.setAccount(this);
        transactions.add(transaction);}

    public Set<Transaction> getTransactions() {
        return transactions;
    }
}


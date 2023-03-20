package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class AccountsDTO {
    private long id;
    private String number;
    private LocalDateTime creationDate;
    private double balance;
    private Boolean deleteAccount;
    private List<TransactionDTO> transaction;
    private AccountType accountType;

    public AccountsDTO(){}
    public AccountsDTO(Account account){
        this.id = account.getId();
        this.number = account.getNumber();
        this.balance = account.getBalance();
        this.creationDate = account.getCreationDate();
        this.deleteAccount = account.getDeleteAccount();
        this.accountType = account.getAccountType();
        this.transaction = account.getTransactions().stream().map(transaction -> new TransactionDTO(transaction)).collect(toList());}

    public long getId() {
        return id;
    }
    public String getNumber() {
        return number;
    }
    public LocalDateTime getCreationDate() {
        return creationDate;
    }
    public double getBalance() {
        return balance;
    }
    public List<TransactionDTO> getTransaction() {
        return transaction;
    }
    public Boolean getDeleteAccount() {return deleteAccount;}
    public AccountType getAccountType() {return accountType;}
}


package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Loan;

import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

public class ClientDTO{
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private List<AccountsDTO> accounts;
    private List<ClientLoanDTO> loans;
    private List<CardDTO> cards;

    public ClientDTO() { }
    public ClientDTO(Client client) {
        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
        this.accounts = client.getAccounts().stream().map(account -> new AccountsDTO(account)).collect(toList());
        this.loans = client.getClientLoans().stream().map(loans -> new ClientLoanDTO(loans)).collect(toList());
        this.cards = client.getCards().stream().map(cards -> new CardDTO(cards)).collect(toList());}

    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {return lastName;}
    public String getEmail() {return email;}
    public List<AccountsDTO> getAccounts() { return accounts; }
    public List<ClientLoanDTO> getLoans() { return loans; }
    public List<CardDTO>getCards(){return cards;}
    }


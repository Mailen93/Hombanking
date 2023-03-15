package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mindhub.homebanking.dtos.ClientDTO;          //Librerias y paquetes
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(name = "native",strategy = "native")
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    @OneToMany(mappedBy="client", fetch= FetchType.EAGER)
    private Set<Account> accounts = new HashSet<>();

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set <ClientLoan> clientLoans = new HashSet<>();

    @OneToMany(mappedBy = "cardHolder", fetch = FetchType.EAGER)
    private Set<Card> cards = new HashSet<>();

    public Client() { } //Spring jpa necesita el constructor vacio por defecto, para poder crear los clientes, despues lo redefinimos
    public Client(String first, String last, String email, String password) {  //Los parametros vienen del homebanking cuando hago un new client.
        this.firstName = first;
        this.lastName = last;
        this.email = email;
        this.password= password;}

    public void addAccount(Account account){
        account.setClient(this);
        accounts.add(account);}
    public void addClientLoan(ClientLoan clientLoan){
        clientLoan.setClient(this);
        clientLoans.add(clientLoan);}
    public void addCard(Card card){
        card.setCardHolder(this);
        cards.add(card);}

    @JsonIgnore
    public List<Loan> getLoan() {  //get loan porque yo a trves del cliente quiero obtener los loan, pero como el cliente no esta relacionado directamente con los loans lo que hago es en el metodo me traigo un client loan y a ese client loan le pongo un get loan para que me traiga el prestamo.
        return clientLoans.stream().map(clientLoan -> clientLoan.getLoan()).collect(toList());}

    public Long getId() {return id;}
    public String getFirstName() {return firstName;}
    public void setFirstName(String firstName) {this.firstName = firstName;}
    public String getLastName() {return lastName;}
    public void setLastName(String lastName) {this.lastName = lastName;}
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
    public Set<ClientLoan> getClientLoans() {return clientLoans;}
    public void setClientLoans(Set<ClientLoan> clientLoans) {this.clientLoans = clientLoans;}
    public Set<Account> getAccounts() {return accounts;}
    public Set<Card> getCards(){return cards;}
    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}
}


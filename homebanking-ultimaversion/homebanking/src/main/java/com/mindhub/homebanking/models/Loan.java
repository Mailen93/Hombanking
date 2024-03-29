package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Entity
public class Loan {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator( name = "native", strategy = "native")
    private Long id;
    private String name;
    private Integer maxAmount;
    private Double iva;

    @ElementCollection //MAPEA
    @Column (name = "payments")
    private List<Integer> payments =new ArrayList<>();

    @OneToMany(mappedBy = "loan", fetch = FetchType.EAGER)
    private Set<ClientLoan> clientLoans = new HashSet<>();

    public Loan(){}
    public Loan(String name, Integer maxAmount, List<Integer> payments, Double iva){
        this.name = name;
        this.maxAmount = maxAmount;
        this.iva = iva;
        this.payments = payments;}
        public void addClientLoan(ClientLoan clientLoan1){
            clientLoan1.setLoan(this);
            clientLoans.add(clientLoan1);}

    public Long getId(){
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getMaxAmount() {
        return maxAmount;
    }
    public void setMaxAmount(Integer maxAmount) {
        this.maxAmount = maxAmount;
    }
    public List<Integer> getPayments() {
        return payments;
    }
    public List<Client> getClients() {return clientLoans.stream().map(clientLoan -> clientLoan.getClient()).collect(toList());}
    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }
    public Double getIva() {return iva;}
    public void setIva(Double iva) {this.iva = iva;}
}

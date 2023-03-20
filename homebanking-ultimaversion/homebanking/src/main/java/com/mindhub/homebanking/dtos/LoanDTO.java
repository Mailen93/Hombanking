package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;

import java.util.List;

public class LoanDTO {
    private long id;
    private String name;
    private int maxAmount;
    private List<Integer> payments;
    private Double iva;

    public LoanDTO(){}
    public LoanDTO(Loan loan){
        this.id = loan.getId();
        this.name = loan.getName();
        this.maxAmount = loan.getMaxAmount();
        this.iva = loan.getIva();
        this.payments = loan.getPayments();}
    public long getId() {return id;}
    public String getName() {return name;}
    public int getMaxAmount() {return maxAmount;}
    public Double getIva() {return iva;}
    public List<Integer> getPayments() {return payments;}
}

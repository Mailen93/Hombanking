
package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.ClientLoan;

public class LoanApplicationDTO {
    private Double amount;
    private Integer payments;
    private Long loans_id;
    private String account_destinated;

    public LoanApplicationDTO(Double amount, Integer payments, Long loans_id, String account_destinated) {
        this.amount = amount;
        this.payments = payments;
        this.loans_id = loans_id;
        this.account_destinated = account_destinated;
    }
    public Double getAmount() {return amount;}
    public Integer getPayments() {return payments;}
    public Long getLoans_id() {return loans_id;}
    public String getAccount_destinated() {return account_destinated;}
}




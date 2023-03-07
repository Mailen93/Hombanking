package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.ClientLoan;

public class LoanApplicationDTO {
    private double amount;
    private int payments;
    private Long loans_id;
    private String account_destinated;

    public LoanApplicationDTO(){}
    public LoanApplicationDTO(double amount, int payments, Long loans_id, String account_destinated ){
}

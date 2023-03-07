package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.ClientLoan;

public class LoanApplicationDTO {
    private Double amount;
    private Integer payments;
    private Long loans_id;
    private String account_destinated;

    public LoanApplicationDTO(){}
    public LoanApplicationDTO(Double amount, Integer payments, Long loans_id, String account_destinated ){
}

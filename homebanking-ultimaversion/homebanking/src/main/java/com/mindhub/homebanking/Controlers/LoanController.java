package com.mindhub.homebanking.Controlers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepositories;
import com.mindhub.homebanking.repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class LoanController {


    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepositories clientRepositories;
    @Autowired
    private LoanRepository loanRepository;


    @RequestMapping("/Loans")
    public List<LoanDTO> getAll() {
        return loanRepository.findAll().stream().map(LoanDTO::new).collect(toList());}
    @Transactional
    @RequestMapping(path = ("/loans"), method = RequestMethod.POST)
    public ResponseEntity<Object> newLoan(Authentication authentication, @RequestBody LoanApplicationDTO loanApplicationDTO){
        Client client = clientRepositories.findByEmail(authentication.getName());
        if (amount.isNaN()){
            return new ResponseEntity<>("Empty amount", HttpStatus.BAD_REQUEST);}
    }
}

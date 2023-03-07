package com.mindhub.homebanking.Controlers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
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
    @Autowired
    private ClientLoanRepository clientLoanRepository;


    @RequestMapping("/Loans")
    public List<LoanDTO> getLoans() {
        return loanRepository.findAll().stream().map(LoanDTO::new).collect(toList());}
    @Transactional
    @RequestMapping(path = ("/loans"), method = RequestMethod.POST)
    public ResponseEntity<Object> newLoan(Authentication authentication, @RequestBody LoanApplicationDTO loanApplicationDTO){
        Client client = clientRepositories.findByEmail(authentication.getName());
        if (loanApplicationDTO.getAmount().isNaN()){
            return new ResponseEntity<>("Empty amount", HttpStatus.BAD_REQUEST);}

    }
}



 /*   Debe recibir un objeto de solicitud de crédito con los datos del préstamo
        Verificar que los datos sean correctos, es decir no estén vacíos, que el monto no sea 0 o que las cuotas no sean 0.
        Verificar que el préstamo exista
        Verificar que el monto solicitado no exceda el monto máximo del préstamo
        Verifica que la cantidad de cuotas se encuentre entre las disponibles del préstamo
        Verificar que la cuenta de destino exista
        Verificar que la cuenta de destino pertenezca al cliente autenticado
        Se debe crear una solicitud de préstamo con el monto solicitado sumando el 20% del mismo
        Se debe crear una transacción “CREDIT” asociada a la cuenta de destino (el monto debe quedar positivo) con la descripción concatenando el nombre del préstamo y la frase “loan approved”
        Se debe actualizar la cuenta de destino sumando el monto solicitado.*/
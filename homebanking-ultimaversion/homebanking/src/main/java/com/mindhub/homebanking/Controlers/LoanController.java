package com.mindhub.homebanking.Controlers;

import ch.qos.logback.core.joran.conditional.IfAction;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
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
    @Autowired
    private TransactionRepository transactionRepository;


    @RequestMapping("/loans")
    public List<LoanDTO> getLoans() {
        return loanRepository.findAll().stream().map(LoanDTO::new).collect(toList());}
    @Transactional
    @RequestMapping(path = ("/loans"), method = RequestMethod.POST)
    public ResponseEntity<Object> newLoan(Authentication authentication, @RequestBody LoanApplicationDTO loanApplicationDTO){
        Client client = clientRepositories.findByEmail(authentication.getName());
        if (loanApplicationDTO.getAmount() == null){
            return new ResponseEntity<>("Empty amount", HttpStatus.BAD_REQUEST);}
        if (loanApplicationDTO.getLoans_id() == null){
            return new ResponseEntity<>("Empty ID", HttpStatus.BAD_REQUEST);}
        if (loanApplicationDTO.getPayments() == null){
            return new ResponseEntity<>("Empty payments", HttpStatus.BAD_REQUEST);}
        if (loanApplicationDTO.getAccount_destinated() == null){
            return new ResponseEntity<>("Empty account", HttpStatus.BAD_REQUEST);}
        if (loanApplicationDTO.getAmount() <= 0){
            return new ResponseEntity<>("NO SE PUEDE", HttpStatus.BAD_REQUEST);}
        if (loanApplicationDTO.getPayments() <= 0){
            return new ResponseEntity<>("TAMPOCO", HttpStatus.BAD_REQUEST);}
        if (!loanRepository.existsById(loanApplicationDTO.getLoans_id())){
            return new ResponseEntity<>("The loan doesn't exist", HttpStatus.BAD_REQUEST);}
        if (loanApplicationDTO.getAmount() > loanRepository.findById(loanApplicationDTO.getLoans_id()).orElse(null).getMaxAmount()){
            return new ResponseEntity<>("el monto excede", HttpStatus.BAD_REQUEST);}
        if (!loanRepository.findById(loanApplicationDTO.getLoans_id()).orElse(null).getPayments().contains(loanApplicationDTO.getPayments())){
            return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);}
        if (!accountRepository.existsByNumber(loanApplicationDTO.getAccount_destinated())){
            return new ResponseEntity<>("la cuenta de destino no existe", HttpStatus.BAD_REQUEST);}
        if (!client.getAccounts().contains(accountRepository.findByNumber(loanApplicationDTO.getAccount_destinated()))){
            return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);}
        if (client.getLoan().contains(loanRepository.findById(loanApplicationDTO.getLoans_id()).orElse(null))){
            return new ResponseEntity<>("no podes tener el mismo prestamo 2 veces", HttpStatus.BAD_REQUEST);}

        ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.getAmount()*1.2, loanApplicationDTO.getPayments());
        client.addClientLoan(clientLoan);
        loanRepository.findById(loanApplicationDTO.getLoans_id()).orElse(null).addClientLoan(clientLoan);
        clientLoanRepository.save(clientLoan);

        Transaction transaction = new Transaction(TransactionType.CREDIT,loanApplicationDTO.getAmount(),loanRepository.findById(loanApplicationDTO.getLoans_id()).orElse(null) + "loan approved", LocalDateTime.now());
        accountRepository.findByNumber(loanApplicationDTO.getAccount_destinated()).addTransaction(transaction);
        accountRepository.findByNumber(loanApplicationDTO.getAccount_destinated()).setBalance(accountRepository.findByNumber(loanApplicationDTO.getAccount_destinated()).getBalance()+loanApplicationDTO.getAmount());
        accountRepository.save(accountRepository.findByNumber(loanApplicationDTO.getAccount_destinated()));
        transactionRepository.save(transaction);

        return new ResponseEntity<>("Loan succesfull",HttpStatus.CREATED);}
}

 /*   Debe recibir un objeto de solicitud de cr??dito con los datos del pr??stamo
        Verificar que los datos sean correctos, es decir no est??n vac??os, que el monto no sea 0 o que las cuotas no sean 0.
        Verificar que el pr??stamo exista
        Verificar que el monto solicitado no exceda el monto m??ximo del pr??stamo
        Verifica que la cantidad de cuotas se encuentre entre las disponibles del pr??stamo
        Verificar que la cuenta de destino exista
        Verificar que la cuenta de destino pertenezca al cliente autenticado

        Se debe crear una solicitud de pr??stamo con el monto solicitado sumando el 20% del mismo
        Se debe crear una transacci??n ???CREDIT??? asociada a la cuenta de destino (el monto debe quedar positivo) con la descripci??n concatenando el nombre del pr??stamo y la frase ???loan approved???
        Se debe actualizar la cuenta de destino sumando el monto solicitado.*/
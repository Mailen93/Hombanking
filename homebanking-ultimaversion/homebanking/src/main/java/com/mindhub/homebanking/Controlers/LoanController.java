package com.mindhub.homebanking.Controlers;

import com.mindhub.homebanking.Services.*;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class LoanController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private LoanService loanService;
    @Autowired
    private ClientLoanService clientLoanService;
    @Autowired
    private TransactionService transactionService;

    @GetMapping("/loans")
    public List<LoanDTO> getLoans() {
        return loanService.findAll().stream().map(LoanDTO::new).collect(toList());}

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> newLoan(Authentication authentication, @RequestBody LoanApplicationDTO loanApplicationDTO){
        Client client = clientService.findByEmail(authentication.getName());
        Account accVar = accountService.findByNumber(loanApplicationDTO.getAccount_destinated());
        if (loanApplicationDTO.getAmount() == null && loanApplicationDTO.getLoans_id() == null && loanApplicationDTO.getPayments() == null && loanApplicationDTO.getAccount_destinated() == null){
            return new ResponseEntity<>("Complete the data", HttpStatus.BAD_REQUEST);}
        if (loanApplicationDTO.getAmount() == null){
            return new ResponseEntity<>("Empty amount", HttpStatus.BAD_REQUEST);}
        if (loanApplicationDTO.getLoans_id() == null){
            return new ResponseEntity<>("Empty ID", HttpStatus.BAD_REQUEST);}
        if (loanApplicationDTO.getPayments() == null){
            return new ResponseEntity<>("Empty payments", HttpStatus.BAD_REQUEST);}
        if (loanApplicationDTO.getAccount_destinated() == null){
            return new ResponseEntity<>("Empty account", HttpStatus.BAD_REQUEST);}
        Loan loanVar = loanService.findById(loanApplicationDTO.getLoans_id()).get();
        if (loanApplicationDTO.getAmount() <= 0){
            return new ResponseEntity<>("T", HttpStatus.BAD_REQUEST);}
        if (loanApplicationDTO.getPayments() <= 0){
            return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);}
        if (!loanService.existsById(loanApplicationDTO.getLoans_id())){
            return new ResponseEntity<>("The loan doesn't exist", HttpStatus.BAD_REQUEST);}
        if (loanApplicationDTO.getAmount() > loanService.findById(loanApplicationDTO.getLoans_id()).orElse(null).getMaxAmount()){
            return new ResponseEntity<>("Max amount exceded", HttpStatus.BAD_REQUEST);}
        if (!loanService.findById(loanApplicationDTO.getLoans_id()).orElse(null).getPayments().contains(loanApplicationDTO.getPayments())){
            return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);}
        if (!accountService.existsByNumber(loanApplicationDTO.getAccount_destinated())){
            return new ResponseEntity<>("Empty account", HttpStatus.BAD_REQUEST);}
        if (!client.getAccounts().contains(accountService.findByNumber(loanApplicationDTO.getAccount_destinated()))){
            return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);}
        if (client.getLoan().contains(loanService.findById(loanApplicationDTO.getLoans_id()).orElse(null))){
            return new ResponseEntity<>("You can't have the same loan twice", HttpStatus.BAD_REQUEST);}

        ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.getAmount()*loanVar.getIva(), loanApplicationDTO.getPayments(), client, loanVar);
        client.addClientLoan(clientLoan);
        loanVar.addClientLoan(clientLoan);

        Transaction transaction = new Transaction(TransactionType.CREDIT,loanApplicationDTO.getAmount(), "loan approved", LocalDateTime.now(), accountService.findByNumber(loanApplicationDTO.getAccount_destinated()).getBalance());
        accVar.addTransaction(transaction);
        accVar.setBalance(accVar.getBalance()+loanApplicationDTO.getAmount());
        accountService.save(accVar);
        transactionService.save(transaction);
        clientLoanService.save(clientLoan);
        clientService.save(client);
        loanService.save(loanVar);

        return new ResponseEntity<>("Loan succesfull",HttpStatus.CREATED);}


    @PostMapping("/loans/admin")
    public ResponseEntity<Object> loanAdmin(@RequestParam String name,
                                            @RequestParam Integer maxAmount,
                                            @RequestParam List<Integer> payments,
                                            @RequestParam Double iva){
        if (name.isEmpty()){
            return new ResponseEntity<>("Name is empty", HttpStatus.BAD_REQUEST);}
        if (maxAmount.toString().isEmpty()){
            return new ResponseEntity<>("Empty max amount", HttpStatus.BAD_REQUEST);}
        if (payments.isEmpty()){
            return new ResponseEntity<>("Empty payment", HttpStatus.BAD_REQUEST);}
        if (iva.isNaN()){
            return new ResponseEntity<>("Empty iva", HttpStatus.BAD_REQUEST);}
        if (loanService.findAll().stream().anyMatch(loan -> loan.getName().equals(name))) {
            return new ResponseEntity<>("ya existe un prestamo con ese nombre", HttpStatus.BAD_REQUEST);
        }

        Loan loan = new Loan(name,maxAmount,payments,iva);
        loanService.save(loan);
        return new ResponseEntity<>("Loan succesfully created", HttpStatus.CREATED);}

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
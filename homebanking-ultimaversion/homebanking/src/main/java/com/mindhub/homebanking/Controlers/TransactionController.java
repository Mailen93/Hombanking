package com.mindhub.homebanking.Controlers;

import com.mindhub.homebanking.Services.AccountService;
import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.Services.TransactionService;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private TransactionService transactionService;

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> getTransactions( @RequestParam Double amount,
                                                   @RequestParam String description,
                                                   @RequestParam String nDeCuentaOr,
                                                   @RequestParam String nDeCuentaDest,
                                                   Authentication authentication){
       Client client = clientService.findByEmail(authentication.getName());
        if (amount <= 0){
            return new ResponseEntity<>("TRANSFERNEGATIVA", HttpStatus.BAD_REQUEST);}
       if (amount.isNaN()){
            return new ResponseEntity<>("Empty amount", HttpStatus.BAD_REQUEST);}
       if (description.isEmpty()) {
           return new ResponseEntity<>("Empty description", HttpStatus.BAD_REQUEST);}
       if (nDeCuentaOr.isEmpty()) {
           return new ResponseEntity<>("Empty origin account", HttpStatus.BAD_REQUEST);}
       if (nDeCuentaDest.isEmpty()){
           return new ResponseEntity<>("Empty destination account",HttpStatus.BAD_REQUEST);}
       if (nDeCuentaDest.equals(nDeCuentaOr)){
           return new ResponseEntity<>("You can't transfer to the same account",HttpStatus.CONFLICT);}
       if (client.getAccounts().stream().noneMatch(account -> account.getNumber().equals(nDeCuentaOr))){
           return new ResponseEntity<>("This account doesn't exist", HttpStatus.BAD_REQUEST);}
       if (!accountService.existsByNumber(nDeCuentaDest)){
           return new ResponseEntity<>("The account is correct", HttpStatus.BAD_REQUEST);}
       if (accountService.findByNumber(nDeCuentaOr).getBalance() < amount){
           return new ResponseEntity<>("Not enough money in the account", HttpStatus.BAD_REQUEST);}

        Account accountOrigen = accountService.findByNumber(nDeCuentaOr);
        Account accountDestino = accountService.findByNumber(nDeCuentaDest);

        Transaction newTransactionDest = new Transaction(TransactionType.CREDIT, amount, description, LocalDateTime.now(), accountOrigen.getBalance() - amount);
        Transaction newTransactionOr = new Transaction(TransactionType.DEBIT, amount, description, LocalDateTime.now(), accountDestino.getBalance() + amount);

        accountOrigen.addTransaction(newTransactionOr);
        accountDestino.addTransaction(newTransactionDest);
        transactionService.save(newTransactionOr);
        transactionService.save(newTransactionDest);

        accountOrigen.setBalance(accountOrigen.getBalance()-amount);
        accountDestino.setBalance(accountDestino.getBalance()+amount);
        accountService.save(accountOrigen);
        accountService.save(accountDestino);

        return new ResponseEntity<>("Transfer succesfull",HttpStatus.CREATED);}

}
//Debe recibir el monto, la descripción, número de cuenta de origen y número de cuenta de destino como parámetros de solicitud
//Verificar que los parámetros no estén vacíos
//Verificar que los números de cuenta no sean iguales
//Verificar que exista la cuenta de origen
//Verificar que la cuenta de origen pertenezca al cliente autenticado
//Verificar que exista la cuenta de destino
//Verificar que la cuenta de origen tenga el monto disponible.

//Se deben crear dos transacciones, una con el tipo de transacción “DEBIT” asociada a la cuenta de origen y la otra con el tipo de transacción “CREDIT” asociada a la cuenta de destino.
//A la cuenta de origen se le restará el monto indicado en la petición y a la cuenta de destino se le sumará el mismo monto.
//  if (client.getAccounts().stream().filter(account -> account.getNumber().equals(nDeCuentaOr)))

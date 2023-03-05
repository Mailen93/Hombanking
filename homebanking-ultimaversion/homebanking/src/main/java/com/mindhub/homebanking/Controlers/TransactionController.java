package com.mindhub.homebanking.Controlers;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepositories;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepositories clientRepositories;
    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    @RequestMapping(path = ("/transactions"), method = RequestMethod.POST)
    public ResponseEntity<Object> getTransactions( @RequestParam Double amount,
                                                   @RequestParam String description,
                                                   @RequestParam String nDeCuentaOr,
                                                   @RequestParam String nDeCuentaDest,
                                                   Authentication authentication){
       Client client = clientRepositories.findByEmail(authentication.getName());
       if (amount.isNaN()){
            return new ResponseEntity<>("NO SE PUEDE MAN", HttpStatus.BAD_REQUEST);}
       if (description.isEmpty()) {
           return new ResponseEntity<>("The description is empty", HttpStatus.BAD_REQUEST);}
       if (nDeCuentaOr.isEmpty()) {
           return new ResponseEntity<>("Number Empty", HttpStatus.BAD_REQUEST);}
       if (nDeCuentaDest.isEmpty()){
           return new ResponseEntity<>("Number Empty",HttpStatus.BAD_REQUEST);}
       if (nDeCuentaDest.equals(nDeCuentaOr)){
           return new ResponseEntity<>("YOU ARE STUPID",HttpStatus.CONFLICT);}
       if (client.getAccounts().stream().noneMatch(account -> account.getNumber().equals(nDeCuentaOr))){
           return new ResponseEntity<>("NO ES TUYA JOAQUI", HttpStatus.BAD_REQUEST);}
       if (accountRepository.existsByNumber(nDeCuentaDest)){
           return new ResponseEntity<>("The account is correct", HttpStatus.BAD_REQUEST);}
       if (accountRepository.findByNumber(nDeCuentaOr).getBalance() < amount){
           return new ResponseEntity<>("NO TENES SUFICIENTE GUITA", HttpStatus.BAD_REQUEST);}

        Transaction newTransactionDest = new Transaction(TransactionType.CREDIT, amount, description, LocalDateTime.now());
        Transaction newTransactionOr = new Transaction(TransactionType.DEBIT, amount, description, LocalDateTime.now());

        Account accountOrigen = accountRepository.findByNumber(nDeCuentaOr);
        Account accountDestino = accountRepository.findByNumber(nDeCuentaDest);
        accountOrigen.addTransaction(newTransactionOr);
        accountDestino.addTransaction(newTransactionDest);
        transactionRepository.save(newTransactionOr);
        transactionRepository.save(newTransactionDest);

        accountOrigen.setBalance(accountOrigen.getBalance()-amount);
        accountDestino.setBalance(accountDestino.getBalance()+amount);
        accountRepository.save(accountOrigen);
        accountRepository.save(accountDestino);

        return new ResponseEntity<>("Transacción Exitosa",HttpStatus.CREATED);}

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

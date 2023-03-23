package com.mindhub.homebanking.Controlers;

import com.mindhub.homebanking.Services.AccountService;
import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.Utils.Utilities;
import com.mindhub.homebanking.dtos.AccountsDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

    @RestController   //CREAR UN NUEVO RECURSO PARA CREAR CUENTAS
    @RequestMapping("/api")
    public class AccountsController {

        @Autowired
        private AccountService accountService;
        @Autowired
        private ClientService clientService;

        @RequestMapping("/accounts")
        public List<AccountsDTO> getAccounts() {
            return accountService.findAll().stream().map(account -> new AccountsDTO(account)).collect(toList());}

        @RequestMapping("/accounts/{id}")
        public AccountsDTO getAccounts(@PathVariable Long id) {
            return new AccountsDTO(accountService.findById(id).orElse(null));}

        @RequestMapping("/clients/current/accounts")
        public List<AccountsDTO> getCurrentAccounts(Authentication authentication) {
            Client client = clientService.findByEmail(authentication.getName());
            List<Account> AccountChecked = client.getAccounts().stream().filter(account -> account.getDeleteAccount() == true).collect(toList());
            return AccountChecked.stream().map(account -> new AccountsDTO(account)).collect(toList());}

        @PostMapping("/clients/current/accounts")
        public ResponseEntity<Object> newAccount(Authentication authentication, @RequestParam String accountType) {
            Client client = clientService.findByEmail(authentication.getName());
            AccountType accountTypeReal = AccountType.valueOf(accountType);
            if (client.getAccounts().stream().filter(account -> account.getDeleteAccount() == true).count() >= 3) {
                return new ResponseEntity<>("You can't have more accounts", HttpStatus.FORBIDDEN);
            }
            Account newAccount = new Account(Utilities.Number(accountService), LocalDateTime.now(), 0.0, true, accountTypeReal);
            client.addAccount(newAccount);
            accountService.save(newAccount);
                return new ResponseEntity<>(HttpStatus.CREATED);}

        @PatchMapping("/clients/current/accounts")
        public ResponseEntity<Object> deleteAccount (Authentication authentication, @RequestParam String name) {
            Client clientAuth = clientService.findByEmail(authentication.getName());
          Account deleteAccount = accountService.findByNumber(name);
            if (name.isEmpty()) {
                return new ResponseEntity<>("Missing Number", HttpStatus.FORBIDDEN);
            }
            if (clientAuth.getAccounts().stream().filter(account -> account.getNumber().equals(name)) == null) {
                return new ResponseEntity<>("Wrong Number", HttpStatus.BAD_REQUEST);
            }
            if (deleteAccount.getBalance() > 0){
                return new ResponseEntity<>("You can't delete an account with cash", HttpStatus.FORBIDDEN);
            }
            if (!clientAuth.getClientLoans().isEmpty() && clientAuth.getAccounts().size() == 1){
                return new ResponseEntity<>("You can't delete an account with a loan", HttpStatus.FORBIDDEN);
            }
            deleteAccount.setDeleteAccount(false);
            accountService.save(deleteAccount);
            clientService.save(clientAuth);
            return new ResponseEntity<>("Your account has been deleted", HttpStatus.ACCEPTED);}
        }

//        IR A CARD CONTROLLER
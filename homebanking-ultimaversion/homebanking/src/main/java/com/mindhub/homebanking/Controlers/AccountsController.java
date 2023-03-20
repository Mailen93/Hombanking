package com.mindhub.homebanking.Controlers;

import com.mindhub.homebanking.Utils.Utilities;
import com.mindhub.homebanking.dtos.AccountsDTO;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.mindhub.homebanking.Utils.Utilities.GenerateNumber;
import static java.util.stream.Collectors.toList;

    @RestController   //CREAR UN NUEVO RECURSO PARA CREAR CUENTAS
    @RequestMapping("/api")
    public class AccountsController {

        @Autowired
        private AccountRepository accountRepo;
        @Autowired
        private ClientRepositories clientRepositories;

        @RequestMapping("/accounts")
        public List<AccountsDTO> getAccounts() {
            return accountRepo.findAll().stream().map(account -> new AccountsDTO(account)).collect(toList());}

        @RequestMapping("/accounts/{id}")
        public AccountsDTO getAccounts(@PathVariable Long id) {
            return new AccountsDTO(accountRepo.findById(id).orElse(null));}

        @RequestMapping("/clients/current/accounts")
        public List<AccountsDTO> getCurrentAccounts(Authentication authentication) {
            Client client = clientRepositories.findByEmail(authentication.getName());
            List<Account> AccountChecked = client.getAccounts().stream().filter(account -> account.getDeleteAccount() == true).collect(toList());
            return AccountChecked.stream().map(account -> new AccountsDTO(account)).collect(toList());}

        @PostMapping("/clients/current/accounts")
        public ResponseEntity<Object> newAccount(Authentication authentication, @RequestParam String accountType) {
            Client client = clientRepositories.findByEmail(authentication.getName());
            AccountType accountTypeReal = AccountType.valueOf(accountType);
            if (client.getAccounts().size() >= 3) {
                return new ResponseEntity<>("You can't have more accounts", HttpStatus.FORBIDDEN);
            }
            Account newAccount = new Account(Utilities.Number(accountRepo), LocalDateTime.now(), 0.0, true, accountTypeReal);
            client.addAccount(newAccount);
            accountRepo.save(newAccount);
                return new ResponseEntity<>(HttpStatus.CREATED);}

        @PatchMapping("/clients/current/accounts")
        public ResponseEntity<Object> deleteAccount (Authentication authentication, @RequestParam String name) {
            Client clientAuth = clientRepositories.findByEmail(authentication.getName());
          Account deleteAccount = accountRepo.findByNumber(name);
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
            accountRepo.save(deleteAccount);
            clientRepositories.save(clientAuth);
            return new ResponseEntity<>("Your account has been deleted", HttpStatus.ACCEPTED);}
        }

//        IR A CARD CONTROLLER
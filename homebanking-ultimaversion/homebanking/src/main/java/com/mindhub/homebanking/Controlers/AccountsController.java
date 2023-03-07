package com.mindhub.homebanking.Controlers;

import com.mindhub.homebanking.Utils.Utilities;
import com.mindhub.homebanking.dtos.AccountsDTO;
import com.mindhub.homebanking.models.Account;
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

    @RestController
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
            return clientRepositories.findByEmail(authentication.getName()).getAccounts().stream().map(account -> new AccountsDTO(account)).collect(toList());}

        @RequestMapping(path = ("/clients/current/accounts"), method = RequestMethod.POST)
        public ResponseEntity<Object> newAccount(Authentication authentication) {
            Client client = clientRepositories.findByEmail(authentication.getName());
            if (client.getAccounts().size() >= 3) {
                return new ResponseEntity<>("You can't have more accounts", HttpStatus.FORBIDDEN);
            }
            Account newAccount = new Account(Utilities.Number(accountRepo), LocalDateTime.now(), 0.0);
            client.addAccount(newAccount);
            accountRepo.save(newAccount);
                return new ResponseEntity<>(HttpStatus.CREATED);}
        }

//        IR A CARD CONTROLLER
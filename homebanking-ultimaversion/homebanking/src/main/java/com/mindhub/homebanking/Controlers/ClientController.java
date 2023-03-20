package com.mindhub.homebanking.Controlers;

import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.Utils.Utilities;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@RestController                     //CREAR NUEVO RECURSO PARA REGISTRAR UN CLIENTE
@RequestMapping("/api")
public class ClientController{
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping("/clients")
    public List<ClientDTO> getClientDto(){
        return clientService.findAll().stream().map(client -> new ClientDTO(client)).collect(toList());}

    @RequestMapping("/clients/{id}")
    public Optional<Object> getClient(@PathVariable Long id){
        return clientService.findById(id).map(ClientDTO::new);}

//    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    @PostMapping("/clients")
    public ResponseEntity<Object> register(
            @RequestParam String first, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {

        if (first.isEmpty()){
            return new ResponseEntity<>("Missing First Name", HttpStatus.BAD_REQUEST);}
        else if (lastName.isEmpty()){
            return new ResponseEntity<>("Missing Last Name", HttpStatus.BAD_REQUEST);}
        else if (email.isEmpty()){
            return new ResponseEntity<>("Missing Email", HttpStatus.BAD_REQUEST);}
        else if (password.isEmpty()){
            return new ResponseEntity<>("Missing Password", HttpStatus.BAD_REQUEST);}
        if (clientService.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Email already in use", HttpStatus.BAD_REQUEST);}

       Client newClient = new Client(first, lastName, email, passwordEncoder.encode(password));
        Account account = new Account(Utilities.Number(accountRepository), LocalDateTime.now(), 0.0, true, AccountType.CURRENT);
        newClient.addAccount(account);
        clientService.save(newClient);
        accountRepository.save(account);
        return new ResponseEntity<>("Welcome!!!",HttpStatus.CREATED);}

    @RequestMapping("/clients/current")
    public ClientDTO getCurrentClient(Authentication authentication){
        Client clientAuth= clientService.findByEmail(authentication.getName());
        Set<Card> cardsTrue = clientAuth.getCards().stream().filter(card -> card.getDeleteCard() == true).collect(toSet());
        clientAuth.setCards(cardsTrue);
        Set<Account> accountsTrue = clientAuth.getAccounts().stream().filter(account -> account.getDeleteAccount() == true).collect(toSet());
        clientAuth.setAccounts(accountsTrue);
        return new ClientDTO(clientAuth);
    }
}

//IR A ACCOUNT CONTROLLER
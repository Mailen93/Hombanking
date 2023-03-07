package com.mindhub.homebanking.Controlers;

import com.mindhub.homebanking.Utils.Utilities;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.mindhub.homebanking.Utils.Utilities.GenerateNumber;
import static java.util.stream.Collectors.toList;
@RestController
@RequestMapping("/api")
public class ClientController{
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ClientRepositories clientRepositories;
    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping("/clients")
    public List<ClientDTO> getClientDto(){
        return clientRepositories.findAll().stream().map(client -> new ClientDTO(client)).collect(toList());}

    @RequestMapping("/clients/{id}")
    public Optional<Object> getClient(@PathVariable Long id){
        return clientRepositories.findById(id).map(ClientDTO::new);}

    @RequestMapping(path = "/clients", method = RequestMethod.POST)
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

        if (clientRepositories.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Email already in use", HttpStatus.BAD_REQUEST);}

       Client newClient = new Client(first, lastName, email, passwordEncoder.encode(password));
        Account account = new Account(Utilities.Number(accountRepository), LocalDateTime.now(), 0);
        newClient.addAccount(account);
        clientRepositories.save(newClient);
        accountRepository.save(account);
        return new ResponseEntity<>("Welcome!!!",HttpStatus.CREATED);}

    @RequestMapping("/clients/current")
    public ClientDTO getCurrentClient(Authentication authentication){
        String email = authentication.getName();
        return new ClientDTO(clientRepositories.findByEmail(email));}
}

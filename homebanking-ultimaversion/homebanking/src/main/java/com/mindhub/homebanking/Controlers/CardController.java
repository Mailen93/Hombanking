package com.mindhub.homebanking.Controlers;

import com.mindhub.homebanking.dtos.AccountsDTO;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.util.List;

import static com.mindhub.homebanking.Utils.Utilities.*;
import static java.util.stream.Collectors.toList;

@RestController   //CREAR METODO QUE NOS PERMITA CREAR TARJETAS
@RequestMapping("/api")
public class CardController {
    @Autowired
    ClientRepositories clientRepositories;
    @Autowired
    CardRepository cardRepository;

    @RequestMapping("/clients/current/cards")
    public List<CardDTO> getCurrentCards(Authentication authentication) {
        return clientRepositories.findByEmail(authentication.getName()).getCards().stream().map(card -> new CardDTO(card)).collect(toList());}

    @RequestMapping(path = "/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> newCard(Authentication authentication,
        @RequestParam CardType type,
        @RequestParam CardColor color) {
        Client client = clientRepositories.findByEmail(authentication.getName());

        if (client.getCards().size() >= 6){
            return new ResponseEntity<> ("You have so many cards",HttpStatus.FORBIDDEN);}
        if (client.getCards().stream().anyMatch(card -> type == card.getType() && color == card.getColor())){
            return new ResponseEntity<>("You already have this card category", HttpStatus.FORBIDDEN);}
        Card addNewCard = new Card(randomNumberCard(cardRepository),returnCvvNumber(), type, color, LocalDate.now(),LocalDate.now().plusYears(5), client );
        client.addCard(addNewCard);
        cardRepository.save(addNewCard);
        return new ResponseEntity<>("You have created a new card", HttpStatus.CREATED);}

}




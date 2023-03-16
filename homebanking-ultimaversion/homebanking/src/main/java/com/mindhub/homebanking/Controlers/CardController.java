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
import org.springframework.web.bind.annotation.*;

import javax.security.auth.message.ClientAuth;
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
        Client clientAuth = clientRepositories.findByEmail(authentication.getName());
        List<Card> checkCard = clientAuth.getCards().stream().filter(card -> card.getDeleteCard() == true).collect(toList());
        return checkCard.stream().map(card -> new CardDTO(card)).collect(toList());
    }

    @RequestMapping(path = "/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> newCard(Authentication authentication,
                                          @RequestParam CardType type,
                                          @RequestParam CardColor color) {
        Client client = clientRepositories.findByEmail(authentication.getName());

        if (client.getCards().size() >= 6) {
            return new ResponseEntity<>("You have so many cards", HttpStatus.FORBIDDEN);
        }
        if (client.getCards().stream().anyMatch(card -> type == card.getType() && color == card.getColor() && card.getDeleteCard() == true)) {
            return new ResponseEntity<>("You already have this card category", HttpStatus.FORBIDDEN);
        }
        Card addNewCard = new Card(randomNumberCard(cardRepository), returnCvvNumber(), type, color, LocalDate.now(), LocalDate.now().plusYears(5), client, true);
        client.addCard(addNewCard);
        cardRepository.save(addNewCard);
        return new ResponseEntity<>("You have created a new card", HttpStatus.CREATED);}

    @PatchMapping("/clients/current/cards")
    public ResponseEntity<Object> deleteCard(Authentication authentication, @RequestParam String number) {
        Client clientAuth = clientRepositories.findByEmail(authentication.getName());
        Card deleteCard = cardRepository.findByNumber(number);
        if (number.isEmpty()) {
            return new ResponseEntity<>("Missing Number", HttpStatus.BAD_REQUEST);
        }
        if (clientAuth.getCards().stream().noneMatch(card -> card.getNumber().equals(number))) {
            return new ResponseEntity<>("Wrong Number", HttpStatus.BAD_REQUEST);
        }
        deleteCard.setDeleteCard(false);
        cardRepository.save(deleteCard);
        return new ResponseEntity<>("Your card has been deleted", HttpStatus.ACCEPTED);}

}




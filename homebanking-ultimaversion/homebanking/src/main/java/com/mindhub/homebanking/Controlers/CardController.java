package com.mindhub.homebanking.Controlers;

import com.mindhub.homebanking.dtos.AccountsDTO;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.PaymentServiceDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepositories;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.message.ClientAuth;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.mindhub.homebanking.Utils.Utilities.*;
import static com.mindhub.homebanking.models.CardType.DEBIT;
import static java.util.stream.Collectors.toList;

@RestController   //CREAR METODO QUE NOS PERMITA CREAR TARJETAS
@RequestMapping("/api")
public class CardController {
    @Autowired
    ClientRepositories clientRepositories;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    AccountRepository accountRepository;

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

        if (client.getCards().stream().filter(card -> card.getDeleteCard() == true).count() >= 6) {
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

    @Transactional
    @CrossOrigin
    @PostMapping("/pay")//nuevo servlet
    public ResponseEntity <Object> payCards(
            @RequestBody(required = false) PaymentServiceDTO paymentServiceDTO){
        Card card = cardRepository.findByNumber(paymentServiceDTO.getNumber());
        if (paymentServiceDTO.getNumber().isEmpty()){
            return new ResponseEntity<>("The card number is missing",HttpStatus.BAD_REQUEST);}
        if (paymentServiceDTO.getAmount() == null && paymentServiceDTO.getAmount()>0){
            return new ResponseEntity<>("The amount to be paid is missing",HttpStatus.BAD_REQUEST);}
        if (paymentServiceDTO.getCvv().isEmpty())
            return new ResponseEntity<>("The card cvv is missing",HttpStatus.BAD_REQUEST);
        if (paymentServiceDTO.getDescription().isEmpty())
            return new ResponseEntity<>("The description is missing",HttpStatus.BAD_REQUEST);
        if (card == null)
            return new ResponseEntity<>("Card not found",HttpStatus.BAD_REQUEST);
        if (!card.getDeleteCard())
            return new ResponseEntity<>("The card is not valid",HttpStatus.BAD_REQUEST);
        if (card.getType() != DEBIT)
            return new ResponseEntity<>("The card is not debit",HttpStatus.BAD_REQUEST);
        if (card.getThruDate().isBefore(LocalDate.now()))
            return new ResponseEntity<>("The card has expired",HttpStatus.BAD_REQUEST);
        Account account = card.getCardHolder().getAccounts().stream().iterator().next();
        if (account == null)
            return new ResponseEntity<>("There is no associated account",HttpStatus.BAD_REQUEST);
        if (!account.getDeleteAccount())
            return new ResponseEntity<>("The associated account is inactive",HttpStatus.BAD_REQUEST);
        if (account.getBalance()<paymentServiceDTO.getAmount())
            return new ResponseEntity<>("Your account balance does not cover the payment",HttpStatus.BAD_REQUEST);
        if (!Objects.equals(paymentServiceDTO.getNumber(), card.getNumber()) ||!Objects.equals(paymentServiceDTO.getCvv(), card.getCvv()))
            return new ResponseEntity<>("the data entered is incorrect",HttpStatus.BAD_REQUEST);

        Transaction transaction = new Transaction(TransactionType.DEBIT,-paymentServiceDTO.getAmount(), paymentServiceDTO.getDescription(), LocalDateTime.now(),account.getBalance() - paymentServiceDTO.getAmount());
        account.setBalance(account.getBalance() - paymentServiceDTO.getAmount());
        account.addTransaction(transaction);
        transactionRepository.save(transaction);
        accountRepository.save(account);
        return new ResponseEntity<>("Payment made",HttpStatus.ACCEPTED);
    }
}




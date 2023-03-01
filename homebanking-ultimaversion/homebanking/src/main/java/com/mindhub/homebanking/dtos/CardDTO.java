package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;

import java.time.LocalDate;

public class CardDTO {

    private long Id;
    private String number;
    private String cvv;
    private CardType cardType;
    private CardColor cardColor;
    private LocalDate fromDate;
    private LocalDate thruDate;
    private String cardHolder;

    public CardDTO(Card card) {
        this.cardHolder = card.getCardHolder().getFirstName()+ " " + card.getCardHolder().getLastName();
        this.Id = card.getId();
        this.number = card.getNumber();
        this.cvv = card.getCvv();
        this.cardType = card.getType();
        this.cardColor = card.getColor();
        this.thruDate = card.getThruDate();
        this.fromDate = card.getFromDate();
    }

    public long getId() {return Id;}

    public String getNumber() {return number;}

    public String getCvv() {return cvv;}

    public CardType getCardType() {return cardType;}

    public CardColor getCardColor() {return cardColor;}

    public LocalDate getFromDate() {return fromDate;}

    public LocalDate getThruDate() {return thruDate;}

    public String getCardHolder() {return cardHolder;}
}

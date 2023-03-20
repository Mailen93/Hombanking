package com.mindhub.homebanking.models;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")

    private Long Id;
    private String number;
    private String cvv;
    private CardType type;
    private CardColor color;
    private LocalDate fromDate;
    private LocalDate thruDate;
    private Boolean deleteCard;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client cardHolder;

    public Card(){}
    public Card(String number, String cvv, CardType type, CardColor color, LocalDate fromDate, LocalDate truDate, Client cardHolder, Boolean deleteCard) {
        this.number = number;
        this.cvv = cvv;
        this.type = type;
        this.color = color;
        this.fromDate = fromDate;
        this.thruDate = truDate;
        this.cardHolder = cardHolder;
        this.deleteCard = deleteCard;}

    public Long getId() {return Id;}
    public String getNumber() {return number;}
    public void setNumber(String number) {this.number = number;}
    public String getCvv() {return cvv;}
    public void setCvv(String cvv) {this.cvv = cvv;}
    public CardType getType() {return type;}
    public void setType(CardType type) {this.type = type;}
    public CardColor getColor() {return color;}
    public void setColor(CardColor color) {this.color = color;}
    public LocalDate getFromDate() {return fromDate;}
    public void setFromDate(LocalDate fromDate) {this.fromDate = fromDate;}
    public LocalDate getThruDate() {return thruDate;}
    public void setThruDate(LocalDate truDate) {this.thruDate = truDate;}
    public Client getCardHolder() {return cardHolder;}
    public void setCardHolder(Client cardHolder) {this.cardHolder = cardHolder;}
    public Boolean getDeleteCard() {return deleteCard;}
    public void setDeleteCard(Boolean deleteCard) {this.deleteCard = deleteCard;}

}

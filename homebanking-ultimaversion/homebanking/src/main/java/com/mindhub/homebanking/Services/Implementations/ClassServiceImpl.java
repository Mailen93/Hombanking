package com.mindhub.homebanking.Services.Implementations;

import com.mindhub.homebanking.Services.CardService;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClassServiceImpl implements CardService {
    @Autowired
    private CardRepository cardRepository;
    @Override
    public Boolean existsCardByNumber(String number) {
        return cardRepository.existsCardByNumber(number);
    }

    @Override
    public Card findByNumber(String number) {
        return cardRepository.findByNumber(number);
    }

    @Override
    public void save(Card card) {
        cardRepository.save(card);
    }
}

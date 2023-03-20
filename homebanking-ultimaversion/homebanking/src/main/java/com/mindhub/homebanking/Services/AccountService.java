package com.mindhub.homebanking.Services;

import com.mindhub.homebanking.models.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    Boolean existsByNumber(String number);
    Account findByNumber(String number);
    void save(Account account);
    List<Account> findAll();
    Optional<Account> findById(Long Id);

}

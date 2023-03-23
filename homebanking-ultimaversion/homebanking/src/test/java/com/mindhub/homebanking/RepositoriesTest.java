package com.mindhub.homebanking;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import org.junit.jupiter.api.Test;

import java.util.List;

@DataJpaTest

@AutoConfigureTestDatabase(replace = NONE)

public class RepositoriesTest {


    @Autowired
    LoanRepository loanRepository;
    @Autowired
    ClientRepositories clientRepositories;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    ClientLoanRepository clientLoanRepository;
    @Autowired
    TransactionRepository transactionRepository;


    @Test
    public void existLoans(){

        List<Loan> loans = loanRepository.findAll();

        assertThat(loans,is(not(empty())));

    }

    @Test
    public void existPersonalLoan(){

        List<Loan> loans = loanRepository.findAll();

        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));

    }


    @Test
    public void existClients(){

        List<Client> clients = clientRepositories.findAll();

        assertThat(clients,is(not(empty())));

    }

    @Test
    public void existEmailClients() {

        List<Client> clients = clientRepositories.findAll();

        assertThat(clients, hasItem(hasProperty("email", isA(String.class))));

    }


    @Test
    public void existAccounts(){

        List<Account> accounts = accountRepository.findAll();

        assertThat(accounts,is(not(empty())));

    }

    @Test
    public void existNumberAccounts() {

        List<Account> accounts = accountRepository.findAll();

        assertThat(accounts, hasItem(hasProperty("number", isA(String.class))));

    }


    @Test
    public void existCards(){

        List<Card> cards = cardRepository.findAll();

        assertThat(cards,is(not(empty())));

    }

    @Test
    public void existColorCards() {

        List<Card> cards = cardRepository.findAll();

        assertThat(cards, hasItem(hasProperty("color", is(CardColor.GOLD))));

    }

    @Test
    public void existClientLoan(){

        List<ClientLoan> clientLoans = clientLoanRepository.findAll();

        assertThat(clientLoans,is(not(empty())));

    }

    @Test
    public void existClientLoanAmount(){

        List<ClientLoan> clientLoans = clientLoanRepository.findAll();

        assertThat(clientLoans, hasItem(hasProperty("amount", isA(Double.class))));

    }

    @Test
    public void existTransaction(){

        List<Transaction> transactions = transactionRepository.findAll();

        assertThat(transactions,is(not(empty())));

    }

    @Test
    public void existTransactionType(){

        List<Transaction> transactions = transactionRepository.findAll();

        assertThat(transactions, hasItem(hasProperty("type", is(TransactionType.DEBIT))));

    }


}

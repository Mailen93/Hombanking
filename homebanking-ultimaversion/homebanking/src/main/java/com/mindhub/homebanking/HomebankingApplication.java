package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.mindhub.homebanking.Utils.Utilities.randomNumberCard;
import static com.mindhub.homebanking.Utils.Utilities.returnCvvNumber;

@SpringBootApplication  //Le indica a spring que será mi punto de partida, la clase principal de una app sprinboot y trae anotaciones de coniguracion
public class HomebankingApplication {
	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {SpringApplication.run(HomebankingApplication.class, args);}
	@Bean  //Le digo a spring que instancie el metodo innit data para que sea usada ni bien se ejecute el main
	public CommandLineRunner initData(ClientRepositories Clientrepositories, AccountRepository AccountR, TransactionRepository TransacR, LoanRepository LoanR, ClientLoanRepository CloanR, CardRepository CardR) {
		return (args) -> {
//comandlinerunner interfaz que me permite ejecutar una tarea luego que la app se haya inicializado. init Data: metodo que implementa la interfaz y crea los objetos necesarios para inicializar la app (clientes, cuentas, trans, prestamos)
//			Clientrepositories.save(new Client("Melba", "Morel", "melba@mindhub.com"));
//			Clientrepositories.save(new Client("Mailen", "Alvarez", "m.labarrere2@gmail.com"));
			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("password"));
			Client client2 = new Client ("Mailen" , "Alvarez" , "m.labarrere2@gmail.com", passwordEncoder.encode("password1"));
			Client admin = new Client ("Admin", "Admin", "admin@gmail.com", passwordEncoder.encode("admin"));
			Account account1 = new Account("VIN 001", LocalDateTime.now(), 5000.0);
			Account account2 = new Account("VIN 002", LocalDateTime.now().plusDays(1), 7500.0);
			Account account3 = new Account("3" , LocalDateTime.now(), 100000.00);
			Account account4 = new Account("4", LocalDateTime.now().plusDays(1), 50.0);
			Transaction transaction1 = new Transaction(TransactionType.CREDIT, 2000.05, "Trabajo Feriado" ,LocalDateTime.now());
			Transaction transaction2 = new Transaction(TransactionType.DEBIT, 750.01, "Pintura para muebles" ,LocalDateTime.now());
			Transaction transaction3 = new Transaction(TransactionType.CREDIT, 200000.01, "Pago mensual" ,LocalDateTime.now());
			Transaction transaction4 = new Transaction(TransactionType.DEBIT, 1000.00, "Paseador de perros" ,LocalDateTime.now());
//			Transaction transaction5 = new Transaction(TransactionType.DEBIT, 7600.01, "Traje de baño" ,LocalDateTime.now());
			Transaction transaction6 = new Transaction(TransactionType.CREDIT, 3000.01, "Transferencia MP" ,LocalDateTime.of(2022,10,1,22,01));
			Transaction transaction7 = new Transaction(TransactionType.DEBIT, 6500.20, "Mesita de Luz" ,LocalDateTime.now());
//			Transaction transaction8 = new Transaction(TransactionType.CREDIT, 4250.30, "Devolución dinero" ,LocalDateTime.now());


			Loan hipotecario = new Loan("Hipotecario", 500000, List.of(12,24,36,48,60));
			Loan automotriz = new Loan("Automotriz", 300000, List.of(6,12,24,36));
			Loan personal = new Loan("Personal", 100000, List.of(6,12,24));
			Card card1 = new Card(randomNumberCard(CardR),returnCvvNumber(),CardType.CREDIT, CardColor.TITANIUM, LocalDate.now(),LocalDate.now().plusYears(5),client1);
			Card card2 = new Card(randomNumberCard(CardR),returnCvvNumber(),CardType.DEBIT, CardColor.GOLD, LocalDate.now(),LocalDate.now().plusYears(5),client1);


			ClientLoan clientLoan1 = new ClientLoan(400000.0, 60);  //creo el objeto
			ClientLoan clientLoan2 = new ClientLoan(50000.0, 12);
			ClientLoan clientLoan3 = new ClientLoan(100000.0, 24);
			ClientLoan clientLoan4 = new ClientLoan(200000.0, 36);

            client1.addAccount(account1);
			client1.addAccount(account2);
			client2.addAccount(account3);
			client2.addAccount(account4);

			account1.addTransaction(transaction1);
			account1.addTransaction(transaction7);
//			account1.addTransaction(transaction8);
			account2.addTransaction(transaction2);
//			account2.addTransaction(transaction5);
			account2.addTransaction(transaction6);
			account3.addTransaction(transaction3);
			account4.addTransaction(transaction4);

			client1.addClientLoan(clientLoan1);
			hipotecario.addClientLoan(clientLoan1);  //Asigno un cliente y un tipo de prestamo
			client1.addClientLoan(clientLoan2);
			personal.addClientLoan(clientLoan2);

			client1.addCard(card1);
			client1.addCard(card2);


			client2.addClientLoan(clientLoan3);
			personal.addClientLoan(clientLoan3);
			client2.addClientLoan(clientLoan4);
			automotriz.addClientLoan(clientLoan4);
			

			Clientrepositories.save(client1);
			Clientrepositories.save(client2);
			Clientrepositories.save(admin);
			AccountR.save(account1);
			AccountR.save(account2);
			AccountR.save(account3);
			AccountR.save(account4);
			TransacR.save(transaction1);
			TransacR.save(transaction2);
			TransacR.save(transaction3);
			TransacR.save(transaction4);
//			TransacR.save(transaction5);
			TransacR.save(transaction6);
			TransacR.save(transaction7);
//			TransacR.save(transaction8);
			LoanR.save(hipotecario);
			LoanR.save(automotriz);
			LoanR.save(personal);
			CloanR.save(clientLoan1);
			CloanR.save(clientLoan2);
			CloanR.save(clientLoan3);
			CloanR.save(clientLoan4);
			CardR.save(card1);
			CardR.save(card2);

		};
	}
}

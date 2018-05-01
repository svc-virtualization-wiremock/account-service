package io.api.rest.virtualization.accountservice;

import io.api.rest.virtualization.accountservice.entity.Account;
import io.api.rest.virtualization.accountservice.repository.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class AccountServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountServiceApplication.class, args);
	}

	@Bean
    @Profile("mock-data")
	CommandLineRunner init(AccountRepository accountRepository) {
		return args -> {
			List<Account> accountList = new ArrayList<>();
			accountList.add(Account.builder().id("1").accountName("Account 1").accountNo("0001").accountType("Savings").customerId("1").build());
			accountList.add(Account.builder().id("2").accountName("Account 2").accountNo("0002").accountType("Current").customerId("1").build());
			accountList.add(Account.builder().id("3").accountName("Account 3").accountNo("0003").accountType("Savings").customerId("2").build());
			accountList.add(Account.builder().id("4").accountName("Account 4").accountNo("0004").accountType("Savings").customerId("3").build());
			accountList.add(Account.builder().id("5").accountName("Account 5").accountNo("0005").accountType("Current").customerId("3").build());
			accountRepository.saveAll(accountList);
		};
	}
}

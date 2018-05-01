package io.api.rest.virtualization.accountservice.controller;

import io.api.rest.virtualization.accountservice.entity.Account;
import io.api.rest.virtualization.accountservice.exception.NotFoundException;
import io.api.rest.virtualization.accountservice.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/accounts")
    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    @GetMapping("/accounts/{id}")
    public Account getAccount(@PathVariable String id) {
        Optional<Account> accountHolder = accountRepository.findById(id);
        return accountHolder.orElseThrow(NotFoundException::new);
    }

    @PostMapping("/accounts")
    @ResponseStatus(HttpStatus.CREATED)
    public Account createAccount(@RequestBody Account account) {
        return accountRepository.save(account);
    }

    @PutMapping("/accounts/{id}")
    public Account updateAccount(@PathVariable String id, @RequestBody Account account) {
        Optional<Account> accountHolder = accountRepository.findById(id);
        return accountHolder.map(dbAccount -> {
            if (!StringUtils.isEmpty(account.getAccountName())) {
                dbAccount.setAccountName(account.getAccountName());
            }
            if (!StringUtils.isEmpty(account.getAccountNo())) {
                dbAccount.setAccountNo(account.getAccountNo());
            }
            if (!StringUtils.isEmpty(account.getAccountType())) {
                dbAccount.setAccountType(account.getAccountType());
            }
            if (!StringUtils.isEmpty(account.getCustomerId())) {
                dbAccount.setCustomerId(account.getCustomerId());
            }
            return accountRepository.save(dbAccount);
        }).orElseThrow(NotFoundException::new);
    }

    @DeleteMapping("/accounts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount(@PathVariable String id) {
        try {
            accountRepository.deleteById(id);
        } catch(EmptyResultDataAccessException e) {
            throw new NotFoundException();
        }
    }
}

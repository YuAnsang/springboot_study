package com.github.asyu.springbootsecurity2;

import com.github.asyu.springbootsecurity2.account.Account;
import com.github.asyu.springbootsecurity2.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AccountRunner implements ApplicationRunner {

    @Autowired
    AccountService accountService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Account account = accountService.createAccount("asyu", "1234");
        System.out.println(account.getUsername() + " password : " + account.getPassword());
    }
}

package com.github.asyu.springbootneo4j;

import com.github.asyu.springbootneo4j.account.Account;
import com.github.asyu.springbootneo4j.account.AccountRepository;
import com.github.asyu.springbootneo4j.account.Role;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class Neo4jRunner implements ApplicationRunner {

    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    AccountRepository accountRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Account account = new Account();
        account.setEmail("kahlman2@naver.com");
        account.setUsername("jhso");

        Role role = new Role();
        role.setName("user");

        account.getRoles().add(role);

        accountRepository.save(account);

        System.out.println("complete");
    }
}

package com.github.asyu.sixth.user;

import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @PostMapping("/users/create")
    public User craete(@RequestBody User user) {
        return user;
    }
}

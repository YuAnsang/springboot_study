package com.github.asyu.seventh;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SampleController {

    @GetMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("name", "asyu");
        return "hello";
    }

    @GetMapping("/exception")
    public String test() {
        throw new SampleException();
    }

    @ExceptionHandler(SampleException.class)
    public @ResponseBody ResponseEntity<String> handler(SampleException e) {
        return ResponseEntity.ok("error");
    }

}

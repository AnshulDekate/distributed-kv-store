package com.example.database.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Front {
    @GetMapping("/front")
    public String front(){
        return "Hello world";
    }
}

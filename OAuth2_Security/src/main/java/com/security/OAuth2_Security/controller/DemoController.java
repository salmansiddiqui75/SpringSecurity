package com.security.OAuth2_Security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController
{
    @GetMapping("/sec")
    public ResponseEntity<String> sayHello()
    {
        return ResponseEntity.ok("Hi");
    }
}

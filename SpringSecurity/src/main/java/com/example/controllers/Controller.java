package com.example.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller
{
    @GetMapping("/hello")
    public String sayHello()
    {
        return "Hello welcome to spring security";
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user")
    public String heyUser()
    {
        return "Hello ! USER";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String heyAdmin()
    {
        return "Hello ! Admin";
    }
}

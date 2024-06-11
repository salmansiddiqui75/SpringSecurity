package com.example.controllers;

import com.example.jwt.JwtUtils;
import com.example.jwt.LogginRequest;
import com.example.jwt.LogginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class Controller
{
    @Autowired
    private AuthenticationManager authenticationManager;

    private JwtUtils jwtUtils;
    private LogginRequest logginRequest;
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

    @PostMapping("/signin")

    public ResponseEntity<?> authenticateUser(@RequestBody LogginRequest logginRequest)
    {
        Authentication authentication;
        try{
            authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(logginRequest.getUsername(),logginRequest.getPassword()));
        }
        catch (AuthenticationException exception)
        {
            Map<String ,Object> map=new HashMap<>();
            map.put("message","Bad credentials");
            map.put("status",false);
            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails= (UserDetails) authentication.getPrincipal();
        String jwtToken=jwtUtils.generateTokenFromUsername(userDetails);

        List<String> roles=userDetails.getAuthorities().stream().map(item->item.getAuthority()).collect(Collectors.toList());

        LogginResponse response=new LogginResponse(userDetails.getUsername(),jwtToken,roles);
        return ResponseEntity.ok(response);

    }
}

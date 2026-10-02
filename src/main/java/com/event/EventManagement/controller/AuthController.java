package com.event.EventManagement.controller;

import com.event.EventManagement.entity.AuthModel;
import com.event.EventManagement.entity.JwtResponse;
import com.event.EventManagement.entity.User;
import com.event.EventManagement.security.JwtTokenService;
import com.event.EventManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenService jwtTokenService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody AuthModel authModel){

      Authentication authentication =  authenticationManager
              .authenticate(new UsernamePasswordAuthenticationToken(authModel.getEmail(),authModel.getPassword()));
      var token = jwtTokenService.generateToken(authentication);
        return ResponseEntity.status(HttpStatus.OK).body(new JwtResponse(token));

    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user){

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
    }

    @PostMapping("/authenticate")
    public Authentication authentication(Authentication authentication){
        return authentication;
    }

}

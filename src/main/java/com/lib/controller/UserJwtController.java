package com.lib.controller;

import com.lib.dto.request.RegisterRequest;
import com.lib.dto.response.LibResponse;
import com.lib.dto.response.ResponseMessage;
import com.lib.security.jwt.JwtUtils;
import com.lib.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserJwtController {

    private JwtUtils jwtUtils;

    private UserService userService;

    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<LibResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest){
        userService.saveUser(registerRequest);

        LibResponse libResponse=new LibResponse(ResponseMessage.REGİSTER_RESPONSE_MESSAGE,true);
        return new ResponseEntity<>(libResponse, HttpStatus.CREATED);

    }
}

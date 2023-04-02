package com.lib.controller;

import com.lib.dto.request.LoginRequest;
import com.lib.dto.request.RegisterRequest;
import com.lib.dto.response.LibResponse;
import com.lib.dto.response.LoginResponse;
import com.lib.dto.response.ResponseMessage;
import com.lib.security.jwt.JwtUtils;
import com.lib.service.UserService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserJwtController {

    private final JwtUtils jwtUtils;


    private final UserService userService;


    private final AuthenticationManager authenticationManager;

    public UserJwtController(JwtUtils jwtUtils, UserService userService, AuthenticationManager authenticationManager) {
        this.jwtUtils = jwtUtils;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }


    @PostMapping("/register")
    public ResponseEntity<LibResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        userService.saveUser(registerRequest);

        LibResponse libResponse = new LibResponse(ResponseMessage.REGİSTER_RESPONSE_MESSAGE, true);
        return new ResponseEntity<>(libResponse, HttpStatus.CREATED);

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@Valid @RequestBody LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());//Kullanıcı bilgilerini zarfladık AutheManager Türüne çevirdik

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);//Authentice edilmiş kullanıcı oluşturuldu

        //Token üretimine geçildi
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();//Anlık login yapan kullanıcı bilgilerini getirdi

        String jwtToken = jwtUtils.generateJwtToken(userDetails);//token oluştu.

        LoginResponse loginResponse = new LoginResponse(jwtToken);
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);

    }
}

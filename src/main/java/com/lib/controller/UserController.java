package com.lib.controller;

import com.lib.dto.UserDTO;
import com.lib.dto.request.UserCreationRequest;
import com.lib.dto.response.LibResponse;
import com.lib.dto.response.ResponseMessage;
import com.lib.mapper.UserMapper;
import com.lib.service.RoleService;
import com.lib.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private final RoleService roleService;



    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;

    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public ResponseEntity<List<UserDTO>> getAllUser(){
       List<UserDTO> listUserDTO= userService.getAllUser();

       return ResponseEntity.ok(listUserDTO);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public ResponseEntity<LibResponse> userCreation(@Valid @RequestBody UserCreationRequest userCreationRequest){
        userService.createUser(userCreationRequest);

        LibResponse libResponse=new LibResponse(ResponseMessage.USER_CREATED_RESPONSE_MESSAGE,true);
        return new ResponseEntity<>(libResponse, HttpStatus.CREATED);
    }





}

package com.lib.controller;

import com.lib.dto.UserDTO;
import com.lib.dto.request.AdminUserUpdateRequest;
import com.lib.dto.request.UpdatePasswordRequest;
import com.lib.dto.request.UserCreationRequest;
import com.lib.dto.response.LibResponse;
import com.lib.dto.response.ResponseMessage;
import com.lib.mapper.UserMapper;
import com.lib.service.RoleService;
import com.lib.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    //GetALLUser
    @GetMapping("/users/all")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public ResponseEntity<List<UserDTO>> getAllUser(){
       List<UserDTO> listUserDTO= userService.getAllUser();

       return ResponseEntity.ok(listUserDTO);
    }
    //Create User
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public ResponseEntity<LibResponse> userCreation(@Valid @RequestBody UserCreationRequest userCreationRequest){
        userService.createUser(userCreationRequest);

        LibResponse libResponse=new LibResponse(ResponseMessage.USER_CREATED_RESPONSE_MESSAGE,true);
        return new ResponseEntity<>(libResponse, HttpStatus.CREATED);
    }
    @GetMapping("/user")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public ResponseEntity<UserDTO> getUser(){
        UserDTO userDTO=userService.getPrincipal();
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id){
        UserDTO userDTO=userService.getUserById(id);
        return ResponseEntity.ok(userDTO);

    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public ResponseEntity<Page<UserDTO>> getAllUserByPage(@RequestParam("page") int page,
                                                          @RequestParam("size")int size,
                                                          @RequestParam("sort") String prop,
                                                          @RequestParam(value = "direction",
                                                                  required=false,
                                                            defaultValue = "DESC") Sort.Direction direction
                                                                        ){

        Pageable pageable= PageRequest.of(page,size,Sort.by(direction,prop));
        Page<UserDTO> userDTOPage=userService.getAllUserWithPAge(pageable);

        return ResponseEntity.ok(userDTOPage);
    }


    @PatchMapping("/reset/password")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE') or hasRole('MEMBER')")
    public ResponseEntity<LibResponse> updatePassword(@Valid @RequestBody
                                                          UpdatePasswordRequest updatePasswordRequest){
        userService.updatePassword(updatePasswordRequest);
        LibResponse libResponse=new LibResponse(ResponseMessage.PASSWORD_UPDATED_RESPONSE_MESSAGE,true);

        return ResponseEntity.ok(libResponse);
    }

    @PutMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE') ")
    public ResponseEntity<LibResponse> updateUserByAdmın( @PathVariable Long id,
                                                                     @Valid @RequestBody AdminUserUpdateRequest adminUserUpdateRequest){
        userService.updateUserByAdmin(id,adminUserUpdateRequest);
        LibResponse libResponse=new LibResponse(ResponseMessage.USER_UPDATED_RESPONSE_MESSAGE,true);

        return ResponseEntity.ok(libResponse);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE') or hasRole('MEMBER')")
    public ResponseEntity<LibResponse> deleteUser(@PathVariable  Long id){
        userService.deleteUserById(id);

        LibResponse libResponse=new LibResponse(ResponseMessage.USER_DELETE_RESPONSE_MESSAGE,true);

        return ResponseEntity.ok(libResponse);
    }







}

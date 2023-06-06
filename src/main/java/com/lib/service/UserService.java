package com.lib.service;

import com.lib.domain.Role;
import com.lib.domain.User;
import com.lib.domain.enums.RoleType;
import com.lib.dto.UserDTO;
import com.lib.dto.request.RegisterRequest;
import com.lib.dto.request.UserCreationRequest;
import com.lib.exception.ConflictException;
import com.lib.exception.ResourceNotFoundException;
import com.lib.exception.message.ErrorMessage;
import com.lib.mapper.UserMapper;
import com.lib.repository.UserRepository;
import com.lib.security.SecurityUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {


    private final UserRepository userRepository;

    private final RoleService roleService;
    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleService roleService, UserMapper userMapper, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserByEmail(String email){
       User user= userRepository.findByEmail(email).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.USER_NOT_FOUND_EXCEPTION,email)));
        return user;
    }

    public void saveUser(RegisterRequest registerRequest) {

        if (userRepository.existsByEmail(registerRequest.getEmail())){//email hazırda var mı?
            throw new ConflictException(String.format(ErrorMessage.EMAIL_ALREADY_EXIST_MESSAGE,registerRequest.getEmail()));
        }

        //!!role bilgisini atama
        Role role=roleService.findByType(RoleType.ROLE_MEMBER);
        Set<Role> roles=new HashSet<>();
        roles.add(role);

        //sifre encode edilcek

        String password= registerRequest.getPassword();
        String encodedPassword=passwordEncoder.encode(password);

        //yeni kullanıcının bilgilerini setlemek
        User user=new User();
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setAddress(registerRequest.getAddress());
        user.setPhone(registerRequest.getPhone());
        user.setBirthDate(registerRequest.getBirthDate());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encodedPassword);
        user.setResetPasswordCode(registerRequest.getResetPasswordCode());
        user.setRoles(roles);

        userRepository.save(user);


    }

    public List<UserDTO> getAllUser() {

        List<User> userList=userRepository.findAll();

        return userMapper.userListToUserDtoList(userList);
    }

    public void createUser(UserCreationRequest userCreationRequest) {
        Boolean existEmail=userRepository.existsByEmail(userCreationRequest.getEmail());

        if (existEmail){
            throw new ConflictException(String.format(ErrorMessage.EMAIL_ALREADY_EXIST_MESSAGE));
        }

        Role role=roleService.findByType(RoleType.ROLE_MEMBER);
        Set<Role> roles=new HashSet<>();
        roles.add(role);

        String password= userCreationRequest.getPassword();
        String encodedPassword= passwordEncoder.encode(password);

        User user=new User();
        user.setFirstName(userCreationRequest.getFirstName());
        user.setLastName(userCreationRequest.getLastName());
        user.setAddress(userCreationRequest.getAddress());
        user.setPhone(userCreationRequest.getPhone());
        user.setBirthDate(userCreationRequest.getBirthDate());
        user.setEmail(userCreationRequest.getEmail());
        user.setPassword(encodedPassword);
        user.setResetPasswordCode(userCreationRequest.getResetPasswordCode());
        user.setRoles(roles);

        userRepository.save(user);


    }

    public UserDTO getPrincipal() {
        User user=getCurrentUser();
        UserDTO userDTO=userMapper.userToUserDTO(user);
        return userDTO;
    }

    private User getCurrentUser(){
        String email=SecurityUtils.getCurrentUserLogin().orElseThrow(() ->
                new ResourceNotFoundException(ErrorMessage.PRINCIPAL_FOUND_MESSAGE));
        User user =getUserByEmail(email);
        return user;
    }

    public UserDTO getUserById(Long id) {
        User user=userRepository.findUserById(id).orElseThrow(()->
                new ResourceNotFoundException(ErrorMessage.USER_NOT_FOUND_EXCEPTION));
        UserDTO userDTO=userMapper.userToUserDTO(user);
        return userDTO;
    }

    public Page<UserDTO> getAllUserWithPAge(Pageable pageable) {
       Page<User> userPage= userRepository.findAll(pageable);
       return getUserPage(userPage);
    }
    private Page<UserDTO> getUserPage(Page<User> userPage){
        return userPage.map(user -> userMapper.userToUserDTO(user));

    }
}

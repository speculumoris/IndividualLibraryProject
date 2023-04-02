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
import org.springframework.context.annotation.Lazy;
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

    public void createUser(UserCreationRequest userRequest) {
        Boolean existEmail=userRepository.existsByEmail(userRequest.getEmail());
        if (existEmail){
            throw new ConflictException(String.format(ErrorMessage.EMAIL_ALREADY_EXIST_MESSAGE));
        }


    }
}

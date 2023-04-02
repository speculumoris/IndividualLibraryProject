package com.lib.dto;

import com.lib.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;



    private String firstName;

    private String lastName;

    private Integer score=0;


    private String address;


    private String phone;


    private LocalDate birthDate;


    private String email;

    private String password;

    private LocalDateTime createDate = LocalDateTime.now();

    private String resetPasswordCode;


    private boolean builtIn=false;


    private Set<String> roles=new HashSet<>();

    public void setRoles(Set<Role> roles){
        Set<String> roleStr=new HashSet<>();
        roles.forEach(r->{
            roleStr.add(r.getRoleType().getName());//Employee,Admınıstrator,member
        });
        this.roles=roleStr;
    }
}

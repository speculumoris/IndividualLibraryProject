package com.lib.domain;

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

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Table(name = "t_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotNull(message = "first name cannot be null")
    @Size(min = 2,max = 30,message = "First name '${validateValue}' should be between {min} and {max}")
    private String firstName;
    @NotNull(message = "last name cannot be null")
    @Size(min = 2,max = 30,message = "last name '${validateValue}' should be between {min} and {max}")
    private String lastName;
    @NotNull(message = "first name cannot be null")
    private Integer score=0;

    @NotNull(message = "last name cannot be null")
    @Size(min = 10,max = 100,message = "address '${validateValue}' should be between {min} and {max}")
    private String address;

    @Pattern(regexp = "^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$", //(541) 317-8828
            message = "Please provide valid phone number")
    @NotNull(message = "phone number cannot be null")
    private String phone;


    @Column(nullable = false)
    private LocalDate birthDate;

    @Email(message = "please provide valid email")
    @NotNull(message = "email cannot be null")
    private String email;

    @NotNull(message = "password cannot be null")
    private String password;

    private LocalDateTime createDate = LocalDateTime.now();

    @Column(nullable = false)
    private String resetPasswordCode;


    private boolean builtIn=false;

    @ManyToMany
    @JoinTable(name = "t_role_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles=new HashSet<>();

}

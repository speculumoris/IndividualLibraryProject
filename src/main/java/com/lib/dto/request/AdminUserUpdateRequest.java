package com.lib.dto.request;

import com.lib.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserUpdateRequest {

    @NotNull(message = "first name cannot be null")
    @Size(min = 2,max = 30,message = "First name '${validateValue}' should be between {min} and {max}")
    private String firstName;
    @NotNull(message = "last name cannot be null")
    @Size(min = 2,max = 30,message = "last name '${validateValue}' should be between {min} and {max}")
    private String lastName;

    @NotNull(message = "last name cannot be null")
    @Size(min = 10,max = 100,message = "address '${validateValue}' should be between {min} and {max}")
    private String address;

    @Pattern(regexp = "^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$", //(541) 317-8828
            message = "Please provide valid phone number")
    @NotNull(message = "phone number cannot be null")
    private String phone;


    @Column(nullable = false)
    @Pattern(regexp = "[1-9]{4}([-./])\\d{1,2}\\1\\d{1,2}")
    private String birthDate;

    @Email(message = "please provide valid email")
    @NotNull(message = "email cannot be null")
    private String email;

    @Column(nullable = false)
    private String resetPasswordCode;



}

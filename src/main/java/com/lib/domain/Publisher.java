package com.lib.domain;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Table(name = "t_publisher")
@Builder(toBuilder = true)
public class Publisher {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank(message = "please provide publisher name")
    @Size(min = 2, max = 25, message = "publisher '${validatedValue}' must be between {min} and {max} long")
    private String name;

    @NotNull
    private Boolean builtIn = false;





}

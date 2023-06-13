package com.lib.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class CategoryRequest {

    @NotBlank(message = "please provide a name")
    @Size(min = 4,max = 80,message = "Author name '${validateValue}' should be between {min} and {max}")
    private String name;

    @NotNull
    private Boolean builtIn;

    @NotNull
    private int sequence;








}

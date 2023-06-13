package com.lib.domain;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Author name can not be null")
    @Size(min = 4,max = 70,message = "Author name '${validateValue}' should be between {min} and {max}")
    private String name;

    @NotNull
    private boolean builtIn =false;

    @OneToMany(mappedBy = "author")
    private List<Book> bookList;


}

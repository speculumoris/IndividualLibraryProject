package com.lib.domain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Book name cannot be null")
    @Size(min = 2,max = 80,message = "Book name '${validateValue}' should be between {min} and {max}")
    private String name;

    @Pattern(regexp = "^\\d{3}-\\d{2}-\\d{5}-\\d{2}-\\d{1}$")
    @NotNull(message = "Book name cannot be null")
    @Column(length = 17)
    private String isbn;

    @Column(nullable = false)
    private Integer pageCount;


    @Pattern(regexp = "^\\d{4}$", message = "please enter : yyyy" )
    @Column(nullable = false)
    private Integer publishDate;


    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "book_id")
    private Set<ImageFile> imageFile;


    @NotNull(message = "ShelfCode cannot be null")
    @Column(length = 6)
    private String shelfCode;


    private boolean active=true;
    private boolean featured=false;
    private boolean loanable=true;
    @NotNull
    private LocalDateTime createDate = LocalDateTime.now();


    private boolean builtIn =false;

    @ManyToOne
    private Category category;

    @ManyToOne
    private Publisher publisher;

    @ManyToOne
    private Author author;



}

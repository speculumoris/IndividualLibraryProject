package com.lib.dto;


import com.lib.domain.Author;
import com.lib.domain.Category;
import com.lib.domain.ImageFile;


import com.lib.domain.Publisher;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Builder(toBuilder = true)
public class BookDTO {


    private Long id;

    @NotNull(message = "Book name cannot be null")
    @Size(min = 2,max = 80,message = "Book name '${validateValue}' should be between {min} and {max}")
    private String name;

    @Pattern(regexp = "^\\d{3}-\\d{2}-\\d{5}-\\d{2}-\\d{1}$")
    @NotNull(message = "Book name cannot be null")
    private String isbn;


    private Integer pageCount;

    private Integer publishDate;

    private Set<String> imageFile;


    @NotNull(message = "ShelfCode cannot be null")
    private String shelfCode;


    private boolean active=true;
    private boolean featured=false;
    private boolean loanable=true;


    private LocalDateTime createDate ;
    private boolean builtIn =false;

    private Category category;


    private Publisher publisher;


    private Author author;





}

package com.lib.dto;


import com.lib.domain.Author;
import com.lib.domain.ImageFile;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {


    private Long id;

    private String name;

    private String isbn;

    private Integer pageCount;


    private Integer publishDate;
    private CategoryDTO categoryId;

    private Set<String> imageFile;
    private boolean loanable;

    private String shelfCode;

    private boolean active;
    private boolean featured;

    private LocalDateTime createDate;
    private Boolean builtIn;




}

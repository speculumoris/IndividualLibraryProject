package com.lib.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class BookUpdateRequest {
    @NotNull(message = "Book name cannot be null")
    @Size(min = 2,max = 80,message = "Book name '${validateValue}' should be between {min} and {max}")
    private String name;

    @NotNull(message = "Book name cannot be null")
    private String isbn;

    private Integer pageCount;
    @NotNull(message = "Author cannot be null")
    private Long authorId;
    @NotNull(message = "Publisher cannot be null")
    private Long publisherId;

    @NotNull(message = "CategoryId cannot be null")
    private Long categoryId;

    private Integer publishDate;

    @NotNull(message = "ShelfCode cannot be null")
    private String shelfCode;
    @NotNull(message = "Featured cannot be null")
    private boolean featured;
    @NotNull(message = "Active cannot be null")
    private boolean active;
    @NotNull(message = "Loanable cannot be null")
    private boolean loanable;

    private String imageId;
    private boolean builtIn;

}

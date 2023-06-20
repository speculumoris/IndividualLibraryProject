package com.lib.dto.request;

import com.lib.domain.Author;
import com.lib.domain.Category;
import com.lib.domain.ImageFile;
import com.lib.domain.Publisher;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class SaveBookRequest {
    @NotNull(message = "Book name cannot be null")
    @Size(min = 2,max = 80,message = "Book name '${validateValue}' should be between {min} and {max}")
    private String name;

    @NotNull(message = "Book name cannot be null")
    @Pattern(regexp = "^\\d{3}-\\d{2}-\\d{5}-\\d{2}-\\d{1}$")
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

    private Set<String> imageFile;
}

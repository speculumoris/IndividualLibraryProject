package com.lib.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoanCreateRequest {

    @NotNull(message = "Please provide your userId ")
    private Long userId;

    @NotNull(message = "Please provide your bookId ")
    private Long bookId;

    @Size(max = 300)
    @NotNull(message = "Please provide your notes ")
    private String notes;
}

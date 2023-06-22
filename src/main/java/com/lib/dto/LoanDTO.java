package com.lib.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lib.domain.enums.ReservationStatus;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class LoanDTO {
    private Long id;
    private Long bookId;

    private Long userId;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy HH:mm:ss")
    private LocalDateTime loanDate=LocalDateTime.now();

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy HH:mm:ss")
    @NotNull(message="Please provide expire date of the book")
    private LocalDateTime expireDate;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy HH:mm:ss")
    @NotNull(message="Please provide return date of the book")
    private LocalDateTime returnDate;

    @Column(nullable = false)
    @Size(max = 300)
    private String notes;
}

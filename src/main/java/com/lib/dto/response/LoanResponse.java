package com.lib.dto.response;


import com.lib.domain.Book;
import com.lib.dto.BookDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoanResponse {


    public Long id;
    public Long userId;
    public BookDTO bookId;}

package com.lib.service;

import com.lib.domain.Book;
import com.lib.exception.ResourceNotFoundException;
import com.lib.exception.message.ErrorMessage;
import com.lib.mapper.BookMapper;
import com.lib.repository.BookRepository;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private final BookMapper mapper;
    private final BookRepository bookRepository;


    public BookService(BookMapper mapper, BookRepository bookRepository) {
        this.mapper = mapper;
        this.bookRepository = bookRepository;
    }

    public Book findBookById(Long bookId) {
       return bookRepository.findById(bookId).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.BOOK_NOT_FOUND_EXCEPTION,bookId)));

    }
}

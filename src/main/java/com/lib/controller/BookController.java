package com.lib.controller;

import com.lib.dto.BookDTO;
import com.lib.dto.request.BookUpdateRequest;
import com.lib.dto.request.SaveBookRequest;
import com.lib.dto.response.LibResponse;
import com.lib.dto.response.ResponseMessage;
import com.lib.exception.message.ErrorMessage;
import com.lib.service.AuthorService;
import com.lib.service.BookService;
import com.lib.service.CategoryService;
import com.lib.service.PublisherService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    private final CategoryService categoryService;

    private final PublisherService publisherService;

    private final AuthorService authorService;




    public BookController(BookService bookService, CategoryService categoryService, PublisherService publisherService, AuthorService authorService) {
        this.bookService = bookService;
        this.categoryService = categoryService;
        this.publisherService = publisherService;
        this.authorService = authorService;
    }

    @GetMapping("/visitors")
    public ResponseEntity<Page<BookDTO>> allBookByPage(
                                                       @RequestParam("page") int page ,
                                                       @RequestParam("size") int size,
                                                       @RequestParam("sort") String prop,
                                                       @RequestParam(value = "direction",
                                                                    required=false,
                                                                    defaultValue = "DESC") Sort.Direction direction) {



        Pageable pageable=PageRequest.of(page,size,Sort.by(direction,prop));

        Page<BookDTO> bookPage=bookService.getAllBookByPage(pageable);

        return ResponseEntity.ok(bookPage);

    }

    @GetMapping("/visitors/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id){

        BookDTO bookResponse=bookService.findBookById(id);
        return ResponseEntity.ok(bookResponse);
    }

    @PostMapping("/admin/{imageId}/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LibResponse> saveBook(@PathVariable String imageId,@Valid @RequestBody SaveBookRequest saveBookRequest){
        bookService.saveBook(imageId,saveBookRequest);
        LibResponse libResponse=
                new LibResponse(ResponseMessage.BOOK_CREATED_RESPONSE_MESSAGE,true);

        return new ResponseEntity<>(libResponse, HttpStatus.CREATED);
    }

    @PutMapping("/auth/upd")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LibResponse> updateBook(@RequestParam("id") Long id,
                                                  @RequestParam("imageId") String imageId,
                                                  @Valid @RequestBody BookUpdateRequest bookUpdateRequest){
        bookService.updateBook(id,imageId,bookUpdateRequest);
        LibResponse libResponse=new LibResponse(ResponseMessage.BOOK_UPDATED_RESPONSE_MESSAGE,true);
        return ResponseEntity.ok(libResponse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LibResponse> deleteBook(@PathVariable Long id){
        bookService.deleteBook(id);
        LibResponse libResponse=new LibResponse(ResponseMessage.BOOK_DELETED_RESPONSE_MESSAGE,true);
        return ResponseEntity.ok(libResponse);
    }





}

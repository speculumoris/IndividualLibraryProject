package com.lib.controller;

import com.lib.domain.Book;
import com.lib.domain.User;
import com.lib.dto.LoanDTO;
import com.lib.dto.request.LoanCreateRequest;
import com.lib.dto.response.LoanResponse;
import com.lib.service.BookService;
import com.lib.service.LoanService;
import com.lib.service.UserService;
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
@RequestMapping("/loans")
public class LoanController {

    private final LoanService loanService;
    private final UserService userService;
    private final BookService bookService;




    public LoanController(LoanService loanService, UserService userService, BookService bookService) {
        this.loanService = loanService;
        this.userService = userService;
        this.bookService = bookService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public ResponseEntity<LoanResponse> createLoan(@Valid @RequestBody LoanCreateRequest loanCreateRequest){

        LoanResponse loanResponse=loanService.saveLoan(loanCreateRequest);



        return new ResponseEntity<>(loanResponse, HttpStatus.CREATED);
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE') or hasRole('MEMBER')")
    public ResponseEntity<Page<LoanDTO>> getAllLoanByUserId(@RequestParam("userId") Long userId,
                                                            @RequestParam("page") int page,
                                                            @RequestParam("size") int size,
                                                            @RequestParam("sort") String prop,
                                                            @RequestParam(value = "direction",
                                                                    required=false,
                                                                    defaultValue = "DESC") Sort.Direction direction) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));
        User user = userService.getById(userId);

        Page<LoanDTO> loanDTOS = loanService.getAllLoanPageByUser(user, pageable);


        return ResponseEntity.ok(loanDTOS);

    }
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<LoanDTO> loanById(@PathVariable Long id){
        LoanDTO loanDTO=loanService.getLoanById(id);

        return ResponseEntity.ok(loanDTO);
    }

    @GetMapping("/page")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public ResponseEntity<Page<LoanDTO>> getAllLoan(@RequestParam("userId") Long userId,
                                                    @RequestParam("page") int page,
                                                    @RequestParam("size") int size,
                                                    @RequestParam("sort") String prop,
                                                    @RequestParam(value = "direction",
                                                            required=false,
                                                            defaultValue = "DESC") Sort.Direction direction) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));
        Page<LoanDTO> loanDTOS = loanService.getAllLoanByPage(pageable);
        return ResponseEntity.ok(loanDTOS);
    }
    @GetMapping("/book/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE') ")
    public ResponseEntity<Page<LoanDTO>> getAllLoanByBookId(@RequestParam("bookId") Long bookId,
                                                            @RequestParam("page") int page,
                                                            @RequestParam("size") int size,
                                                            @RequestParam("sort") String prop,
                                                            @RequestParam(value = "direction",
                                                                    required=false,
                                                                    defaultValue = "DESC") Sort.Direction direction) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));
        Book book=bookService.getBook(bookId);

        Page<LoanDTO> loanDTOS = loanService.getAllLoanPageByBook(book, pageable);


        return ResponseEntity.ok(loanDTOS);

    }
}

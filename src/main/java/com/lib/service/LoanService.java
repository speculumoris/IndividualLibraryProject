package com.lib.service;

import com.lib.domain.Book;
import com.lib.domain.Loan;
import com.lib.domain.User;
import com.lib.dto.LoanDTO;
import com.lib.dto.request.LoanCreateRequest;
import com.lib.dto.response.LoanResponse;
import com.lib.exception.BadRequestException;
import com.lib.exception.ResourceNotFoundException;
import com.lib.exception.message.ErrorMessage;
import com.lib.mapper.BookMapper;
import com.lib.mapper.LoanMapper;
import com.lib.repository.LoanRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final UserService userService;
    private final BookMapper bookMapper;
    private final BookService bookService;

    private final LoanMapper loanMapper;

    public LoanService(LoanRepository loanRepository, UserService userService, BookMapper bookMapper, BookService bookService, LoanMapper loanMapper) {
        this.loanRepository = loanRepository;
        this.userService = userService;
        this.bookMapper = bookMapper;
        this.bookService = bookService;
        this.loanMapper = loanMapper;
    }

    public LoanResponse saveLoan(LoanCreateRequest loanCreateRequest) {
        User user=userService.getCurrentUser();
        Book book=bookService.getBook(loanCreateRequest.getBookId());

        LocalDateTime now=LocalDateTime.now();

        List<Loan> loanExpireds=loanRepository.findLoanExpiredByUser(loanCreateRequest.getUserId(),now);
        checkBookIsAvailableAndLoanable(book);

        if (loanExpireds.size()>0){
            throw new IllegalStateException(ErrorMessage.BOOK_IS_NOT_HAVE_PERMISSON);
        }
        List<Loan> activeLoansOfUser = loanRepository.findLoansByUserIdAndExpireDateIsNull(user.getId());

        Loan loan =new Loan();
        switch (user.getScore()) {
            case -2:
                if (activeLoansOfUser.size() < 1) {
                    loan.setLoanDate(now);
                    loan.setExpireDate(now.plusDays(3));
                }
                break;
            case -1:
                if (activeLoansOfUser.size() < 2)
                    loan.setLoanDate(now);
                loan.setExpireDate(now.plusDays(6));
                break;
            case 0:
                if (activeLoansOfUser.size() < 3) {
                    loan.setLoanDate(now);
                    loan.setExpireDate(now.plusDays(10));
                }
                break;
            case 1:
                if (activeLoansOfUser.size() < 4) {
                    loan.setLoanDate(now);
                    loan.setExpireDate(now.plusDays(15));
                }
                break;
            case 2:

                loan.setLoanDate(now);
                loan.setExpireDate(now.plusDays(20));
                break;
            default:
                throw new BadRequestException(ErrorMessage.SCORE_IS_NOT_ENOUGH);

        }
        loan.setUser(user);
        loan.setBook(book);
        loan.setNotes(loanCreateRequest.getNotes());
        loanRepository.save(loan);
        book.setLoanable(false);
        bookService.save(book);
        LoanResponse loanResponse=new LoanResponse();
        loanResponse.setId(loan.getId());
        loanResponse.setUserId(user.getId());
        loanResponse.setBookId(bookMapper.bookToBookDTO(book));

        return loanResponse;
    }
    private boolean checkBookIsAvailableAndLoanable(Book book) {
        if (!book.isActive()) {
            throw new BadRequestException(ErrorMessage.BOOK_IS_NOT_AVAILABLE_MESSAGE);
        }
        if (!book.isLoanable()) {
            throw new BadRequestException(ErrorMessage.BOOK_IS_NOT_LOANABLE_MESSAGE);
        }
        return true;
    }

    public Page<LoanDTO> getAllLoanPageByUser(User user, Pageable pageable) {

        Page<Loan> loans=loanRepository.findAllLoansByUserAndPage(user,pageable);
        return loans.map(loan -> loanMapper.loanToLoanDTO(loan));
    }

    public LoanDTO getLoanById(Long id) {
        Loan loan=loanRepository.findById(id).orElseThrow
                (()-> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_EXCEPTION,id)));
        return loanMapper.loanToLoanDTO(loan);
    }


    public Page<LoanDTO> getAllLoanByPage(Pageable pageable) {
        Page<Loan> loans=loanRepository.findAll(pageable);
        return loans.map(loan -> loanMapper.loanToLoanDTO(loan));
    }

    public Page<LoanDTO> getAllLoanPageByBook(Book book, Pageable pageable) {
        Page<Loan> loans=loanRepository.findAllLoansByBookPage(book,pageable);
        return loans.map(loan -> loanMapper.loanToLoanDTO(loan));
    }
}

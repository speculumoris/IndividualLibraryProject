package com.lib.service;

import com.lib.domain.Book;
import com.lib.domain.Loan;
import com.lib.domain.User;
import com.lib.dto.LoanDTO;
import com.lib.dto.request.LoanCreateRequest;
import com.lib.dto.request.UpdateLoanRequest;
import com.lib.dto.response.LoanResponse;
import com.lib.dto.response.LoanUpdateResponse;
import com.lib.exception.BadRequestException;
import com.lib.exception.ResourceNotFoundException;
import com.lib.exception.message.ErrorMessage;
import com.lib.mapper.BookMapper;
import com.lib.mapper.LoanMapper;
import com.lib.repository.LoanRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

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

        Page<Loan> loans=loanRepository.findAllLoansByUser(user,pageable);
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
        Page<Loan> loans=loanRepository.findAllLoansByBook(book,pageable);
        return loans.map(loan -> loanMapper.loanToLoanDTO(loan));
    }


    public LoanUpdateResponse updateLoan(Long loanId, UpdateLoanRequest updateLoanRequest) {

        Loan loan = loanRepository.findById(loanId).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.LOAN_NOT_FOUND_EXCEPTION, loanId)));

        User user = loan.getUser();
        Book book = loan.getBook();

        try {
            if(updateLoanRequest.getReturnDate()!=null){
                book.setLoanable(true);
                loan.setReturnDate(updateLoanRequest.getReturnDate());
                bookService.save(book);
                loanRepository.save(loan);
                if(updateLoanRequest.getReturnDate().isEqual(loan.getReturnDate()) || updateLoanRequest.getReturnDate().isBefore(loan.getReturnDate())){
                    user.setScore(user.getScore()+1);
                    userService.save(user);
                    return new LoanUpdateResponse(loan);
                }else{
                    user.setScore(user.getScore()-1);
                    userService.save(user);
                    return new LoanUpdateResponse(loan);
                }
            }else{
                loan.setExpireDate(updateLoanRequest.getExpireDate());
                loan.setNotes(updateLoanRequest.getNotes());
                bookService.save(book);
                userService.save(user);
                loanRepository.save(loan);
                return new LoanUpdateResponse(loan);
            }
        } catch (RuntimeException e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } finally {
            return new LoanUpdateResponse(loan);
        }
    }

    private Loan getLoan(Long loanId) {
        Loan loan=loanRepository.findLoanById(loanId).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_EXCEPTION,loanId)));
        return loan;
    }

    public List<Loan> findByBookId(Long id) {
        List<Loan> loans =  loanRepository.findByBookId(id);
        return loans;
    }
}

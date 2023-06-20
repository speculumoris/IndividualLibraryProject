package com.lib.service;

import com.lib.domain.*;
import com.lib.dto.BookDTO;
import com.lib.dto.request.SaveBookRequest;
import com.lib.exception.ConflictException;
import com.lib.exception.ResourceNotFoundException;
import com.lib.exception.message.ErrorMessage;
import com.lib.mapper.BookMapper;
import com.lib.repository.BookRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
public class BookService {

    private final BookMapper bookMapper;
    private final BookRepository bookRepository;

    private final ImageFileService imageFileService;

    private final AuthorService authorService;

    private final CategoryService categoryService;

    private final PublisherService publisherService;


    public BookService(BookMapper mapper, BookRepository bookRepository, ImageFileService imageFileService, AuthorService authorService,@Lazy CategoryService categoryService, @Lazy PublisherService publisherService) {
        this.bookMapper = mapper;
        this.bookRepository = bookRepository;
        this.imageFileService = imageFileService;
        this.authorService = authorService;
        this.categoryService = categoryService;
        this.publisherService = publisherService;
    }

    public BookDTO findBookById(Long bookId) {
        Book book=bookRepository.findBookById(bookId).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.BOOK_NOT_FOUND_EXCEPTION,bookId)));
        return bookMapper.bookToBookDTO(book);

    }



    public Page<BookDTO> getAllBookByPage(Pageable pageable) {
        Page<Book> bookPage=bookRepository.findAll(pageable);

      return   bookPage.map(book -> bookMapper.bookToBookDTO(book));

    }

    public void saveBook(String imageId,SaveBookRequest saveBookRequest) {

        ImageFile imageFile=imageFileService.getImageId(imageId);

        Integer usedBookCount=bookRepository.findBookCountByImageId(imageFile.getId());
        if (usedBookCount>0){
            throw new ConflictException(ErrorMessage.IMAGE_USED_MESSAGE);
        }

        boolean existByIsbn=bookRepository.existsByIsbn(saveBookRequest.getIsbn());
        if (existByIsbn){
            throw new ConflictException(ErrorMessage.ISBN_ALREADY_EXIST_MESSAGE);
        }

        Book book=bookMapper.bookRequestToBook(saveBookRequest);

        Set<ImageFile> imageFileSet=new HashSet<>();
        imageFileSet.add(imageFile);
        book.setImageFile(imageFileSet);

        Author author=authorService.getAuthorById(saveBookRequest.getAuthorId());
        Publisher publisher=publisherService.getPublisher(saveBookRequest.getPublisherId());
        Category category=categoryService.findCategoryById(saveBookRequest.getCategoryId());

        book.setAuthor(author);
        book.setCategory(category);
        book.setPublisher(publisher);
        book.setName(saveBookRequest.getName());
        book.setIsbn(saveBookRequest.getIsbn());
        book.setPageCount(saveBookRequest.getPageCount());
        book.setPublishDate(saveBookRequest.getPublishDate());
        book.setShelfCode(saveBookRequest.getShelfCode());
        book.setFeatured(saveBookRequest.isFeatured());
        book.setCreateDate(LocalDateTime.now());

        bookRepository.save(book);

    }
}

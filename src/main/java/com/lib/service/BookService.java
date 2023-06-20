package com.lib.service;

import com.lib.domain.*;
import com.lib.dto.BookDTO;
import com.lib.dto.PublisherDTO;
import com.lib.dto.request.BookUpdateRequest;
import com.lib.dto.request.SaveBookRequest;
import com.lib.exception.BadRequestException;
import com.lib.exception.ConflictException;
import com.lib.exception.ResourceNotFoundException;
import com.lib.exception.message.ErrorMessage;
import com.lib.mapper.BookMapper;
import com.lib.repository.BookRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        return bookToBookDTO(book);

    }


    @Transactional
    public Page<BookDTO> getAllBookByPage(Pageable pageable) {
        Page<Book> bookPage=bookRepository.findAll(pageable);

      return   bookPage.map(book -> bookToBookDTO(book));

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
    private BookDTO bookToBookDTO(Book book){
        return BookDTO.builder()
                .id(book.getId())
                .name(book.getName())
                .isbn(book.getIsbn())
                .pageCount(book.getPageCount())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .category(book.getCategory())
                .publishDate(book.getPublishDate())
                .imageFile(convertImage(book.getImageFile()))
                .shelfCode(book.getShelfCode())
                .active(book.isActive())
                .builtIn(book.isBuiltIn())
                .loanable(book.isLoanable())
                .createDate(LocalDateTime.now())
                .featured(book.isFeatured())
                .build();
    }
    public static Set<String> convertImage(Set<ImageFile> imageFiles){

        Set<String> imgs = new HashSet<>();
        imgs = imageFiles.stream().
                map(imFile->imFile.getId().toString()).
                collect(Collectors.toSet());
        return imgs;
    }


    public void updateBook(Long id, String imageId, BookUpdateRequest bookUpdateRequest) {
        Book book=getBook(id);

        if (book.isBuiltIn()) {
            throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
        }
        ImageFile imageFile=imageFileService.getImageId(imageId);

        List<Book> books =bookRepository.findBookByImageId(imageFile.getId());
        for (Book b: books) {
            if (book.getId().longValue()!= b.getId().longValue()){
                throw  new ConflictException(ErrorMessage.IMAGE_USED_MESSAGE);
            }
        }

        boolean existByIsbn=bookRepository.existsByIsbn(bookUpdateRequest.getIsbn());
        if (existByIsbn && !bookUpdateRequest.getIsbn().equals(book.getIsbn())){
            throw new ConflictException(ErrorMessage.ISBN_ALREADY_EXIST_MESSAGE);
        }
        Author author=authorService.getAuthorById(bookUpdateRequest.getAuthorId());
        Publisher publisher=publisherService.getPublisher(bookUpdateRequest.getPublisherId());
        Category category=categoryService.findCategoryById(bookUpdateRequest.getCategoryId());


        book.setAuthor(author);
        book.setCategory(category);
        book.setPublisher(publisher);
        book.setName(bookUpdateRequest.getName());
        book.setIsbn(bookUpdateRequest.getIsbn());
        book.setPageCount(bookUpdateRequest.getPageCount());
        book.setPublishDate(bookUpdateRequest.getPublishDate());
        book.setShelfCode(bookUpdateRequest.getShelfCode());
        book.setFeatured(bookUpdateRequest.isFeatured());
        book.setActive(bookUpdateRequest.isActive());
        book.setLoanable(bookUpdateRequest.isLoanable());
        book.setBuiltIn(bookUpdateRequest.isBuiltIn());

        book.getImageFile().add(imageFile);

        bookRepository.save(book);

    }

    private Book getBook(Long id) {
        return bookRepository.findBookById(id).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.BOOK_NOT_FOUND_EXCEPTION,id)));
    }

    public void deleteBook(Long id) {
        Book book=getBook(id);

        if (book.isBuiltIn()) {
            throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
        }

        //reservation için kontrol sağlanacak //!!!

        bookRepository.delete(book);

    }
}

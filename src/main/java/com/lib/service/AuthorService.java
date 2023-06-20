package com.lib.service;

import com.lib.domain.Author;
import com.lib.domain.Publisher;
import com.lib.dto.AuthorDTO;
import com.lib.dto.request.AuthorRequest;
import com.lib.dto.request.UpdateAuthorRequest;
import com.lib.exception.BadRequestException;
import com.lib.exception.ResourceNotFoundException;
import com.lib.exception.message.ErrorMessage;
import com.lib.mapper.AuthorMapper;
import com.lib.repository.AuthorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {
    public final AuthorRepository authorRepository;

    public final AuthorMapper authorMapper;

    public AuthorService(AuthorRepository authorRepository, AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }

    public Page<AuthorDTO> getAllAuthByPage(Pageable pageable) {
        Page<Author> authors = authorRepository.findAll(pageable);

        return authors.map(author -> authorMapper.authorToAuthorDTO(author));

    }

    public AuthorDTO getAuthById(Long id) {
        Author author = authorRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(ErrorMessage.AUTHOR_NOT_FOUND_EXCEPTION, id)));
        return authorMapper.authorToAuthorDTO(author);
    }

    public void saveAuthor(AuthorRequest authorRequest) {
        Author author = new Author();
        author.setName(authorRequest.getName());


        authorRepository.save(author);
    }


    public void updateAuthor(Long id, UpdateAuthorRequest updateAuthorRequest) {
        Author author = getAuthorById(id);

        if (author.isBuiltIn()) {
            throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
        }
        author.setName(updateAuthorRequest.getName());
        author.setBuiltIn(updateAuthorRequest.isBuiltIn());
        authorRepository.save(author);

    }

    public Author getAuthorById(Long id) {

        return authorRepository.findAuthorById(id).orElseThrow
                (() -> new ResourceNotFoundException(String.format(ErrorMessage.AUTHOR_NOT_FOUND_EXCEPTION, id)));
    }

    public void deleteAuthorById(Long id) {

        Author author = getAuthorById(id);

        if (author.isBuiltIn()) {
            throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
        }
        authorRepository.delete(author);

    }

    public List<AuthorDTO> getAllAuthor() {
        List<Author> authors = authorRepository.findAll();
        return authorMapper.authorMap(authors);
    }

}

package com.lib.controller;

import com.lib.dto.AuthorDTO;
import com.lib.dto.request.AuthorRequest;
import com.lib.dto.request.UpdateAuthorRequest;
import com.lib.dto.response.LibResponse;
import com.lib.dto.response.ResponseMessage;
import com.lib.service.AuthorService;
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
@RequestMapping("/authors")
public class AuthorController {

    public final AuthorService authorService;


    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/anonymous")
    public ResponseEntity<Page<AuthorDTO>> getAllAuthByPage(@RequestParam("page") int page ,
                                                            @RequestParam("size") int size,
                                                            @RequestParam("sort") String prop,
                                                            @RequestParam(value = "direction",
                                                                    required=false,
                                                                    defaultValue = "DESC") Sort.Direction direction) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));
        Page<AuthorDTO> allAuthByPage =authorService.getAllAuthByPage(pageable);

        return ResponseEntity.ok(allAuthByPage);
    }
    @GetMapping("/anonymous/{id}")
    public ResponseEntity<AuthorDTO> getAuthorById(@PathVariable Long id){
       AuthorDTO authorDTO= authorService.getAuthById(id);
       return ResponseEntity.ok(authorDTO);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LibResponse> saveAuthor(@Valid @RequestBody AuthorRequest authorRequest){
        authorService.saveAuthor(authorRequest);

        LibResponse libResponse=new LibResponse(ResponseMessage.AUTHOR_CREATED_RESPONSE_MESSAGE,true);
        return new ResponseEntity<>(libResponse, HttpStatus.CREATED);

    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LibResponse> updateAuthor(@PathVariable Long id,
                                                    @Valid @RequestBody UpdateAuthorRequest updateAuthorRequest){

        authorService.updateAuthor(id,updateAuthorRequest);

        LibResponse libResponse=new LibResponse(ResponseMessage.AUTHOR_UPDATED_RESPONSE_MESSAGE,true);

        return ResponseEntity.ok(libResponse);


    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LibResponse> deleteAuthor(@PathVariable Long id){
        authorService.deleteAuthorById(id);

        LibResponse libResponse=new LibResponse(ResponseMessage.AUTHOR_DELETED_RESPONSE_MESSAGE,true);

        return ResponseEntity.ok(libResponse);    }





}

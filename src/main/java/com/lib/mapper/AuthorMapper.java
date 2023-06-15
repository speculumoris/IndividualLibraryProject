package com.lib.mapper;

import com.lib.domain.Author;
import com.lib.dto.AuthorDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuthorMapper {


    AuthorDTO authorToAuthorDTO(Author author);

    List<AuthorDTO> authorMap(List<Author> authors);



}

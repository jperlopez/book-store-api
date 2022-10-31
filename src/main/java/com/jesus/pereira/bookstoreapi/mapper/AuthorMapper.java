package com.jesus.pereira.bookstoreapi.mapper;

import com.jesus.pereira.bookstoreapi.domain.Author;
import com.jesus.pereira.bookstoreapi.resource.dto.AuthorDTO;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring"
)
public interface AuthorMapper {

    AuthorDTO toAuthorDto(Author author);

    Author toAuthorDto(AuthorDTO authorDTO);
}

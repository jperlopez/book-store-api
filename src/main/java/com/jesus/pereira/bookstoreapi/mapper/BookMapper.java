package com.jesus.pereira.bookstoreapi.mapper;

import com.jesus.pereira.bookstoreapi.domain.Book;
import com.jesus.pereira.bookstoreapi.resource.dto.BookDTO;
import org.mapstruct.Mapper;


@Mapper(
        componentModel = "spring"
)
public interface BookMapper {
    Book toBook(BookDTO bookDTO);

    BookDTO toBookDto(Book book);

    Book toBookUpdate(BookDTO bookDTO, Long id);

}

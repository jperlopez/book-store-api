package com.jesus.pereira.bookstoreapi.resource.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.*;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDTO {

    @JsonIgnore
    private Long id;

    @Size(min = 1, max = 45)
    @NotNull
    private String name;

    @Size(min = 1, max = 100)
    private String description;

    @Digits(integer = 5, fraction = 2)
    @NotNull
    private BigDecimal prize;

    @Digits(integer = 4, fraction = 0)
    private int pages;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long authorId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long categoryId;

    private AuthorDTO authorDTO;

    private CategoryDTO categoryDTO;
}

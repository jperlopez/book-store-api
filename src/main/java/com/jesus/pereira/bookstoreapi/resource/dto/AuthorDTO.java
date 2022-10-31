package com.jesus.pereira.bookstoreapi.resource.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.*;

import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorDTO {

    @JsonIgnore
    private Long id;

    @Size(min = 1, max = 45)
    @NotNull
    private String name;

    @Size(min = 1, max = 45)
    private String surname;
}

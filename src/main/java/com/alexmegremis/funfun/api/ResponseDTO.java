package com.alexmegremis.funfun.api;

import com.alexmegremis.funfun.persistence.PersonEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO {
    private String       message;
    private PersonEntity person;
}
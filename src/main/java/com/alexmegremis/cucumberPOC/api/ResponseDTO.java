package com.alexmegremis.cucumberPOC.api;

import com.alexmegremis.cucumberPOC.persistence.PersonEntity;
import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO {
    private String       message;
    private PersonEntity person;
}

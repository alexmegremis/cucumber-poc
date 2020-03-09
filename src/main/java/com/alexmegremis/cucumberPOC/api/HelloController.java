package com.alexmegremis.cucumberPOC.api;

import com.alexmegremis.cucumberPOC.persistence.application.PersonEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping (value = "/api/v1")
public class HelloController {

    @GetMapping (value = "hello", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus (code = HttpStatus.OK)
    public ResponseEntity<ResponseDTO> hello(@RequestParam (name = "name", required = false) final String name) {
        log.info(">>> hello called");
        return ResponseEntity.of(Optional.ofNullable(ResponseDTO.builder().message("Hello " + (StringUtils.isEmpty(name) ? "world" : name)).build()));
    }

    @GetMapping (value = "helloYou", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus (code = HttpStatus.OK)
    public ResponseEntity<ResponseDTO> helloYou(@RequestParam (name = "nameFirst") final String nameFirst,
                                                @RequestParam (name = "nameLast", required = false) final String nameLast) {
        log.info(">>> helloYou called");
        final PersonEntity person = PersonEntity.builder().nameFirst(nameFirst + "-io").nameLast(nameLast).id(0).build();
        return ResponseEntity.of(Optional.ofNullable(ResponseDTO.builder().message("Hello " + nameFirst).person(person).build()));
    }
}
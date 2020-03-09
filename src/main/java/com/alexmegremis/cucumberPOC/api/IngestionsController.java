package com.alexmegremis.cucumberPOC.api;

import com.alexmegremis.cucumberPOC.persistence.PersonEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping (value = "/api/v1")
public class IngestionsController {

    @GetMapping (value = "ingestions/hr/do", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus (code = HttpStatus.OK)
    public ResponseEntity<String> ingestHR(@RequestParam (name = "filename", required = false) final String name) {
        log.info(">>> HR ingestion called");
        return ResponseEntity.of(Optional.ofNullable(name));
    }
}
package com.alexmegremis.cucumberPOC.api;

import com.alexmegremis.cucumberPOC.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping (value = "/api/v1/person/spring")
public class PersonSpringController {

    @Autowired
    private PersonRepository personRepository;

    @GetMapping (value = "find", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus (code = HttpStatus.OK)
    public ResponseEntity<Set<PersonEntity>> find(@RequestParam (name = "id", required = false) final Integer id,
                                                  @RequestParam (name = "nameFirst", required = false) final String nameFirst,
                                                  @RequestParam (name = "nameLast", required = false) final String nameLast,
                                                  @RequestParam (name = "applicationName", required = false) final String applicationName) {

        ApplicationEntity      exampleApplication = ApplicationEntity.builder().name(applicationName).build();
        RepoEntity             exampleRepo        = RepoEntity.builder().application(exampleApplication).build();
        MapPrincipalRepoEntity exampleRepoMapping = MapPrincipalRepoEntity.builder().repo(exampleRepo).build();
        PrincipalEntity        examplePrincipal   = PrincipalEntity.builder().repoMappings(ProducingCollection.setOf(exampleRepoMapping)).build();
        PersonEntity           examplePerson      = PersonEntity.builder()
                                                                .nameFirst(nameFirst)
                                                                .nameLast(nameLast)
                                                                .id(id)
                                                                .principals(ProducingCollection.setOf(examplePrincipal))
                                                                .build();
        Example<PersonEntity>  example            = Example.of(examplePerson);
        Set<PersonEntity>      allByExample       = new HashSet(personRepository.findAll(example));

        return ResponseEntity.of(Optional.ofNullable(allByExample));
    }

    @GetMapping (value = "helloYou", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus (code = HttpStatus.OK)
    public ResponseEntity<ResponseDTO> helloYou(@RequestParam (name = "nameFirst") final String nameFirst,
                                                @RequestParam (name = "nameLast", required = false) final String nameLast) {
        final PersonEntity person = PersonEntity.builder().nameFirst(nameFirst + "-io").nameLast(nameLast).id(0).build();
        return ResponseEntity.of(Optional.ofNullable(ResponseDTO.builder().message("Hello " + nameFirst).person(person).build()));
    }
}

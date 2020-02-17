package com.alexmegremis.funfun.api;

import com.alexmegremis.funfun.persistence.*;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.*;

@RestController
@RequestMapping (value = "/api/v1/person/spring")
public class PersonSpringController {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

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
        PrincipalEntity        examplePrincipal   = PrincipalEntity.builder().repoMappings(Set.of(exampleRepoMapping)).build();
        PersonEntity           examplePerson      = PersonEntity.builder().nameFirst(nameFirst).nameLast(nameLast).id(id).principals(Set.of(examplePrincipal)).build();
        Example<PersonEntity>  example            = Example.of(examplePerson);
        Set<PersonEntity>      allByExample       = new HashSet(personRepository.findAll(example));

//        session.createCriteria(PersonEntity.class);
//
//
//        CriteriaQuery<PersonEntity> query = entityManager.getCriteriaBuilder().createQuery(PersonEntity.class);
//
//        Root<PersonEntity> from = query.from(PersonEntity.class);
//        query.where(entityManager.getCriteriaBuilder().equal())
//
//        entityManager.
//
//                Set<PersonEntity> allByNameFirst = personRepository.findAllByNameFirst(name);
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

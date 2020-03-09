package com.alexmegremis.cucumberPOC.api;

import com.alexmegremis.cucumberPOC.persistence.application.*;
import org.hibernate.*;
import org.hibernate.criterion.Example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.*;

@RestController
@RequestMapping(value = "/api/v1/person/hibernate")
public class PersonHibernateController {

    @Autowired
    private PersonRepository      personRepository;
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private RepoRepository             repoRepository;
    @Autowired
    private MapPrincipalRepoRepository mapPrincipalRepoRepository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping(value = "find", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Set<PersonEntity>> find(@RequestParam(name = "id", required = false) final Integer id,
                                                  @RequestParam(name = "nameFirst", required = false) final String nameFirst,
                                                  @RequestParam(name = "nameLast", required = false) final String nameLast,
                                                  @RequestParam(name = "applicationName", required = false) final Set<String> applicationName) {

        Set<PersonEntity> allByExample = null;
        Session           session      = entityManager.unwrap(Session.class);
        CriteriaBuilder   builder      = session.getCriteriaBuilder();

//        entityManager.createQuery("SELECT DISTINCT a FROM APPLICATION a JOIN FETCH a.repos r WHERE a.identifier IN ", ApplicationEntity.class).getResultList();

        CriteriaQuery<ApplicationEntity> appCQ = builder.createQuery(ApplicationEntity.class);
        Root<ApplicationEntity>          root  = appCQ.from(ApplicationEntity.class);
//        CriteriaBuilder.In<ApplicationEntity> in
//        appCQ.select(root).where(builder.in);

//        Set<ApplicationEntity>      applications = applicationRepository.findAllByIdentifierIn(applicationName);
//        Set<RepoEntity>             repos        = repoRepository.findAllByApplicationIn(applications);
//        Set<MapPrincipalRepoEntity> repoMappings = mapPrincipalRepoRepository.findAllByRepoInAndPrincipalIn(repos);

        ApplicationEntity exampleApplication = ApplicationEntity.builder().name(applicationName.iterator().next()).build();
        RepoEntity        exampleRepo        = RepoEntity.builder().application(exampleApplication).build();
        exampleApplication.setRepos(ProducingCollection.setOf(exampleRepo));
        MapPrincipalRepoEntity exampleRepoMapping = MapPrincipalRepoEntity.builder().repo(exampleRepo).build();
        PrincipalEntity        examplePrincipal   = PrincipalEntity.builder().repoMappings(ProducingCollection.setOf(exampleRepoMapping)).build();
        PersonEntity           examplePerson      = PersonEntity.builder()
                                                                .nameFirst(nameFirst)
                                                                .nameLast(nameLast)
                                                                .id(id)
                                                                .principals(ProducingCollection.setOf(examplePrincipal))
                                                                .build();

        Example example = Example.create(examplePerson);

        Criteria criteria = session.createCriteria(PersonEntity.class);
        criteria.add(example);
        allByExample = new HashSet(criteria.list());

//        session.close();
        allByExample.stream().forEach(Hibernate :: unproxy);
        allByExample.stream().forEach(person -> {
            Hibernate.unproxy(person.getPrincipals());
            person.getPrincipals().stream().forEach(Hibernate :: unproxy);
            person.getPrincipals().stream().forEach(principal -> {
                Hibernate.unproxy(principal.getRepoMappings());
                principal.getRepoMappings().stream().forEach(Hibernate :: unproxy);
                principal.getRepoMappings().stream().forEach(repoMapping -> {
                    Hibernate.unproxy(repoMapping.getRepo());
                    Hibernate.unproxy(repoMapping.getRepo().getApplication());
                    Hibernate.unproxy(repoMapping.getRepo().getApplication().getRepos());
                });
            });
        });

        // Create CriteriaQuery
//        CriteriaQuery<PersonEntity> criteria = builder.createQuery(PersonEntity.class);
//        criteria.where(example);
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
}

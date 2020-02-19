package com.alexmegremis.funfun.api;

import com.alexmegremis.funfun.persistence.*;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.persistence.*;
import javax.persistence.criteria.*;
import javax.persistence.criteria.CriteriaBuilder.In;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/person/graphed")
public class PersonGraphedController {

    @Autowired
    private PersonRepository           personRepository;
    @Autowired
    private ApplicationRepository      applicationRepository;
    @Autowired
    private RepoRepository             repoRepository;
    @Autowired
    private MapPrincipalRepoRepository mapPrincipalRepoRepository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @PersistenceContext
    private EntityManager em;

    @GetMapping(value = "find", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Set<PersonEntity>> find(@RequestParam(name = "id", required = false) final Integer id,
                                                  @RequestParam(name = "nameFirst", required = false) final String nameFirst,
                                                  @RequestParam(name = "nameLast", required = false) final String nameLast,
                                                  @RequestParam(name = "applicationIdentifier", required = false) final Set<String> applicationIdentifier) {

        CriteriaBuilder                  cb             = em.getCriteriaBuilder();
        CriteriaQuery<ApplicationEntity> cqApplicationn = cb.createQuery(ApplicationEntity.class);

        EntityGraph<ApplicationEntity> egApplication = em.createEntityGraph(ApplicationEntity.class);
        egApplication.addAttributeNodes("repos");

        Root<ApplicationEntity> rootApplication               = cqApplicationn.from(ApplicationEntity.class);
        In<Object>              inClauseApplicationIdentifier = cb.in(rootApplication.get("identifier"));

        applicationIdentifier.forEach(inClauseApplicationIdentifier :: value);

        cqApplicationn.select(rootApplication).where(inClauseApplicationIdentifier);

        TypedQuery<ApplicationEntity> tqApplication = em.createQuery(cqApplicationn);
        tqApplication.setHint("javax.persistence.fetchgraph", egApplication);

        final List<ApplicationEntity> resultList = tqApplication.getResultList();

        Map<String, Set<String>> params = new HashMap<>();
        params.put("identifier", applicationIdentifier);
        final List<ApplicationEntity> applications = graphedFindDistinctWithValuesIn(ApplicationEntity.class, ProducingCollection.listOf("repos"), params);

        params = new HashMap<>();
        params.put("nameFirst", ProducingCollection.setOf(nameFirst));
        params.put("nameLast", ProducingCollection.setOf(nameLast));
        final List<PersonEntity> persons = graphedFindDistinctWithValuesIn(PersonEntity.class, null, params);

        params = new HashMap<>();
        params.put("idApplication", new HashSet<String>(applications.stream().map(ApplicationEntity :: getId).map(String::valueOf).collect(Collectors.toList())));
        final List<RepoEntity> repos = graphedFindDistinctWithValuesIn(RepoEntity.class, null, params);

        Set<PersonEntity> allByExample = null;

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

    public <T> List<T> graphedFindDistinctWithValuesIn(Class<T> clazz, final List<String> attributeNodes, final Map<String, Set<String>> inValues) {
        CriteriaBuilder  cb     = em.getCriteriaBuilder();
        CriteriaQuery<T> cq     = cb.createQuery(clazz);
        Root<T>          root   = cq.from(clazz);
        TypedQuery<T>    tq     = em.createQuery(cq);
        EntityGraph<T>   eg     = em.createEntityGraph(clazz);
        CriteriaQuery<T> select = cq.select(root);//.distinct(true);

        if (!CollectionUtils.isEmpty(attributeNodes)) {
            attributeNodes.forEach(eg :: addAttributeNodes);
        }

        if (!CollectionUtils.isEmpty(inValues)) {
            inValues.entrySet().forEach(e -> {
                In<Object> inClause = cb.in(root.get(e.getKey()));
                e.getValue().forEach(inClause :: value);
                select.where(inClause);
            });
        }

        tq.setHint("javax.persistence.fetchgraph", eg);

        final List<T> proxiedResult = tq.getResultList();
        final List<T> result        = (List<T>) proxiedResult.stream().map(Hibernate :: unproxy).collect(Collectors.toList());
        return proxiedResult;
    }
}

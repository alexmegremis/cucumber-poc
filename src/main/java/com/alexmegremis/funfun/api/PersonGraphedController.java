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
@RequestMapping (value = "/api/v1/person/graphed")
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

    @GetMapping (value = "find", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus (code = HttpStatus.OK)
    public ResponseEntity<Set<PersonEntity>> find(@RequestParam (name = "id", required = false) final Integer id,
                                                  @RequestParam (name = "nameFirst", required = false) final String nameFirst,
                                                  @RequestParam (name = "nameLast", required = false) final String nameLast,
                                                  @RequestParam (name = "applicationIdentifier", required = false) final Set applicationIdentifier) {

        Map<String, Set<Object>> params = new HashMap<>();
        params.put("identifier", applicationIdentifier);
        final List<ApplicationEntity> applications = graphedFindDistinctWithValuesIn(ApplicationEntity.class, ProducingCollection.listOf("repos"), params);

        params = new HashMap<>();
        params.put("nameFirst", ProducingCollection.setOf(nameFirst));
        params.put("nameLast", ProducingCollection.setOf(nameLast));
        final List<PersonEntity> persons = graphedFindDistinctWithValuesIn(PersonEntity.class, null, params);

        params = new HashMap<>();
        params.put("idApplication", new HashSet(applications.stream().map(ApplicationEntity :: getId).collect(Collectors.toList())));
        final List<RepoEntity> repos = graphedFindDistinctWithValuesIn(RepoEntity.class, null, params);

        Set<PersonEntity> allByExample = null;

        return ResponseEntity.of(Optional.ofNullable(allByExample));
    }

    public <T> List<T> graphedFindDistinctWithValuesIn(Class<T> clazz, final List<String> attributeNodes, final Map<String, Set<Object>> inValues) {
        CriteriaBuilder  cb     = em.getCriteriaBuilder();
        CriteriaQuery<T> cq     = cb.createQuery(clazz);
        Root<T>          root   = cq.from(clazz);
        EntityGraph<T>   eg     = em.createEntityGraph(clazz);
        CriteriaQuery<T> select = cq.select(root);//.distinct(true);

        if (! CollectionUtils.isEmpty(attributeNodes)) {
            attributeNodes.forEach(eg :: addAttributeNodes);
        }

        if (! CollectionUtils.isEmpty(inValues)) {

            List<In<Object>> inClauses = new ArrayList<>();

            inValues.entrySet().forEach(e -> {
                In<Object> inClause = cb.in(root.get(e.getKey()));
                e.getValue().forEach(inClause :: value);
                inClauses.add(inClause);
            });

            select.where(inClauses.toArray(new In[inClauses.size()]));
        }

        // This must be done right before the actual query call
        TypedQuery<T> tq = em.createQuery(cq);
        tq.setHint("javax.persistence.fetchgraph", eg);

        final List<T> proxiedResult = tq.getResultList();
        final List<T> result        = (List<T>) proxiedResult.stream().map(Hibernate :: unproxy).collect(Collectors.toList());
        return result;
    }
}

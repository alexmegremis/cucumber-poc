package com.alexmegremis.cucumberPOC.api;

import com.alexmegremis.cucumberPOC.persistence.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping (value = "/api/v1/person")
public class SeedController {

    @Autowired
    private PersonRepository           personRepository;
    @Autowired
    private PrincipalRepository        principalRepository;
    @Autowired
    private MapPrincipalRepoRepository mapPrincipalRepoRepository;

    final int target         = 100000;
    final int capacityTarget = 150000;

    List<PersonEntity>           personEntities    = Collections.synchronizedList(new ArrayList<>(capacityTarget));
    List<PrincipalEntity>        principalEntities = Collections.synchronizedList(new ArrayList<>(capacityTarget));
    List<MapPrincipalRepoEntity> mappings          = Collections.synchronizedList(new ArrayList<>(capacityTarget * 3));

    final static EnumSet<BitwiseTest.PERMISSIONS>[] permissionCombos = new EnumSet[] {BitwiseTest.P_MIXED, BitwiseTest.P_WRITE, BitwiseTest.P_READ, BitwiseTest.P_WRITE_MEDIUM, BitwiseTest.P_WRITE_SIMPLE};

    @GetMapping (value = "seed", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus (code = HttpStatus.OK)
    public String doInsert() throws InterruptedException {

        int personCounter    = 11;
        int principalCounter = 11;
        int mapCounter       = 11;

        final ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < target; i++) {
            mapCounter += 5;
            Runnable thread = createWorker(i, ++ personCounter, ++ principalCounter, mapCounter);

            executorService.submit(thread);

            if (i % 1000 == 0) {
                log.info(">>> now at {} persons", i);
            }
        }

        executorService.shutdown();
        executorService.awaitTermination(20l, TimeUnit.SECONDS);

        log.info(">>> About to save {} persons, {} principals, and {} mappings.", personEntities.size(), principalEntities.size(), mappings.size());

        final Collection<List<PersonEntity>>           personBuckets    = getCollectionBuckets(personEntities);
        final Collection<List<PrincipalEntity>>        principalBuckets = getCollectionBuckets(principalEntities);
        final Collection<List<MapPrincipalRepoEntity>> mappingsBuckets  = getCollectionBuckets(mappings);

        personBuckets.parallelStream().forEach(personRepository :: saveAll);
        log.info(">>> Saved persons.");
        principalBuckets.parallelStream().forEach(principalRepository :: saveAll);
        log.info(">>> Saved principals.");
        mappingsBuckets.parallelStream().forEach(mapPrincipalRepoRepository :: saveAll);
        log.info(">>> Saved mappings.");

        return "Saved.";
    }

    public static <T> Collection<List<T>> getCollectionBuckets(final List<T> collection) {
        final AtomicInteger       counter   = new AtomicInteger();
        final int                 chunkSize = 5000;
        final Collection<List<T>> result    = collection.stream().collect(Collectors.groupingBy(it -> counter.getAndIncrement() / chunkSize)).values();
        return result;
    }

    private String getPermissionsString(final EnumSet<BitwiseTest.PERMISSIONS> permissions) {
        StringBuilder                           sb       = new StringBuilder();
        final Iterator<BitwiseTest.PERMISSIONS> iterator = permissions.iterator();

        while (iterator.hasNext()) {
            sb.append("\"");
            sb.append(iterator.next().name());
            sb.append("\"");
            if (iterator.hasNext()) {
                sb.append(",");
            }
        }

        return sb.toString();
    }

    private Runnable createWorker(final int mark, final int personCounter, final int principalCounter, int mapCounter) {
        Runnable result = () -> {

            PersonEntity personEntity = PersonEntity.builder().nameFirst("NameFirst" + mark).nameLast("NameLast" + mark).id(personCounter).build();
            personEntities.add(personEntity);
            PrincipalEntity principalEntity = PrincipalEntity.builder().name("FirstLast" + mark).idPersonOwner(personCounter).id(principalCounter).build();
            principalEntities.add(principalEntity);

            for (int j = 0; j < 5; j++) {
                MapPrincipalRepoEntity mapPrincipalRepoEntity = MapPrincipalRepoEntity.builder()
                                                                                      .id(mapCounter + j)
                                                                                      .idPrincipal(principalEntity.getId())
                                                                                      .idRepo(j)
                                                                                      .permission(getPermissionsString(permissionCombos[j]))
                                                                                      .permissionBits(BitwiseTest.getPermission(permissionCombos[j]))
                                                                                      .build();

                mappings.add(mapPrincipalRepoEntity);
            }

            if (mark % 1000 == 0) {
                log.info(">>> Just created {}th person with trimmings.", mark);
            }
        };

        return result;
    }
}

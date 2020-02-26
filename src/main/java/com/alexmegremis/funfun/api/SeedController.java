package com.alexmegremis.funfun.api;

import com.alexmegremis.funfun.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.*;

@RestController
@RequestMapping (value = "/api/v1/person/spring")
public class SeedController {

    @Autowired
    private PersonRepository           personRepository;
    @Autowired
    private PrincipalRepository        principalRepository;
    @Autowired
    private MapPrincipalRepoRepository mapPrincipalRepoRepository;

    final int target         = 200000;
    final int capacityTarget = 300000;

    List<PersonEntity>           personEntities    = Collections.synchronizedList(new ArrayList<>(capacityTarget));
    List<PrincipalEntity>        principalEntities = Collections.synchronizedList(new ArrayList<>(capacityTarget));
    List<MapPrincipalRepoEntity> mappings          = Collections.synchronizedList(new ArrayList<>(capacityTarget * 3));

    final static EnumSet<BitwiseTest.PERMISSIONS>[] permissionCombos = new EnumSet[] {BitwiseTest.P_MIXED, BitwiseTest.P_WRITE, BitwiseTest.P_READ};

    @GetMapping (value = "insert", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus (code = HttpStatus.OK)
    public String doInsert() throws InterruptedException {

        int personCounter    = 11;
        int principalCounter = 11;
        int mapCounter       = 11;

        final ExecutorService executorService = Executors.newFixedThreadPool(4);

        for (int i = 0; i < target; i++) {
//            PersonEntity personEntity = PersonEntity.builder().nameFirst("NameFirst" + i).nameLast("NameLast" + i).id(++ personCounter).build();
//            personEntities.add(personEntity);
//            PrincipalEntity principalEntity = PrincipalEntity.builder().name("FirstLast" + i).idPersonOwner(personCounter).id(++ principalCounter).build();
//            principalEntities.add(principalEntity);
//
//            for (int j = 0; j < 3; j++) {
//                MapPrincipalRepoEntity mapPrincipalRepoEntity = MapPrincipalRepoEntity.builder()
//                                                                                      .id(++ mapCounter)
//                                                                                      .idPrincipal(principalEntity.getId())
//                                                                                      .idRepo(j)
//                                                                                      .permission(getPermissionsString(permissionCombos[j]))
//                                                                                      .permissionBits(BitwiseTest.getPermission(permissionCombos[j]))
//                                                                                      .build();
//
//                mappings.add(mapPrincipalRepoEntity);
//            }

            mapCounter += 3;
            Runnable thread = createWorker(i, ++ personCounter, ++ principalCounter, mapCounter);

            executorService.submit(thread);

            if (i % 1000 == 0) {
                System.out.println(">>> now at " + i + "persons");
//                System.out.println(">>> now at " + principalEntities.size() + "principals");
//                System.out.println(">>> now at " + mappings.size() + "mappings");
            }
        }

        executorService.shutdown();
        executorService.awaitTermination(20l, TimeUnit.SECONDS);

        System.out.println(">>> About to save " + personEntities.size() + " persons, " + principalEntities.size() + " principals, " + " and " + mappings.size() + " mappings");

        personRepository.saveAll(new ArrayList<>(personEntities));
        principalRepository.saveAll(new ArrayList<>(principalEntities));
        mapPrincipalRepoRepository.saveAll(new ArrayList<>(mappings));

        return "foo";
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
        permissions.forEach(p -> {
            sb.append("\"");
            sb.append(p.name());
            sb.append("\",");
        });

        return sb.toString();
    }

    private Runnable createWorker(final int mark, final int personCounter, final int principalCounter, int mapCounter) {
        Runnable result = () -> {

            PersonEntity personEntity = PersonEntity.builder().nameFirst("NameFirst" + mark).nameLast("NameLast" + mark).id(personCounter).build();
            personEntities.add(personEntity);
            PrincipalEntity principalEntity = PrincipalEntity.builder().name("FirstLast" + mark).idPersonOwner(personCounter).id(principalCounter).build();
            principalEntities.add(principalEntity);

            for (int j = 0; j < 3; j++) {
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
                System.out.println(">>> Just created " + mark);
            }
        };

        return result;
    }
}

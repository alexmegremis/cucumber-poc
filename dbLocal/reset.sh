java -cp ~/.m2/repository/com/h2database/h2/1.4.200/h2-1.4.200.jar org.h2.tools.RunScript -user sa -url jdbc:h2:tcp://localhost:9101/funfun01?FILE_LOCK=NO -script ../src/main/resources/schemaFull.sql
java -cp ~/.m2/repository/com/h2database/h2/1.4.200/h2-1.4.200.jar org.h2.tools.RunScript -user sa -url jdbc:h2:tcp://localhost:9101/funfun01?FILE_LOCK=NO -script ../src/main/resources/dataFull.sql

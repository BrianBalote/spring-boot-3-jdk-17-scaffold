# spring-boot-3-jdk-17-scaffold

### installing sybase jconn4
1. install DBeaver
2. create a new connection to a Sybase to download the jconn4 driver
3. jconn4 driver will be inside: "C:\Users\<username>\AppData\Roaming\DBeaverData\drivers\drivers\sybase\jconnect"
4. install jconn4 in maven using: mvn install:install-file "-Dfile=<path to jar>/jconn4.jar" "-DgroupId=com.sybase" "-DartifactId=jconn4" "-Dversion=7.0" "-Dpackaging=jar" "-DgeneratePom=true"

### run integration test
1. mvn clean install -DskipTests
2. docker compose up -d
3. run contextLoads



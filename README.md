# TryoutApp using Spring Boot with PostgresSQL and Docker
# Author: David Brosnan

## STEPS
- Define dependencies in build.gradle
- Create a Spring Boot Main @SpringBootApplication
- Create components @Entity / @RestController / @Repository
- Create application.properties
- Build
- Start Docker(springbootapp, postgresDB)
- Test app using curls 

## BUILD the application 
./gradlew build   

## Start Docker
docker-compose up --build

# Stop Docker
docker-compose down

## TEST APP USING CURLS 

### POST /user/save 
curl -s -X POST \
  http://localhost:8080/user/save \
  -H 'Content-Type: application/json' \
  -d '{"name":"YourName"}''

### GET /user/{id}
curl -s -X GET \
  http://localhost:8080/user/1 

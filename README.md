# CloudKeeper (cloud storage)

## Implementation features:


- **Application implements features described in** [Specification](CloudServiceSpecification.yaml)


- **App built with Spring Boot:**

  The application provides a RESTful interface integrated with [frontend](frontend/cloudKeeper-frontend).


- **User data is stored in database:**

  The application uses Postgres database.
  
  Database is initializes on first application run via Liquibase framework's migration mechanisms (see [initialization scripts](src/main/resources/db/changelog)).

- **Implemented authentication and other information security mechanisms (e.g. access control):**

  - [Token-Based Authentication](src/main/java/com/example/CloudKeeper/security),
  - [Spring Security](src/main/java/com/example/CloudKeeper/config/SecurityConfig.java),
  - [CORS](src/main/java/com/example/CloudKeeper/config/MvcConfig.java).


- **Maven build automation tool is used:**

  See  [pom.xml](pom.xml).


- **Application runs in container using Docker, Docker Compose.**

  See
    - [Dockerfile for the frontend](frontend/Dockerfile),
    - [Dockerfile for the backend](Dockerfile),
    - [Docker-compose](docker-compose.yml).


- **Implemented logging using [log4j](src/main/resources/log4j.properties):**

  Key operations are logged (authorization, file operations (uploading / downloading / renaming), displaying a list of user files) (INFO level), as well as errors (ERROR level),

  The log is written to the file [cloudKeeper-backend.log](log/cloudKeeper-backend.log).


- **Application code is covered with unit-tests using JUnit, Mockito:**

  Test classes are located in [src/test/java/com/example/CloudKeeper](src/test/java/com/example/CloudKeeper).


- **Implemented integration tests using Testcontainers:**

  Integration tests class - [src/test/java/com/example/CloudKeeper/container](src/test/java/com/example/CloudKeeper/container).


- **The configuration settings are in** [application.yml](src/main/resources/application.yml).

## Project setup and launch:

1. Install and run frontend following [instructions](frontend/cloudKeeper-frontend/README.md),
2. Make sure Docker is installed on your machine,
3. Build the project by running the following command in terminal:
```
mvn install
```
3. Prepare (build) docker images to launch the backend and the frontend by running the following commands in terminal:
```
docker build --file=frontend/Dockerfile  -t cloudkeeper-frontend .

docker build --file=Dockerfile  -t cloudkeeper-app .
```
3. After successfully building the images, run the docker-compose script by running the command in terminal:

```
docker-compose up
```
4. After a successful launch, the following will be running and ready for use:
- backend at http://localhost:8080,
- frontend at http://localhost:8081,
- database at http://localhost:5432.

## Usage:
- Description and rules for using the frontend can be found [here](frontend/cloudKeeper-frontend/README.md),
- It's **recommended to use preconfigured credentials** to test the functionality (could be found in DML-script [002-data.sql](src/main/resources/db/changelog/migrations/002-data.sql)):

  - **login:** *User1@mail.ru*, **password:** *P@sswd123*
  - **login:** *Elyne@gmail.com*, **password:** *@pplicAtion3313*

## Screenshots:
<b name="enter">Enter Window:</b>


![Enter window](img/EnterWindow.png)

<b name="enter">Main Window:</b>


![Enter window](img/MainWindow.png)





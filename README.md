# CloudKeeper (облачное хранилище)

## Особенности реализации:


- **Application implements features described in the** [Specification](CloudServiceSpecification.yaml)


- **App built with Spring Boot:**

  The application provides a RESTful interface integrated with the [frontend](frontend/cloudKeeper-frontend).


- **User data is stored in database:**

  The application uses Postgres database.\
  
  Database is initializes on first application run via Liquibase framework's migration mechanisms (see [initialization scripts](src/main/resources/db/changelog)).

- **Implemented authentication and other information security mechanisms (e.g. access control):**

  - [Token-Based Authentication](src/main/java/com/example/CloudKeeper/security),
  - [Spring Security](src/main/java/com/example/CloudKeeper/config/SecurityConfig.java)
  - [CORS](src/main/java/com/example/CloudKeeper/config/MvcConfig.java).


- **Maven build automation tool is used:**

  See  [pom.xml](pom.xml).


- **Application runs in container using Docker, Docker Compose.**

  См.
    - [Dockerfile для front'a](frontend/Dockerfile),
    - [Dockerfile для backend'a](Dockerfile),
    - [Docker-compose скрипт](docker-compose.yml).


- **Implemented logging using [log4j](src/main/resources/log4j.properties):**

  Key operations are logged (authorization, file operations (uploading / downloading / renaming), displaying a list of user files) and their results (INFO level), as well as errors (ERROR level),

  The log is written to a file [cloudKeeper-backend.log](log/cloudKeeper-backend.log).


- **Application code is covered with unit-tests using JUnit, Mockito:**

  Test classes are located [src/test/java/com/example/CloudKeeper](src/test/java/com/example/CloudKeeper).


- **Implemented integration tests using Testcontainers:**

  Integration tests class - [src/test/java/com/example/CloudKeeper/container](src/test/java/com/example/CloudKeeper/container).


- **The configuration settings are in ** [application.yml](src/main/resources/application.yml).

## Launch:

1. Install and run frontend, следуя [инструкции](frontend/cloudKeeper-frontend/README.md),
2. Убедитесь, что на машине установлен Docker,
3. Осуществите сборку проекта, выполнив команду в терминале:
```
mvn install
```
3. Подготовьте (соберите) образы для запуска backend'a и frontend'a, выполнив в терминале последовательно команды:
```
docker build --file=frontend/Dockerfile  -t cloudkeeper-frontend .

docker build --file=Dockerfile  -t cloudkeeper-app .
```
3. После успешной сборки образов запустите docker-compose скрипт, выполнив команду:

```
docker-compose up
```
4. После успешного запуска будут активны и готовы к использованию:
- backend по адресу http://localhost:8080,
- frontend по адресу http://localhost:8081,
- база данных по адресу http://localhost:5432.

## Использование:
- С описанием и правилами использования front'а можно ознакомиться [здесь](frontend/cloudKeeper-frontend/README.md),

- Для тестирования функционала **рекомендуется использовать преднастроенные учетные данные** (см. в DML скрипте [002-data.sql](src/main/resources/db/changelog/migrations/002-data.sql)):

  - **login:** *User1@mail.ru*, **password:** *P@sswd123*
  - **login:** *Elyne@gmail.com*, **password:** *@pplicAtion3313*

## Скриншоты:
<b name="enter">Enter Window:</b>


![Enter window](img/EnterWindow.png)

<b name="enter">Main Window:</b>


![Enter window](img/MainWindow.png)





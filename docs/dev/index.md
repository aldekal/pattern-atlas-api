# PatternAtlas Developer Guide
This document provides an index to all development guidelines and background information of the PatternAtlas.
- [ADR](/adr) - Information on Architectural decisions can be found here

## Quick Develop
### PatternPediaAuth
PatternPediaAuth is a Spring Boot Authentication Server and it's main purpose is to give PatternAtlasAPI user management capabilities.
This is achieved through using the OAuth 2.0 Authentication Code Flow, additionally new users can create accounts.
It runs on Port 8081

### Development

1. Run the maven build `mvn package -DskipTests`
2. Clone the contents repository `git clone https://github.com/PatternAtlas/pattern-atlas-content`.
3. Clone the Docker repository `git clone https://github.com/PatternAtlas/pattern-atlas-docker`
4. Setup IntelliJ to also use the contents repository in the deployment. :warning: you MUST use the _Applicaition_ run configuration, not _Spring Boot_!
  ![Run-Configuration](IntelliJ-run-config.png)
5. Start the development configuration using docker compose:
   1. Change into the docker compose repository
   2. Run the following command  `docker-compose -f docker-compose.dev.yml up -d`
6. Run the Pattern Atlas using IntelliJ
7. Go to <http://localhost:7080/> and login with user admin and password admin
8. Add a user by ONLY specifying her name
9. Afterwards, under the _Credentials_ tab, you can set a password for this user. Make sure to set the `Temporary` flag to `false`!
10. Start the UI (either using Docker or local setup)
11. Go to <http://localhost:1978> and login. The first user is automatically assigned to the ADMIN role.


#### IntelliJ
3. [Follow PatternAtlasAPI from Step 5 pls](#step5)

#### Turn authentication on/off for PatternAtlasAPI
If you don't need the capabilities of the PatternPediaAuth server during development. You can follow the instructions in the following file 
[Security Config file](https://github.com/PatternAtlas/pattern-atlas-api/blob/ba-meyer-master/src/main/java/io/github/patternatlas/api/config/ResourceServerConfig.java)
to turn those off or on. An easier way will be added in a future realease.

#### Default User
During development default users are
- Admin: name: `admin@mail` password: `pass` 
- Member: name: `member@mail` password: `pass` 

### PatternAtlasAPI 
--->
1. Clone the repository `git clone https://github.com/PatternAtlas/pattern-atlas-api.git`.
2. Build the repository `mvn package -DskipTests` (skiping the tests for a faster build), Java 8 required.
3. Clone the repository `git clone https://github.com/PatternAtlas/pattern-atlas-ui.git`.
4. Build the repository `mvn package -DskipTests` (skiping the tests for a faster build), npm is required. (plus yarn, optionally)
5. <a name="step5"></a>Continue your IDE setup:
    - [IntelliJ Ultimate](IntelliJ/)
6. Set up database:
    - Open Terminal in IntelliJ
    ![checkstyle](IntelliJ/graphics/terminal.png)
    - Navigate to directory  ``..\.docker\``
    - Insert the following commands ``docker-compose up -d``
    - Open "Database"
    ![checkstyle](IntelliJ/graphics/open-database.png)
    - Click the "+"
    - Go to "Data Source > PostgresSQL"
    - Enter username and password: "postgres" (both)
    ![checkstyle](IntelliJ/graphics/postgres-setup.png)
    - Click "Apply"
    

7. Start the application (via the runconfig that you configured in step 5)

## Main API Endpoints
API-Root:   /

Swagger-UI: http://localhost:1977/swagger-ui

HAL - Browser: On "/" -> redirects to http://localhost:1977/explorer/index.html#uri=/


## License

See the NOTICE file(s) distributed with this work for additional
information regarding copyright ownership.

This program and the accompanying materials are made available under the Apache Software License 2.0 
which is available at https://www.apache.org/licenses/LICENSE-2.0.

SPDX-License-Identifier: Apache-2.0

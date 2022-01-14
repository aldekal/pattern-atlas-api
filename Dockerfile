FROM maven:3-jdk-8 as builder

COPY . /tmp/pattern-atlas-api
WORKDIR /tmp/pattern-atlas-api
RUN mvn package -DskipTests
RUN mv target/patternatlas.api-1.2.0-SNAPSHOT.jar target/patternatlas.api-1.2.0-SNAPSHOT-no-hal-explorer.jar
RUN mvn package -DskipTests -PHAL_EXPLORER


FROM openjdk:8

ARG DOCKERIZE_VERSION=v0.6.1

ENV API_PORT 1977
ENV JDBC_DATABASE_URL localhost
ENV JDBC_DATABASE_USERNAME postgres
ENV JDBC_DATABASE_PASSWORD postgres
ENV JDBC_DATABASE_NAME postgres
ENV JDBC_DATABASE_PORT 5060
ENV HAL_EXPLORER true

RUN wget https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && tar -C /usr/local/bin -xzvf dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && rm dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz

EXPOSE ${API_PORT}
COPY --from=builder /tmp/pattern-atlas-api/target/patternatlas.api-1.2.0-SNAPSHOT.jar /var/www/java/api.jar
COPY --from=builder /tmp/pattern-atlas-api/target/patternatlas.api-1.2.0-SNAPSHOT-no-hal-explorer.jar /var/www/java/api_no_hal_explorer.jar

ADD .docker/application.properties.tpl /var/www/java/application.properties.tpl

CMD  dockerize -template /var/www/java/application.properties.tpl:/var/www/java/application.properties \
     && cd /var/www/java/ \
     && if [ $HAL_EXPLORER = "true" ]; then java -jar api.jar; else java -jar api_no_hal_explorer.jar; fi


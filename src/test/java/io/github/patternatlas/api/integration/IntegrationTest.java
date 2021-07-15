package io.github.patternatlas.api.integration;

import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

import com.vladmihalcea.hibernate.type.util.Configuration;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public abstract class IntegrationTest {

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = PostgresqlContainer.getInstance();

    public static class PostgresqlContainer extends PostgreSQLContainer<PostgresqlContainer> {

        private static final String IMAGE_VERSION = "postgres:10";

        private static PostgresqlContainer container;

        private PostgresqlContainer() {
            super(IMAGE_VERSION);
        }

        public static PostgresqlContainer getInstance() {
            if (container == null) {
                container = new PostgresqlContainer();
            }
            return container;
        }

        @Override
        public void start() {
            super.start();
            System.setProperty(Configuration.PropertyKey.PRINT_BANNER.getKey(), Boolean.FALSE.toString());
            System.setProperty("JDBC_DATABASE_URL", container.getJdbcUrl());
            System.setProperty("JDBC_DATABASE_USERNAME", container.getUsername());
            System.setProperty("JDBC_DATABASE_PASSWORD", container.getPassword());
        }

        @Override
        public void stop() {
            // do nothing, JVM handles shut down
        }
    }
}

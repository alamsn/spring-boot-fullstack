package com.alam.portofolio;

import com.github.javafaker.Faker;
import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

@Testcontainers
public abstract class AbsctractTestcontainers {

    protected static final Faker FAKER = new Faker();

    @BeforeAll
    static void beforeAll() {
        Flyway flyway = Flyway.configure().dataSource(
            posgreSQLContainer.getJdbcUrl(),
            posgreSQLContainer.getUsername(),
            posgreSQLContainer.getPassword()
        ).load();
        flyway.migrate();
        System.out.println();
    }

    @Container
    protected static final PostgreSQLContainer<?> posgreSQLContainer =
        new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("alamsn-dao-unit-test")
            .withUsername("alamsn317")
            .withPassword("alamsn317");

    @DynamicPropertySource
    public static void registerDBDataSource(@NotNull DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", posgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", posgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", posgreSQLContainer::getPassword);
    }

    private static DataSource getDataSource() {
        return DataSourceBuilder.create()
            .driverClassName(posgreSQLContainer.getDriverClassName())
            .url(posgreSQLContainer.getJdbcUrl())
            .username(posgreSQLContainer.getUsername())
            .password(posgreSQLContainer.getPassword()).build();
    }

    protected static JdbcTemplate getJDBCTemplate() {
        return new JdbcTemplate(getDataSource());
    }


}

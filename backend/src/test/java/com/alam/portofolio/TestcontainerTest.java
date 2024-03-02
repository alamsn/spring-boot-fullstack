package com.alam.portofolio;

import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public class TestcontainerTest extends AbsctractTestcontainers {

    @Test
    void postgreSQLTest() {
        assertThat(posgreSQLContainer.isCreated()).isTrue();
        assertThat(posgreSQLContainer.isRunning()).isTrue();
    }

}

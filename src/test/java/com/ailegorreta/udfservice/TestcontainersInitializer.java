/* Copyright (c) 2023, LegoSoft Soluciones, S.C.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are not permitted.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 *  TestcontainersInitializer.java
 *
 *  Developed 2023 by LegoSoftSoluciones, S.C. www.legosoft.com.mx
 */
package com.ailegorreta.udfservice;

import com.ailegorreta.commons.event.EventDTO;
import com.ailegorreta.commons.event.EventDTODeSerializer;
import com.ailegorreta.commons.event.EventDTOSerializer;
import com.ailegorreta.resourceserver.utils.HasLogger;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ser.std.StringSerializer;
import org.testcontainers.utility.DockerImageName;

import java.util.HashMap;
import java.util.Map;

import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

/**
 * This is a class to start the containers only once for all tests
 *
 * Algo it starts the container in parallel
 *
 * @project udf-service
 * @author rlh
 * @date October 2023
 */
class TestcontainersInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>, HasLogger {
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:15.1"));
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.4.3"));

    static {
        Startables.deepStart(postgres, kafka).join();
        await().until( postgres::isRunning);
        await().until( kafka::isRunning);
    }

    /**
     * Sets all environment variables without the need to create a
     * @ActiveProfiles("integration-flyway")
     *
     * @param ctx the application to configure
     */
    @Override
    public void initialize(ConfigurableApplicationContext ctx) {
        getLogger().info("Kafka test container bootstrap-servers: {}", kafka.getBootstrapServers());
        getLogger().info("Postgres datasource url: {}", postgres.getJdbcUrl());
        getLogger().info("Postgres datasource username: {}", postgres.getUsername());
        getLogger().info("Postgres datasource password: {}", postgres.getPassword());
        getLogger().info("Flyway user: {}", postgres.getUsername());
        getLogger().info("Flyway password: {}", postgres.getPassword());
    }

    /**
     * Sets all environment variables without the need to create a
     * @ActiveProfiles("integration-test")
     *
     * note: Do NOT use public void initialize(ConfigurableApplicationContext ctx) because
     *       for Redis container it does not work (i.e. it tries to connect before)
     */
    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers=", () -> kafka.getBootstrapServers());
        registry.add("spring.datasource.url", () -> postgres.getJdbcUrl());
        registry.add("spring.datasource.username", () -> postgres.getUsername());
        registry.add("spring.datasource.password", () -> postgres.getPassword());
        registry.add("spring.flyway.user", () -> postgres.getUsername());
        registry.add("spring.flyway.password", () -> postgres.getPassword());
    }

    @NotNull
    @Override
    public Logger getLogger() { return HasLogger.DefaultImpls.getLogger(this); }
}

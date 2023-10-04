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
 *  RepositoryTests.java
 *
 *  Developed 2023 by LegoSoftSoluciones, S.C. www.legosoft.com.mx
 */
package com.ailegorreta.udfservice.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.ailegorreta.udfservice.EnableTestContainers;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * This class is just for test the repositories
 *
 * @project udf-service
 * @autho: rlh
 * @date: October 2023
 */
@DataJpaTest
/* ^ This is just the case we wanted to test just the JPA Repositories and not download all context */
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
/* ^ Disables the default behavior of relying on an embedded test database since we want to use Testcontainers */
@EnableTestContainers
@ExtendWith(MockitoExtension.class)
@EmbeddedKafka(bootstrapServersProperty = "spring.kafka.bootstrap-servers")
/* ^ this is because: https://blog.mimacom.com/embeddedkafka-kafka-auto-configure-springboottest-bootstrapserversproperty/ */
@ActiveProfiles("integration-tests")
@DirtiesContext                /* will make sure this context is cleaned and reset between different tests */
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
public class RepositoryTests {

    @MockBean
    private StreamBridge streamBridge;
    @MockBean
    private JwtDecoder jwtDecoder;

    @Autowired
    private MappingRepository mappingRepository;
    @Autowired
    private MicroservicesRepository microservicesRepository;
    @Autowired
    private DatasourceRepository datasourceRepository;
    @Autowired
    private DatasourceFieldRepository datasourceFieldRepository;
    @Autowired
    private UDFRepository udfRepository;

    @Test
    void dataInitialization() {
        var microservices = microservicesRepository.findAll();

        assertThat(microservices.size()).isEqualTo(2);

        var cartera = microservicesRepository.findByNombre("cartera");
        var ingestor = microservicesRepository.findByNombre("ingestor");

        assertThat(cartera).isNotNull();
        assertThat(ingestor).isNotNull();

        /* Check datasource's initialization */
        assertThat(cartera.getDatasources().size()).isEqualTo(3);
        assertThat(ingestor.getDatasources().size()).isEqualTo(2);

        var mappings = mappingRepository.findAll();

        assertThat(mappings.size()).isEqualTo(5);

        var udfs = udfRepository.findAll();

        assertThat(udfs.size()).isEqualTo(4);
    }

    /**
     * This microservice does not do any update
     */
    @Test
    void testSomeUpdates() {
    }

}

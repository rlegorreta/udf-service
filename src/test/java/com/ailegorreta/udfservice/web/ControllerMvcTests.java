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
 *  ControllerMvcTests.kt
 *
 *  Developed 2023 by LegoSoftSoluciones, S.C. www.legosoft.com.mx
 */
package com.ailegorreta.udfservice.web;

import com.ailegorreta.udfservice.config.ResourceServerConfig;
import com.ailegorreta.udfservice.domain.*;
import com.ailegorreta.udfservice.controller.MappingController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.UriComponentsBuilder;

import javax.sql.DataSource;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.*;
import java.util.UUID;

/**
 *
 * Sometimes to test Webmvc only is difficult for all existing dependencies in the application. For example
 * udf-service has many dependencies with JPA specially in the DataConfig class.
 *
 * @WebMvcTest
 *
 * note: to use this slice but the DataConfig class, @Configuration annotation must be commented to avoid JDBC
 *       dependencies, otherwise load the complete context with...
 *
 * @SpringBootTest annotation to load all context.
 *
 * @project: udf-service
 * @author: rlh
 * @date: Icitber 2023
 */
@WebMvcTest(MappingController.class)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
/* ^ Disables the default behavior of relying on an embedded test database since we want to use Testcontainers */
//@EnableJpaRepositories(basePackages = "com.ailegorreta.udfservice.domain")
//@EntityScan(basePackages = "com.ailegorreta.udfservice.domain")
//@EnableTestContainers
@ExtendWith(MockitoExtension.class)
@Import({ResourceServerConfig.class, MappingController.class})
@ActiveProfiles("integration-tests-mvc")            // This is to permit duplicate singleton beans
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
class ControllerMvcTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private JwtDecoder jwtDecoder;
    /* ^ Mocks the JwtDecoder so that the application does not try to call Spring Security Server and get the public
         keys for decoding the Access Token  */
    @MockBean
    private StreamBridge streamBridge;

    /* MockBeans for all repositories & JPA classes */
    @MockBean
    private MappingRepository mappingRepository;
    @MockBean
    private MicroservicesRepository microservicesRepository;
    @MockBean
    private DatasourceRepository datasourceRepository;
    @MockBean
    private DatasourceFieldRepository datasourceFieldRepository;
    @MockBean
    private UDFRepository  udfRepository;
    @MockBean
    private UDFFieldRepository udfFieldRepository;
    @MockBean
    DataSource dataSource;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Test how to read all System dates using a normal REST controller
     */
    @Test
    void whenGetAllSystemRatesAndAuthenticatedTheShouldReturn200() throws Exception{
        when(mappingRepository.obtenerMapping("Posicion", "cartera", "Calif_Actinver")).thenReturn(
                List.of(new Mapping(UUID.randomUUID(), "MovPosicion", "cartera", null,
                        "(emisora, emisora)", "", "")));

        var uri = UriComponentsBuilder.fromUriString("/udf/mapping");
        var res = mockMvc.perform(MockMvcRequestBuilders.get(uri.queryParam("udf", "Posicion")
                                                                .queryParam("microservicio", "cartera")
                                                                .queryParam("datasource", "Calif_Actinver")
                                                            .build().toUri())
                         .with(jwt().authorities(Arrays.asList(new SimpleGrantedAuthority("SCOPE_cartera.read"),
                                                               new SimpleGrantedAuthority("ROLE_ADMINLEGO"))))
                         .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print());

        res.andExpect(status().isOk());

    }

}

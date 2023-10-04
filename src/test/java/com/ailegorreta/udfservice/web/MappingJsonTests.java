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
 *  MappingJsonTests.java
 *
 *  Developed 2023 by LegoSoftSoluciones, S.C. www.legosoft.com.mx
 */
package com.ailegorreta.udfservice.web;

import com.ailegorreta.udfservice.domain.Mapping;
import com.ailegorreta.udfservice.domain.UDF;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Example for a JsonTest for some Postgres DTOs, many others can be added
 *
 * @project param-service
 * @author rlh
 * @date August 2023
 */
@JsonTest
@ContextConfiguration(classes = com.ailegorreta.udfservice.web.MappingJsonTests.class)
@ActiveProfiles("integration-tests")
public class MappingJsonTests {
    @Autowired
    public JacksonTester<Mapping> json;

    @Test
    void testSerialize() throws Exception {
        var mapping = new Mapping(UUID.randomUUID(), "Posicion", "cartera", null,
                            "(importeLimpio, precioLimpioVector), (importeSucio, precioSucioVector)", "", "");
        var jsonContent = json.write(mapping);

        assertThat(jsonContent).extractingJsonPathStringValue("@.id")
                               .isEqualTo(mapping.getId().toString());
        assertThat(jsonContent).extractingJsonPathStringValue("@.datasource")
                               .isEqualTo(mapping.getDatasource());
    }

    @Test
    void testDeserialize() throws Exception {
        var udf  = new UDF(UUID.randomUUID(), "Resta_importes", LocalDate.now(), LocalDate.now(), "START",
                true, "", "", new ArrayList<>(), new ArrayList<>());
        var mapping = new Mapping(UUID.randomUUID(), "Posicion", "cartera", udf,
                "(importeLimpio, precioLimpioVector), (importeSucio, precioSucioVector)", "", "");
        var content = """
                {
                    "id": 
                    """ + "\"" + mapping.getId() + "\"," + """
                    "datasource": "Posicion",
                    "microservicio": "cartera",
                    "udf": {
                        "id":
                        """ + "\"" + udf.getId() + "\"," + """
                        "nombre" : "Resta_importes",
                        "fechaCreacion":
                         """ + "\"" + udf.getFechaCreacion() + "\"," + """
                        "ultimaModificacion":
                         """ + "\"" + udf.getUltimaModificacion() + "\"," + """
                        "autor": "START",
                        "activa": "true",
                        "json": "",
                        "blockly": "",
                        "fields": [],
                        "mappings": []
                        },
                    "mapping": "(importeLimpio, precioLimpioVector), (importeSucio, precioSucioVector)",
                    "code": "",
                    "code": "",
                    "blockly": ""
                }
                """;
        assertThat(json.parse(content))
                       .usingRecursiveComparison()
                       .isEqualTo(mapping);
    }
}

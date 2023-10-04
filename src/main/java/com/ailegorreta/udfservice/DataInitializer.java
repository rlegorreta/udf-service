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
 *  DataInitializer.kt
 *
 *  Developed 2023 by LegoSoftSoluciones, S.C. www.legosoft.com.mx
 */
package com.ailegorreta.udfservice;

import com.ailegorreta.commons.utils.HasLogger;
import com.ailegorreta.udfservice.domain.*;
import com.ailegorreta.udfservice.service.EventService;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Initialize default dates and some rates form demo purpose
 *
 * @project udf-service
 * @autho rlh
 * @date October 2023
 */
@Component
class DataInitializer implements ApplicationRunner, HasLogger {

    final MicroservicesRepository microservicesRepository;
    final DatasourceRepository datasourceRepository;
    final DatasourceFieldRepository datasourceFieldRepository;
    final UDFRepository udfRepository;
    final UDFFieldRepository udfFieldRepository;
    final MappingRepository mappingRepository;
    final EventService eventService;

    public DataInitializer(MicroservicesRepository microservicesRepository,
                           DatasourceRepository datasourceRepository,
                           DatasourceFieldRepository datasourceFieldRepository,
                           UDFRepository udfRepository,
                           UDFFieldRepository udfFieldRepository,
                           EventService eventService,
                           MappingRepository mappingRepository) {
        this.microservicesRepository = microservicesRepository;
        this.datasourceRepository = datasourceRepository;
        this.datasourceFieldRepository = datasourceFieldRepository;
        this.udfRepository = udfRepository;
        this.udfFieldRepository = udfFieldRepository;
        this.eventService = eventService;
        this.mappingRepository = mappingRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (this.microservicesRepository.findAll().size() == 0) {
            getLogger().info("Base de datos vacia, se inicializan algunos UDFs...");
            microservicesRepository.deleteAll();
            datasourceRepository.deleteAll();
            udfRepository.deleteAll();
            mappingRepository.deleteAll();
            getLogger().info("se borran todos los registros");

            List<Microservice> microservices = new ArrayList<>();

            microservices.add(new Microservice(UUID.randomUUID(), "cartera", LocalDate.now(), LocalDate.now(),
                                "START", true, new ArrayList<>()));
            microservices.add(new Microservice(UUID.randomUUID(), "ingestor", LocalDate.now(), LocalDate.now(),
                    "START", true, new ArrayList<>()));
            microservicesRepository.saveAll(microservices);

            var portfolio = microservicesRepository.findByNombre("cartera");

            portfolio.getDatasources().add(datasourceRepository.save(new Datasource(UUID.randomUUID(), "Posicion", portfolio, new ArrayList<>(), "", "")));
            portfolio.getDatasources().add(datasourceRepository.save(new Datasource(UUID.randomUUID(), "MovPosicion", portfolio, new ArrayList<>(),"", "")));
            portfolio.getDatasources().add(datasourceRepository.save(new Datasource(UUID.randomUUID(), "MovEfectivo", portfolio, new ArrayList<>(), "", "")));

            var posicion = datasourceRepository.findByNombre("Posicion");

            posicion.getFields().add(datasourceFieldRepository.save(new DatasourceField(UUID.randomUUID(), posicion, "emisora", "Texto")));
            posicion.getFields().add(datasourceFieldRepository.save(new DatasourceField(UUID.randomUUID(), posicion, "serie", "Texto")));
            posicion.getFields().add(datasourceFieldRepository.save(new DatasourceField(UUID.randomUUID(), posicion, "titulosOperados", "Real")));
            posicion.getFields().add(datasourceFieldRepository.save(new DatasourceField(UUID.randomUUID(), posicion, "costoPromedioLimpio", "Real")));
            posicion.getFields().add(datasourceFieldRepository.save(new DatasourceField(UUID.randomUUID(), posicion, "costoPromedioSucio", "Real")));
            posicion.getFields().add(datasourceFieldRepository.save(new DatasourceField(UUID.randomUUID(), posicion, "precioLimpioVector", "Real")));
            posicion.getFields().add(datasourceFieldRepository.save(new DatasourceField(UUID.randomUUID(), posicion, "precioSucioVector", "Real")));
            posicion.getFields().add(datasourceFieldRepository.save(new DatasourceField(UUID.randomUUID(), posicion, "plusMinusvaliaAcum", "Real")));
            posicion.getFields().add(datasourceFieldRepository.save(new DatasourceField(UUID.randomUUID(), posicion, "costoTotalLimpio", "Real")));
            posicion.getFields().add(datasourceFieldRepository.save(new DatasourceField(UUID.randomUUID(), posicion, "costoTotalSucio", "Real")));
            datasourceRepository.save(posicion);

            var movPosicion = datasourceRepository.findByNombre("MovPosicion");

            movPosicion.getFields().add(datasourceFieldRepository.save(new DatasourceField(UUID.randomUUID(), movPosicion, "tipoValor", "Texto")));
            movPosicion.getFields().add(datasourceFieldRepository.save(new DatasourceField(UUID.randomUUID(), movPosicion, "emisora", "Texto")));
            movPosicion.getFields().add(datasourceFieldRepository.save(new DatasourceField(UUID.randomUUID(), movPosicion, "serie", "Texto")));
            movPosicion.getFields().add(datasourceFieldRepository.save(new DatasourceField(UUID.randomUUID(), movPosicion, "tipoOperacion", "Texto")));
            movPosicion.getFields().add(datasourceFieldRepository.save(new DatasourceField(UUID.randomUUID(), movPosicion, "liquidacion", "Texto")));
            movPosicion.getFields().add(datasourceFieldRepository.save(new DatasourceField(UUID.randomUUID(), movPosicion, "fechaOper", "Fecha")));
            movPosicion.getFields().add(datasourceFieldRepository.save(new DatasourceField(UUID.randomUUID(), movPosicion, "fechaLiq", "Fecha")));
            movPosicion.getFields().add(datasourceFieldRepository.save(new DatasourceField(UUID.randomUUID(), movPosicion, "titulos", "Real")));
            movPosicion.getFields().add(datasourceFieldRepository.save(new DatasourceField(UUID.randomUUID(), movPosicion, "tasa", "Real")));
            movPosicion.getFields().add(datasourceFieldRepository.save(new DatasourceField(UUID.randomUUID(), movPosicion, "plazo", "Entero")));
            movPosicion.getFields().add(datasourceFieldRepository.save(new DatasourceField(UUID.randomUUID(), movPosicion, "precio", "Real")));
            datasourceRepository.save(movPosicion);

            var movEfectivo = datasourceRepository.findByNombre("MovEfectivo");

            movEfectivo.getFields().add(datasourceFieldRepository.save(new DatasourceField(UUID.randomUUID(), movEfectivo, "divisa", "Texto")));
            movEfectivo.getFields().add(datasourceFieldRepository.save(new DatasourceField(UUID.randomUUID(), movEfectivo, "monto", "Real")));
            movEfectivo.getFields().add(datasourceFieldRepository.save(new DatasourceField(UUID.randomUUID(), movEfectivo, "montoMXP", "Real")));
            movEfectivo.getFields().add(datasourceFieldRepository.save(new DatasourceField(UUID.randomUUID(), movEfectivo, "fechaLiq", "Fecha")));
            datasourceRepository.save(movEfectivo);

            microservicesRepository.save(portfolio);

            var ingestor = microservicesRepository.findByNombre( "ingestor");

            ingestor.getDatasources().add(datasourceRepository.save(new Datasource(UUID.randomUUID(), "Vector", ingestor, new ArrayList<>(),  "", "")));
            ingestor.getDatasources().add(datasourceRepository.save(new Datasource(UUID.randomUUID(), "Archivo", ingestor, new ArrayList<>(), "", "")));

            var vector = datasourceRepository.findByNombre("Vector");

            vector.getFields().add(datasourceFieldRepository.save(new DatasourceField(UUID.randomUUID(), vector, "nombre", "Textor")));
            datasourceRepository.save(vector);

            var archivo = datasourceRepository.findByNombre("Archivo");

            archivo.getFields().add(datasourceFieldRepository.save(new DatasourceField(UUID.randomUUID(), vector, "nombre", "Textor")));
            datasourceRepository.save(archivo);

            microservicesRepository.save(ingestor);

            // UDF and UDFs fields
            List<UDF> UDFs = new ArrayList<>();

            UDFs.add(new UDF(UUID.randomUUID(), "Calif_Actinver", LocalDate.now(), LocalDate.now(), "START", true, "", "", new ArrayList<>(), new ArrayList<>()));
            UDFs.add(new UDF(UUID.randomUUID(), "Resta_importes", LocalDate.now(), LocalDate.now(), "START", true, "", "", new ArrayList<>(), new ArrayList<>()));
            UDFs.add(new UDF(UUID.randomUUID(), "Liquidaciones_UDF", LocalDate.now(), LocalDate.now(), "START", true, "", "", new ArrayList<>(), new ArrayList<>()));
            UDFs.add(new UDF(UUID.randomUUID(), "Titulos_UDF", LocalDate.now(), LocalDate.now(), "START", true, "", "", new ArrayList<>(), new ArrayList<>()));
            udfRepository.saveAll(UDFs);

            var califActinver = udfRepository.findByNombre("Calif_Actinver");

            califActinver.getFields().add(udfFieldRepository.save(new UDFField(UUID.randomUUID(), califActinver, "emisora", "Texto")));
            califActinver.getFields().add(udfFieldRepository.save(new UDFField(UUID.randomUUID(), califActinver, "serie", "Texto")));
            udfRepository.save(califActinver);

            var restaImportes = udfRepository.findByNombre( "Resta_importes");

            restaImportes.getFields().add(udfFieldRepository.save(new UDFField(UUID.randomUUID(), restaImportes, "importeLimpio", "Real")));
            restaImportes.getFields().add(udfFieldRepository.save(new UDFField(UUID.randomUUID(), restaImportes, "importeSucio", "Real")));
            udfRepository.save(califActinver);

            var liquidacionesUDF = udfRepository.findByNombre( "Liquidaciones_UDF");

            liquidacionesUDF.getFields().add(udfFieldRepository.save(new UDFField(UUID.randomUUID(), liquidacionesUDF, "liquidacion", "Texto")));
            udfRepository.save(califActinver);

            var titulosUDF = udfRepository.findByNombre("Titulos_UDF");

            titulosUDF.getFields().add(udfFieldRepository.save(new UDFField(UUID.randomUUID(), titulosUDF, "emisora", "Texto")));
            titulosUDF.getFields().add(udfFieldRepository.save(new UDFField(UUID.randomUUID(), titulosUDF, "serie", "Texto")));
            titulosUDF.getFields().add(udfFieldRepository.save(new UDFField(UUID.randomUUID(), titulosUDF, "titulos", "Real")));
            udfRepository.save(califActinver);

            // Mapping
            List<Mapping> mappings = new ArrayList<>();

            mappings.add(new Mapping(UUID.randomUUID(), "Posicion", "cartera", udfRepository.findByNombre( "Resta_importes"),
                        "(importeLimpio, precioLimpioVector), (importeSucio, precioSucioVector)", "", ""));
            mappings.add(new Mapping(UUID.randomUUID(), "MovPosicion", "cartera", udfRepository.findByNombre( "Liquidaciones_UDF"),
                        "(liquidacion, liquidacion)", "", ""));
            mappings.add(new Mapping(UUID.randomUUID(), "MovPosicion", "cartera", udfRepository.findByNombre("Titulos_UDF"),
                        "(titulos, titulos)", "", ""));
            mappings.add(new Mapping(UUID.randomUUID(), "MovPosicion", "cartera", udfRepository.findByNombre("Calif_Actinver"),
                        "(emisora, emisora)", "", ""));
            mappings.add(new Mapping(UUID.randomUUID(), "Posicion", "cartera", udfRepository.findByNombre("Calif_Actinver"),
                        "(serie, serie), (emisora, emisora)", "", ""));
            mappingRepository.saveAll(mappings);

            getLogger().info("done data initialization...");
            try {
                eventService.sendEvent("NA", "NA", "INICIALIZA DB udfDB", microservices);
            } catch (Exception e) {
                e.printStackTrace();
                getLogger().warn("No se pudo enviar el mensaje de INICIALIZA DB udfDB");
            }
        }
    }

    @NotNull
    @Override
    public Logger getLogger() { return HasLogger.DefaultImpls.getLogger(this); }

}

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
 *  UDFService.kt
 *
 *  Developed 2023 by LegoSoftSoluciones, S.C. www.legosoft.com.mx
 */
package com.ailegorreta.udfservice.service

import com.ailegorreta.commons.utils.HasLogger
import com.ailegorreta.udfservice.dto.UDFDTO
import com.ailegorreta.udfservice.domain.MappingRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.collections.ArrayList

/**
 *
 * @project: udf-service
 * @author rlh
 * @dat: October 2023
 */
@Service
class UDFService(private val mappingRepository: MappingRepository): HasLogger {

    /**
     * Query all UDF for a given micro.service and datasource
     *
     */
    @Transactional
    fun opbtenerUDFs(microservicio: String, datasource: String): Collection<UDFDTO> {
        val result = ArrayList<UDFDTO>()
        val mappings = mappingRepository.findByMicroservicioAndDatasource(microservicio, datasource)

        mappings.forEach { mapping ->
            result.add(UDFDTO.fromEntity(mapping.udf!!))
        }
        return result
    }

}

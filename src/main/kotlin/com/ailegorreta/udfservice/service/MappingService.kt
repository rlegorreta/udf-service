/* Copyright (c) 2023, LMASS Desarrolladores S.C.
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
 *  MappingService.kt
 *
 *  Developed 2023 by LLMASS Desarrolladores, S.C. www.lmass.com.mx
 */
package com.ailegorreta.udfservice.service

import com.ailegorreta.udfservice.dto.MappingDTO
import com.ailegorreta.udfservice.domain.MappingRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 *
 * @project: udf-service
 * @author rlh
 * @dat: October 2023
 */
@Service
class MappingService(private val mappingRepository: MappingRepository) {

    /**
     * Queries a mapping for a specific UDF, microservice and datasource
     */
    @Transactional
    fun obtenerMapping(udf: String, microservice: String, datasource: String): Optional<MappingDTO> {
        val mappings = mappingRepository.obtenerMapping(udf, microservice, datasource)

        if (mappings.isEmpty())
            return Optional.empty()

        return Optional.of(MappingDTO.fromEntity(mappings.first()))
    }
}

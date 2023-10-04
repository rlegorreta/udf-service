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
 *  MappingDTO.kt
 *
 *  Developed 2023 by LegoSoftSoluciones, S.C. www.legosoft.com.mx
 */
package com.ailegorreta.udfservice.dto

import com.ailegorreta.commons.dtomappers.EntityDTOMapper
import com.ailegorreta.udfservice.domain.Mapping
import java.util.UUID


/**
 * Data class for MappingDTO
 *
 * @author rlh
 * @project : udf-service
 * @date October 2023
 */
class MappingDTO (var id: UUID,
                 var microservicio: String,
                 var datasource: String,
                 var mapping: String = "{}",
                 var code: String = "",
                 var blockly: String = "") {

    companion object: EntityDTOMapper<Mapping, MappingDTO> {
        override var dtos = HashMap<Int, Any>()

        override fun fromEntityRecursive(entity: Mapping): MappingDTO {
            val a = dtos[entity.hashCode()]

            if (a != null)
                return a as MappingDTO

            var mappingDTO =  MappingDTO (id = entity.id,
                microservicio = entity.microservicio,
                datasource = entity.datasource,
                mapping = entity.mapping,
                code = entity.code,
                blockly = entity.blockly)

            dtos[entity.hashCode()] = mappingDTO

            return mappingDTO
        }
    }
}

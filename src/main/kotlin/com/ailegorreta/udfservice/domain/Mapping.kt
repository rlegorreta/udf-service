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
 *  Mapping.kt
 *
 *  Developed 2023 by LLMASS Desarrolladores, S.C. www.lmass.com.mx
 */
package com.ailegorreta.udfservice.domain

import com.ailegorreta.commons.dtomappers.EntityDTOMapper
import com.fasterxml.jackson.annotation.JsonBackReference
import com.ailegorreta.udfservice.dto.MappingDTO
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import java.util.UUID


/**
 * Hash map for each microservice + datasource
 *
 * This table is to store the hashmap mapping between a UDF and a microservice + datasource
 *
 * The hashmap is stored as a string
 *
 *  @author rlh
 *  @project :UDF UI
 *  @date January 2023
 */
@Entity
@Table(name = "mapping")
data class Mapping (@Id
                    @GeneratedValue
                    var id: UUID,

                    @Column(name = "datasource")
                    var datasource: String = "",

                    @Column(name = "microservice")
                    var microservicio: String = "",

                    @JsonBackReference
                    @ManyToOne(fetch = FetchType.LAZY)
                    @JoinColumn(name = "id_udf", referencedColumnName = "id", insertable = true, updatable = false)
                    var udf: UDF? = null,

                    @Column(name = "mapping", length = 2048)
                    var mapping: String = "{}",

                    @NotNull
                    @Column(name = "code", length = 2048)
                    var code: String = "",

                    @NotNull
                    @Column(name = "blockly_Blocks", length = 5120)
                    var blockly: String = "") {

    companion object: EntityDTOMapper<MappingDTO, Mapping> {

        override var dtos = HashMap<Int, Any>()

        override fun fromEntityRecursive(dto: MappingDTO): Mapping {
            val a = dtos[dto.hashCode()]

            if (a != null)
                return a as Mapping

            var mapping =  Mapping (id = dto.id,
                microservicio = dto.microservicio,
                datasource = dto.datasource,
                mapping = dto.mapping,
                code = dto.code,
                blockly = dto.blockly
            )

            dtos.put(dto.hashCode(), mapping)

            return mapping
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other !is Mapping) return false

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

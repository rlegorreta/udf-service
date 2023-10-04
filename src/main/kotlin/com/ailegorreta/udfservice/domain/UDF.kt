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
 *  UDF.kt
 *
 *  Developed 2023 by LLMASS Desarrolladores, S.C. www.lmass.com.mx
 */
package com.ailegorreta.udfservice.domain

import com.ailegorreta.commons.dtomappers.EntityDTOMapper
import com.ailegorreta.udfservice.dto.UDFDTO
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDate
import java.util.*

/**
 * Table header for UDFs
 *
 * This table is to store the name and general information for the UDF. It has two detail tables:
 * - One for all its parameter fields declaration
 * - One for all the mapping to different data sources for each microservice
 *
 *  @author rlh
 *  @project :udf-service
 *  @date October 2023
 */
@Entity
@Table(name = "udfs")
data class UDF (@Id
                @GeneratedValue
                var id: UUID,

                @Column(name = "name")
                var nombre: String = "",

                @CreatedDate @Column(name = "creation_date") var fechaCreacion: @NotNull LocalDate = LocalDate.now(),

                @LastModifiedDate
                @Column(name = "modification_date") val ultimaModificacion: @NotNull LocalDate = LocalDate.now(),

                @Column(name = "author")
                val autor: String = "",

                @Column(name = "active")
                val activa: Boolean = true,

                @Column(name = "json")
                var json: String? = null,

                @Column(name = "blockly_Blocks")
                var blockly: String? = null,

                @Column(nullable = true)
                @OneToMany(mappedBy = "udf", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
                var fields: MutableList<UDFField>? = null,

                @Column(nullable = true)
                @OneToMany(mappedBy = "udf", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
                var mappings: MutableList<Mapping>? = null) {

    companion object: EntityDTOMapper<UDFDTO, UDF> {
        override var dtos = HashMap<Int, Any>()

        override fun fromEntityRecursive(dto: UDFDTO): UDF {
            val a = dtos[dto.hashCode()]

            if (a != null)
                return a as UDF

            val udf =  UDF (id = dto.id,
                nombre = dto.nombre,
                fechaCreacion = dto.fechaCreacion,
                autor = dto.autor,
                activa = dto.activa,
                json = dto.json,
                blockly = dto.blockly
            )

            dtos[dto.hashCode()] = udf
            dto.fields.let { udf.fields = UDFField.mapFromEntities(it) as MutableList<UDFField> }
            udf.fields?.forEach { it.udf = udf }     // set ManyToOne relationship
            dto.mappings.let { udf.mappings = Mapping.mapFromEntities(it) as MutableList<Mapping>}
            udf.mappings?.forEach { it.udf = udf }   // set ManyToOne relationship

            return udf
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other !is UDF) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

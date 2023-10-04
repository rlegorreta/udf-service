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
 *  UDFDTO.java
 *
 *  Developed 2023 by LegoSoftSoluciones, S.C. www.legosoft.com.mx
 */
package com.ailegorreta.udfservice.dto

import com.ailegorreta.commons.dtomappers.EntityDTOMapper
import com.fasterxml.jackson.annotation.JsonIgnore
import com.ailegorreta.udfservice.domain.UDF
import java.time.LocalDate
import java.util.UUID


/**
 * Data class for header of the UDF
 *
 * @author rlh
 * @project : udf-service
 * @date October 2023
 */
data class UDFDTO(var id: UUID,
                  var nombre: String,
                  var fechaCreacion: LocalDate = LocalDate.now(),
                  var ultimaModificacion: LocalDate = LocalDate.now(),
                  var autor: String = "",
                  var activa: Boolean = true,
                  var json: String? = null,
                  var blockly: String? = null,
                  var fields: List<UDFFieldDTO> = ArrayList(),
                  var mappings: List<MappingDTO> = ArrayList()) {

    @JsonIgnore
    fun getActivaStr() = if (activa) "Activa" else "Suspendida"

    fun setActivaStr(activoStr: String):Unit {
        activa = activoStr.equals("Activa")
    }

    companion object: EntityDTOMapper<UDF, UDFDTO> {

        override var dtos = HashMap<Int, Any>()

        override fun fromEntityRecursive(entity: UDF): UDFDTO {
            val a = dtos[entity.hashCode()]

            if (a != null)
                return a as UDFDTO

            val udfDTO =  UDFDTO (id = entity.id,
                nombre = entity.nombre,
                fechaCreacion = entity.fechaCreacion,
                ultimaModificacion = entity.ultimaModificacion,
                autor = entity.autor,
                activa = entity.activa,
                json = entity.json,
                blockly = entity.blockly
            )

            dtos.put(entity.hashCode(), udfDTO)
            entity.fields.let { udfDTO.fields = UDFFieldDTO.mapFromEntities(it) as List<UDFFieldDTO> }
            entity.mappings.let{ udfDTO.mappings = MappingDTO.mapFromEntities(it) as List<MappingDTO>}

            return udfDTO
        }
    }
}

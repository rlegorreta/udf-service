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
 *  UDFField.kt
 *
 *  Developed 2023 by LegoSoftSoluciones, S.C. www.legosoft.com.mx
 */
package com.ailegorreta.udfservice.domain

import com.ailegorreta.commons.dtomappers.EntityDTOMapper
import com.fasterxml.jackson.annotation.JsonBackReference
import com.ailegorreta.udfservice.dto.UDFFieldDTO
import jakarta.persistence.*
import org.hibernate.proxy.HibernateProxy
import java.util.UUID


/**
 * Parameter fields defined in a UDF.
 *
 * This table is to store the parameter fields for a UDF.
 *
 * The field type is just for reference, no type checking is done because we can convert the field
 * inside the Phyton code (see documentation).
 *
 *  @author rlh
 *  @project :udf-service
 *  @date October 2023
 */
@Entity
@Table(name = "udf_fields")
data class UDFField (@Id
                     @GeneratedValue
                     var id: UUID,

                     @JsonBackReference
                     @ManyToOne(fetch = FetchType.LAZY)
                     @JoinColumn(name = "id_udf", referencedColumnName = "id", insertable = true, updatable = false)
                     var udf: UDF? = null,

                     @Column(name = "name")
                     var nombre: String = "",

                     @Column(name = "type")
                     var fieldType: String = FieldType.Texto.toString()) {

    companion object: EntityDTOMapper<UDFFieldDTO, UDFField> {

        override var dtos = HashMap<Int, Any>()

        override fun fromEntityRecursive(dto: UDFFieldDTO): UDFField {
            val a = dtos.get(dto.hashCode())

            if (a != null)
                return a as UDFField

            val udfField =  UDFField (id = dto.id,
                nombre = dto.nombre,
                fieldType = dto.fieldType.toString()
            )

            dtos.put(dto.hashCode(), udfField)

            return udfField
        }
    }

    enum class FieldType {
        Texto, Entero, Real, Fecha, ERROR;

        companion object {
            fun fromString(value: String): FieldType {
                entries.forEach { if (it.toString() == value) return it }

                return ERROR
            }
        }

        fun toMxGraph() = when (this) {
            Texto -> "String"
            Entero -> "Integer"
            Real -> "Double"
            Fecha -> "Date"
            ERROR -> "String"
        }
    }

    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        val oEffectiveClass =
            if (other is HibernateProxy) other.hibernateLazyInitializer.persistentClass else other.javaClass
        val thisEffectiveClass =
            if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass else this.javaClass
        if (thisEffectiveClass != oEffectiveClass) return false
        other as UDFField

        return id == other.id
    }

    final override fun hashCode(): Int =
        if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass.hashCode() else javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , nombre = $nombre , fieldType = $fieldType )"
    }

}

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
 *  DataSourceField.kt
 *
 *  Developed 2023 by LegoSoftSoluciones, S.C. www.legosoft.com.mx
 */
package com.ailegorreta.udfservice.domain

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import org.hibernate.proxy.HibernateProxy
import java.util.*

/**
 * Datasource fields defined in a UDF.
 *
 * This table is to store the database fields for a UDF.
 *
 * This Entity is 'just' declared for database initialization purpose. It is not used by this
 * service
 *
 *  @author rlh
 *  @project :udf-service
 *  @date October 2023
 */
@Entity
@Table(name = "datasource_fields")
data class DatasourceField (@Id
                     @GeneratedValue
                     var id: UUID,

                     @JsonBackReference
                     @ManyToOne(fetch = FetchType.LAZY)
                     @JoinColumn(name = "id_datasource", referencedColumnName = "id", insertable = true, updatable = false)
                     var datasource: Datasource? = null,

                     @Column(name = "name")
                     var nombre: String = "",

                     @Column(name = "type")
                     var fieldType: String = FieldType.Texto.toString()) {

    enum class FieldType {
        Texto, Entero, Real, Fecha, ERROR;

        companion object {
            fun fromString(value: String): FieldType {
                entries.forEach { if (it.toString() == value) return it }

                return ERROR
            }
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

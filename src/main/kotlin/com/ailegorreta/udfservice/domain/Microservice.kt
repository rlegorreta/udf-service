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
 *  MicroService.kt
 *
 *  Developed 2023 by LLMASS Desarrolladores, S.C. www.lmass.com.mx
 */
package com.ailegorreta.udfservice.domain

import java.util.ArrayList
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDate
import java.util.UUID


/**
 * Table header for microservices that support UDFs
 *
 * This table is to store the name and general information for the microservice. It has one detail table:
 * - All its datasource that the microservice exposes
 *
 *  This Entity is 'just' declared for database initialization purpose. It is not used by this
 *  service
 *
 *  @author rlh
 *  @project :udf-service
 *  @date October 2023
 */
@Entity
@Table(name = "microservices")
data class Microservice (@Id
                         @GeneratedValue
                         var id: UUID,

                         @Column(name = "name")
                         var nombre: String = "",

                         @CreatedDate @Column(name = "creation_date")
                         var fechaCreacion: @NotNull LocalDate = LocalDate.now(),

                         @LastModifiedDate
                         @Column(name = "modification_date")
                         val ultimaModificacion: @NotNull LocalDate = LocalDate.now(),

                         @Column(name = "author")
                         val autor: String = "",

                         @Column(name = "active")
                         val activa: Boolean = true,

                         @Column(nullable = true)
                         @OneToMany(mappedBy = "microservice", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
                         var datasources: MutableList<Datasource>? = null) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other !is Microservice) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return if (id != null) id.hashCode() else 0
    }
}

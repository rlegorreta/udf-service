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
 *  Datasource.kt
 *
 *  Developed 2023 by LLMASS Desarrolladores, S.C. www.lmass.com.mx
 */
package com.ailegorreta.udfservice.domain

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import java.util.*

/**
 * Table header for Datasources
 *
 * This table is to store the name and general information for the Datasource. It has two detail tables:
 * - One for all its parameter fields declaration
 *
 *  This Entity is 'just' declared for database initialization purpose. It is not used by this
 *  service
 *
 *  @author rlh
 *  @project :udf-service
 *  @date October 2023
 */
@Entity
@Table(name = "datasources")
data class Datasource (@Id
                @GeneratedValue
                var id: UUID,

               @Column(name = "name")
                var nombre: String = "",

                @JsonBackReference
                @ManyToOne(fetch = FetchType.LAZY)
                @JoinColumn(name = "id_microservice", referencedColumnName = "id", insertable = true, updatable = false)
                var microservice: Microservice? = null,

                @Column(nullable = true)
                @OneToMany(mappedBy = "datasource", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
                var fields: MutableList<DatasourceField>? = null,

                @Column(name = "json")
                val json: String = "",

                @Column(name = "blockly_Blocks")
                var blockly: String = "") {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other !is UDF) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

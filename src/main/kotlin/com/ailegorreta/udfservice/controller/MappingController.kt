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
 *  MappingController.kt
 *
 *  Developed 2023 by LegoSoftSoluciones, S.C. www.legosoft.com.mx
 */
package com.ailegorreta.udfservice.controller

import com.ailegorreta.commons.utils.HasLogger
import com.ailegorreta.udfservice.service.MappingService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

/**
 * Controller where we can read the mapping UDFs
 *
 * @project: udf-service
 * @author: rlh
 * @date: October 2023
 */
@Controller
@RequestMapping("/udf")
class MappingController(private val mappingService: MappingService): HasLogger {

    @GetMapping("/mapping", produces = ["application/json"])
    fun obtenerUDFs(@RequestParam(required=true) udf: String,
                    @RequestParam(required=true) microservicio: String,
                    @RequestParam(required=true) datasource: String) = mappingService.obtenerMapping(udf, microservicio, datasource)

}

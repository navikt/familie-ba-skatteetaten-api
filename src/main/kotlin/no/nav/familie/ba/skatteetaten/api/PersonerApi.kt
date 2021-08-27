package org.openapitools.api

import org.openapitools.model.PersonerResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity

import org.springframework.web.bind.annotation.*
import org.springframework.validation.annotation.Validated
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.beans.factory.annotation.Autowired

import javax.validation.Valid
import javax.validation.constraints.DecimalMax
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

import kotlin.collections.List
import kotlin.collections.Map

@RestController
@Validated
@RequestMapping("/api")
class PersonerApiController(@Autowired(required = true) val service: PersonerApiService) {


    @GetMapping(
        value = ["/personer"],
        produces = ["application/json;charset=UTF-8"]
    )
    fun finnPersonerMedUtvidetBarnetrygd(@NotNull  @RequestParam(value = "aar", required = true) aar: kotlin.String
, @RequestHeader(value="Authorization", required=true) authorization: kotlin.String
): ResponseEntity<PersonerResponse> {
        return ResponseEntity(service.finnPersonerMedUtvidetBarnetrygd(aar, authorization), HttpStatus.valueOf(200))
    }


    @GetMapping(
        value = ["/ping"],
        produces = ["application/json;charset=UTF-8"]
    )
    fun ping(): ResponseEntity<String> {
        return ResponseEntity.ok("OK")
    }
}

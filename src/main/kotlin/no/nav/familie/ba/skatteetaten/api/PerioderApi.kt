package org.openapitools.api

import org.openapitools.model.PerioderRequest
import org.openapitools.model.PerioderResponse
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
@RequestMapping("/api/foo")
class PerioderApiController(@Autowired(required = true) val service: PerioderApiService) {


    @PostMapping(
        value = ["/perioder"],
        produces = ["application/json;charset=UTF-8"],
        consumes = ["application/json"]
    )
    fun hentPerioderMedUtvidetBarnetrygd(@NotNull  @RequestParam(value = "aar", required = true) aar: kotlin.String
, @RequestHeader(value="Authorization", required=true) authorization: kotlin.String
, @Valid @RequestBody perioderRequest: PerioderRequest
): ResponseEntity<PerioderResponse> {
        return ResponseEntity(service.hentPerioderMedUtvidetBarnetrygd(aar, authorization, perioderRequest), HttpStatus.valueOf(200))
    }
}

package org.openapitools.model

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
import org.openapitools.model.Periode
import javax.validation.constraints.DecimalMax
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size
import javax.validation.Valid

/**
 * 
 * @param ident Person identifikator
 * @param sisteVedtakPaaIdent Tidspunkt for siste vedtak (systemtidspunkt)
 * @param perioder 
 */
data class Person(

    @get:NotNull  
    @field:JsonProperty("ident") val ident: kotlin.String,

    @get:NotNull  
    @field:JsonProperty("sisteVedtakPaaIdent") val sisteVedtakPaaIdent: java.time.OffsetDateTime,

    @field:Valid
    @field:JsonProperty("perioder") val perioder: kotlin.collections.List<Periode>? = null
) {

}


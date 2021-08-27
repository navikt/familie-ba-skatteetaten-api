package org.openapitools.model

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
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
 * @param identer Liste over fødselsnumre det ønskes opplysninger om.
 */
data class PerioderRequest(

    @get:NotNull  
    @field:JsonProperty("identer") val identer: kotlin.collections.List<kotlin.String>
) {

}


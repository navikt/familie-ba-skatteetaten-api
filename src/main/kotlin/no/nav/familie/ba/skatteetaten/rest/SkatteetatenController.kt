package no.nav.familie.ba.skatteetaten.rest

import no.nav.commons.foedselsnummer.FoedselsNr
import no.nav.familie.ba.skatteetaten.service.SkatteetatenService
import no.nav.familie.eksterne.kontrakter.skatteetaten.SkatteetatenPerioderRequest
import no.nav.familie.eksterne.kontrakter.skatteetaten.SkatteetatenPerioderResponse
import no.nav.familie.eksterne.kontrakter.skatteetaten.SkatteetatenPersonerResponse
import no.nav.security.token.support.core.api.ProtectedWithClaims
import no.nav.security.token.support.core.api.Unprotected
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Year
import javax.validation.Valid
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
import kotlin.math.min

@RestController
@Validated
@RequestMapping("/api/v1")
@Unprotected
//@ProtectedWithClaims(issuer = "maskinporten", claimMap = ["scope=nav:familie/v1/barnetrygd/utvidet"])
class SkatteetatenController(@Autowired(required = true) val service: SkatteetatenService) {


    @GetMapping(
        value = ["/personer"],
        produces = ["application/json;charset=UTF-8"]
    )
    fun finnPersonerMedUtvidetBarnetrygd(
        @Size(min = 4, max = 4, message = "Ugyldig format, må være år med 4 siffer")
        @RequestParam( value = "aar", required = true) aar: Year
    ): ResponseEntity<SkatteetatenPersonerResponse> {
        return ResponseEntity(service.finnPersonerMedUtvidetBarnetrygd(aar.toString()), HttpStatus.valueOf(200))
    }


    @PostMapping(
        value = ["/perioder"],
        produces = ["application/json;charset=UTF-8"],
        consumes = ["application/json"]
    )
    fun hentPerioderMedUtvidetBarnetrygd(
        @Valid @RequestBody perioderRequest: SkatteetatenPerioderRequest
    ): ResponseEntity<SkatteetatenPerioderResponse> {

       erSkatteetatenPeriodeRequestGyldig(perioderRequest)

       Return ResponseEntity(
                service.hentPerioderMedUtvidetBarnetrygd(perioderRequest),
                HttpStatus.valueOf(200)
            )

    }

    fun erSkatteetatenPeriodeRequestGyldig(perioderRequest: SkatteetatenPerioderRequest){
        for (personident: String in perioderRequest.identer) {
            require("""\d{11}""".toRegex().matches(personident)) { "Ikke et gyldig fødselsnummer: $personident" }
        }
    }
}

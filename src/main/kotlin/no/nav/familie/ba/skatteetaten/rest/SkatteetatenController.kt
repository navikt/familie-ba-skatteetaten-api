package no.nav.familie.ba.skatteetaten.rest

import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import no.nav.familie.ba.skatteetaten.service.SkatteetatenService
import no.nav.familie.eksterne.kontrakter.skatteetaten.SkatteetatenPerioderRequest
import no.nav.familie.eksterne.kontrakter.skatteetaten.SkatteetatenPerioderResponse
import no.nav.familie.eksterne.kontrakter.skatteetaten.SkatteetatenPersonerResponse
import no.nav.security.token.support.core.api.ProtectedWithClaims
import org.slf4j.LoggerFactory
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

@RestController
@Validated
@RequestMapping("/api/v1")
@ProtectedWithClaims(issuer = "maskinporten", claimMap = ["scope=nav:familie/v1/barnetrygd/utvidet"])
class SkatteetatenController(@Autowired(required = true) val service: SkatteetatenService) {
    private val logger = LoggerFactory.getLogger(SkatteetatenController::class.java)

    @GetMapping(
        value = ["/personer"],
        produces = ["application/json;charset=UTF-8"]
    )
    fun finnPersonerMedUtvidetBarnetrygd(
        @Min(value = 2020, message = "Ugyldig format, kan ikke være eldre enn 2020")
        @Max(value = 2050, message = "Ugyldig format, kan ikke spørre om år etter 2050")
        @RequestParam(value = "aar", required = true)
        aar: Int
    ): ResponseEntity<SkatteetatenPersonerResponse> {
        logger.info("Henter skatteetaten-personer for år=$aar")
        return ResponseEntity(service.finnPersonerMedUtvidetBarnetrygd(aar.toString()), HttpStatus.OK)
    }

    @PostMapping(
        value = ["/perioder"],
        produces = ["application/json;charset=UTF-8"],
        consumes = ["application/json"]
    )
    fun hentPerioderMedUtvidetBarnetrygd(
        @Valid @RequestBody
        perioderRequest: SkatteetatenPerioderRequest
    ): ResponseEntity<SkatteetatenPerioderResponse> {
        erSkatteetatenPeriodeRequestGyldig(perioderRequest)
        logger.info("Henter skatteetaten-perioder for år=${perioderRequest.aar}")

        return ResponseEntity(
            service.hentPerioderMedUtvidetBarnetrygd(perioderRequest),
            HttpStatus.OK
        )
    }

    fun erSkatteetatenPeriodeRequestGyldig(perioderRequest: SkatteetatenPerioderRequest) {
        Year.of(perioderRequest.aar.toInt())
        if (perioderRequest.identer.size > MAX_ANTALL_I_PERIODER) {
            throw IllegalArgumentException("Maks antall identer er 10000")
        }
        for (personident: String in perioderRequest.identer) {
            require("""\d{11}""".toRegex().matches(personident)) { "Ikke et gyldig fødselsnummer: $personident" }
        }
    }

    companion object {
        const val MAX_ANTALL_I_PERIODER = 10000
    }
}

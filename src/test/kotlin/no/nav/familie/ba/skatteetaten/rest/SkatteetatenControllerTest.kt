package no.nav.familie.ba.skatteetaten.rest

import io.mockk.mockk
import no.nav.familie.eksterne.kontrakter.skatteetaten.SkatteetatenPerioderRequest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class SkatteetatenControllerTest {

    @Test
    fun `erSkatteetatenPeriodeRequestGyldig skal fange opp ugyldig input` () {
        val skatteetatenController = SkatteetatenController(mockk())

        val skatteetatenPerioderRequest = SkatteetatenPerioderRequest(aar = "2020",
                                                                      identer = listOf("12345678910", "123"))

        Assertions.assertThrows(
            IllegalArgumentException::class.java
        ) { skatteetatenController.erSkatteetatenPeriodeRequestGyldig(skatteetatenPerioderRequest) }
    }

    @Test
    fun `erSkatteetatenPeriodeRequestGyldig skal ikke kaste feil ved gyldig input` () {
        val skatteetatenController  = SkatteetatenController(mockk())

        val skatteetatenPerioderRequest = SkatteetatenPerioderRequest(aar = "2020",
                                                                      identer = listOf("27903249671"))

        skatteetatenController.erSkatteetatenPeriodeRequestGyldig(skatteetatenPerioderRequest)

    }

    @Test
    fun `erSkatteetatenPeriodeRequestGyldig skal kaste feil ved ugyldig år` () {
        val skatteetatenController  = SkatteetatenController(mockk())

        val skatteetatenPerioderRequest = SkatteetatenPerioderRequest(aar = "sdsdf",
                                                                      identer = listOf("27903249671"))

        Assertions.assertThrows(
            IllegalArgumentException::class.java
        ) { skatteetatenController.erSkatteetatenPeriodeRequestGyldig(skatteetatenPerioderRequest) }

    }
}
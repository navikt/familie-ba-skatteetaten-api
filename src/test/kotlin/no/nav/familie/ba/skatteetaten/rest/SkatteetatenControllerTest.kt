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

        Assertions.assertEquals(skatteetatenController.erSkatteetatenPeriodeRequestGyldig(skatteetatenPerioderRequest), false)
    }

    @Test
    fun `erSkatteetatenPeriodeRequestGyldig skal ikke kaste feil ved gyldig input` () {
        val skatteetatenController  = SkatteetatenController(mockk())

        val skatteetatenPerioderRequest = SkatteetatenPerioderRequest(aar = "2020",
                                                                      identer = listOf("27903249671"))

        Assertions.assertEquals(skatteetatenController.erSkatteetatenPeriodeRequestGyldig(skatteetatenPerioderRequest), true)
    }
}
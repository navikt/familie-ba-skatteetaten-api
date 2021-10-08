package no.nav.familie.ba.skatteetaten.rest

import io.mockk.mockk
import no.nav.familie.eksterne.kontrakter.skatteetaten.SkatteetatenPerioderRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class SkatteetatenControllerTest {

    @Test
    fun `erSkatteetatenPeriodeRequestGyldig skal fange opp ugyldig input` () {
        val skatteetatenController = SkatteetatenController(mockk())

        val skatteetatenPerioderRequest = SkatteetatenPerioderRequest(aar = "2020",
                                                                      identer = listOf("12345678910", "123"))

        val error = assertThrows(
            IllegalArgumentException::class.java
        ) { skatteetatenController.erSkatteetatenPeriodeRequestGyldig(skatteetatenPerioderRequest) }

        assertEquals(error.message, "Ikke et gyldig fødselsnummer: 123")
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

        val error = assertThrows(
            IllegalArgumentException::class.java
        ) { skatteetatenController.erSkatteetatenPeriodeRequestGyldig(skatteetatenPerioderRequest) }
        assertEquals(error.message, "For input string: \"sdsdf\"")
    }


    @Test
    fun `erSkatteetatenPeriodeRequestGyldig skal kaste IllegalArgumentException ved for stor liste` () {
        val skatteetatenController  = SkatteetatenController(mockk())
        val identer = mutableListOf<String>()
        for (i in 1..10000) {
            identer.add("12345678901")
        }
        skatteetatenController.erSkatteetatenPeriodeRequestGyldig(SkatteetatenPerioderRequest(aar = "2020",
                                                                                              identer = identer))

        identer.add("12345678901")
        val error = assertThrows(
            IllegalArgumentException::class.java
        ) { skatteetatenController.erSkatteetatenPeriodeRequestGyldig(SkatteetatenPerioderRequest(aar = "2020",
                                                                                                  identer = identer)) }
        assertEquals(error.message, "Maks antall identer er 10000")

    }
}
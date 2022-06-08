package no.nav.familie.ba.skatteetaten.rest

import no.nav.familie.ba.skatteetaten.IntegrationTest
import no.nav.familie.ba.skatteetaten.TokenTestUtil
import no.nav.familie.eksterne.kontrakter.skatteetaten.SkatteetatenPeriode
import no.nav.familie.eksterne.kontrakter.skatteetaten.SkatteetatenPerioder
import no.nav.familie.eksterne.kontrakter.skatteetaten.SkatteetatenPerioderRequest
import no.nav.familie.eksterne.kontrakter.skatteetaten.SkatteetatenPerioderResponse
import no.nav.familie.eksterne.kontrakter.skatteetaten.SkatteetatenPerson
import no.nav.familie.eksterne.kontrakter.skatteetaten.SkatteetatenPersonerResponse
import no.nav.familie.kontrakter.felles.Ressurs
import no.nav.familie.kontrakter.felles.objectMapper
import okhttp3.mockwebserver.MockResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.web.client.exchange
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.time.LocalDateTime

internal class SkatteetatenControllerIntegrationTest : IntegrationTest() {

    @BeforeEach
    internal fun setUp() {
        withClientToken()
    }

    @Test
    internal fun `har ingen token skal returnere 401`() {
        headers.clear()
        val response = finnPersoner<String>()

        assertThat(response.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
    }

    @Test
    internal fun `har feil scope i token skal returnere 401`() {
        withClientToken(TokenTestUtil.MaskinportenToken(scope = "feil-scope"))
        val response = finnPersoner<String>()

        assertThat(response.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
    }

    @Test
    internal fun `personer - skal returnere personer`() {
        val personer = defaultSkatteetatenPersonerResponse()
        enqueJson(Ressurs.success(personer))

        val response = finnPersoner<SkatteetatenPersonerResponse>()

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body!!).isEqualTo(personer)
    }

    @Test
    internal fun `personer - feil fra ba-sak`() {
        enqueJson(Ressurs.failure<String>(errorMessage = "Finner ikke person"))
        val response = finnPersoner<String>()

        assertThat(response.statusCode).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
        assertThat(response.body!!).contains("En feil har oppstått med callId ")
    }

    @Test
    internal fun `personer - kall uten år`() {
        val response = finnPersoner<String>(aar = null)

        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(response.body!!).contains("Required request parameter 'aar' for method parameter type int is not present")
    }

    @Test
    internal fun `personer med ugyldig år - skal returnere 500`() {
        val response = finnPersoner<String>(aar = 1000)

        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(response.body!!).contains("Ugyldig format, kan ikke være eldre enn 2020")
    }

    @Test
    internal fun `perioder - skal finne perioder til person`() {
        val perioder = defaultSkatteetatenPerioderResponse()
        enqueJson(Ressurs.success(perioder))

        val response = hentPerioder<SkatteetatenPerioderResponse>()

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body!!).isEqualTo(perioder)
    }

    @Test
    internal fun `perioder - ulgyldig fnr skal returnere 400`() {
        val response = hentPerioder<String>(ident = "1")

        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(response.body!!).contains("Ikke et gyldig fødselsnummer: 1")
    }

    @Test
    internal fun `personer - jsonparsing er riktig`() {
        enqueJson(readResource("requests/personer-mock-response.json"))

        val response = finnPersoner<String>()

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body!!).isEqualToIgnoringWhitespace(readResource("requests/personer-response-expected.json"))
    }

    @Test
    internal fun `perioder - jsonparsing er riktig`() {
        enqueJson(readResource("requests/perioder-mock-response.json"))

        val response = hentPerioder<String>()

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body!!).isEqualToIgnoringWhitespace(readResource("requests/perioder-response-expected.json"))
    }

    private fun readResource(path: String): String {
        return this::class.java.classLoader.getResource(path)!!.readText()
    }

    private fun enqueJson(obj: Any) {
        enqueJson(objectMapper.writeValueAsString(obj))
    }

    private fun enqueJson(body: String) {
        val response = MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(body)
        baSakMiremock.enqueue(response)
    }

    private fun defaultSkatteetatenPersonerResponse(): SkatteetatenPersonerResponse {
        val person = SkatteetatenPerson(ident = "2",
                                        sisteVedtakPaaIdent = LocalDateTime.now())
        return SkatteetatenPersonerResponse(listOf(person))
    }

    private fun defaultSkatteetatenPerioderResponse(): SkatteetatenPerioderResponse {
        val periode = SkatteetatenPeriode(fraMaaned = "2021-01-01",
                                          tomMaaned = "2021-01-01",
                                          delingsprosent = SkatteetatenPeriode.Delingsprosent._50)
        val perioder = SkatteetatenPerioder(ident = "0",
                                            sisteVedtakPaaIdent = LocalDateTime.now(),
                                            perioder = listOf(periode))
        return SkatteetatenPerioderResponse(listOf(perioder))
    }

    private inline fun <reified T : Any> finnPersoner(aar: Int? = ÅR): ResponseEntity<T> {
        val aarQueryParam = aar?.let { "aar=$it" }
        return restTemplate.exchange(localhost("/api/v1/personer?$aarQueryParam"),
                                     HttpMethod.GET,
                                     HttpEntity(null, headers))
    }

    private inline fun <reified T : Any> hentPerioder(ident: String = IDENT,
                                                      aar: Int = ÅR): ResponseEntity<T> {
        return restTemplate.exchange(localhost("/api/v1/perioder"),
                                     HttpMethod.POST,
                                     HttpEntity(SkatteetatenPerioderRequest(aar.toString(), listOf(ident)), headers))
    }

    companion object {

        private const val IDENT = "12345678901"
        private const val ÅR = 2020
    }
}

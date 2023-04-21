package no.nav.familie.ba.skatteetaten

import no.nav.familie.ba.skatteetaten.TokenTestUtil.MaskinportenToken
import no.nav.security.mock.oauth2.MockOAuth2Server
import no.nav.security.token.support.spring.test.EnableMockOAuth2Server
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpHeaders
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [DevLauncher::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(
    "integrationtest",
    "mock-oauth",
    "mock-ba-sak"
)
@EnableMockOAuth2Server
class IntegrationTest {

    @Autowired
    private lateinit var mockOAuth2Server: MockOAuth2Server

    @Autowired
    @Qualifier("mock-ba-sak")
    protected lateinit var baSakMiremock: MockWebServer

    @LocalServerPort
    private var port: Int? = 0

    @Value("\${MASKINPORTEN_WELL_KNOWN_URL}")
    private lateinit var maskinportenWellKnownUrl: String

    protected val restTemplate = TestRestTemplate()
    protected val headers = HttpHeaders()

    @AfterEach
    internal fun tearDown() {
        headers.clear()
    }

    protected fun localhost(uri: String): String {
        return "http://localhost:$port$uri"
    }

    protected fun withClientToken(clientToken: MaskinportenToken = MaskinportenToken()) {
        headers.setBearerAuth(TokenTestUtil.clientToken(mockOAuth2Server, clientToken, maskinportenWellKnownUrl))
    }
}

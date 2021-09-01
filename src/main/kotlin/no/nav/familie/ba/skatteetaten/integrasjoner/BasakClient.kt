package no.nav.familie.ba.skatteetaten.integrasjoner

import no.nav.familie.http.client.AbstractRestClient
import no.nav.familie.kontrakter.felles.Ressurs
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestClientResponseException
import org.springframework.web.client.RestOperations
import java.net.URI
import java.time.LocalDateTime

private val logger = LoggerFactory.getLogger(BasakClient::class.java)

@Component
class BasakClient @Autowired constructor(@param:Value("\${BA_SAK_API_URL}") private val sakServiceUri: String,
                                       @Qualifier("azure2") restOperations: RestOperations)
                                        : AbstractRestClient(restOperations, "skatt.sak") {




    fun hentPersoner(aar: String) {
        val uri = URI.create("$sakServiceUri/fagsaker/100000")
        val response: Ressurs<String> = getForEntity(uri)
    }

}

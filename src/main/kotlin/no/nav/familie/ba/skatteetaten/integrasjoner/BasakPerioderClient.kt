package no.nav.familie.ba.skatteetaten.integrasjoner

import no.nav.familie.eksterne.kontrakter.skatteetaten.SkatteetatenPerioderRequest
import no.nav.familie.eksterne.kontrakter.skatteetaten.SkatteetatenPerioderResponse
import no.nav.familie.http.client.AbstractRestClient
import no.nav.familie.kontrakter.felles.Ressurs
import no.nav.familie.kontrakter.felles.getDataOrThrow
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestOperations
import java.net.URI

private val logger = LoggerFactory.getLogger(BasakPerioderClient::class.java)

@Component
class BasakPerioderClient
    @Autowired
    constructor(
        @param:Value("\${BA_SAK_API_URL}") private val sakServiceUri: String,
        @Qualifier("azure-longtimeout") restOperations: RestOperations,
    ) : AbstractRestClient(restOperations, "skatt.perioder") {
        fun hentPerioder(request: SkatteetatenPerioderRequest): SkatteetatenPerioderResponse {
            val uri = URI.create("$sakServiceUri/skatt/perioder")
            val response: Ressurs<SkatteetatenPerioderResponse> = postForEntity(uri, request)
            if (response.status == Ressurs.Status.SUKSESS && response.data == null) error("Ressurs har status suksess, men mangler data")
            return response.getDataOrThrow()
        }
    }

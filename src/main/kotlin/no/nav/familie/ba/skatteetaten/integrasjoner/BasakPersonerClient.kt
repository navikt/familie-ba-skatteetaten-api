package no.nav.familie.ba.skatteetaten.integrasjoner

import no.nav.familie.eksterne.kontrakter.skatteetaten.SkatteetatenPersonerResponse
import no.nav.familie.http.client.AbstractRestClient
import no.nav.familie.kontrakter.felles.Ressurs
import no.nav.familie.kontrakter.felles.getDataOrThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestOperations
import java.net.URI


@Component
class BasakPersonerClient @Autowired constructor(
    @param:Value("\${BA_SAK_API_URL}") private val sakServiceUri: String,
    @Qualifier("azure-longtimeout") restOperations: RestOperations
) : AbstractRestClient(restOperations, "skatt.personer") {

    fun hentPersoner(aar: String): SkatteetatenPersonerResponse {
        val uri = URI.create("$sakServiceUri/skatt/personer?aar=$aar")
        val response: Ressurs<SkatteetatenPersonerResponse> = getForEntity(uri)
        if (response.status == Ressurs.Status.SUKSESS && response.data == null) error("Ressurs har status suksess, men mangler data")
        return response.getDataOrThrow()
    }
}

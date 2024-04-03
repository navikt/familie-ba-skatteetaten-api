package no.nav.familie.ba.skatteetaten

import no.nav.security.mock.oauth2.MockOAuth2Server
import java.util.UUID

object TokenTestUtil {
    fun clientToken(
        mockOAuth2Server: MockOAuth2Server,
        token: MaskinportenToken,
        maskinportenWellKnownUrl: String,
    ): String {
        val thisId = UUID.randomUUID().toString()

        val claims = mutableMapOf<String, Any>("scope" to token.scope)
        if (token.consumer != null) {
            claims["consumer"] = token.consumer
        }

        return mockOAuth2Server.issueToken(
            issuerId = maskinportenWellKnownUrl,
            subject = thisId,
            claims = claims,
        ).serialize()
    }

    data class MaskinportenToken(
        val scope: String = "nav:familie/v1/barnetrygd/utvidet",
        val issuer: String = "maskinporten",
        val consumer: Map<String, String>? =
            mapOf(
                "authority" to "iso6523-actorid-upis",
                "ID" to "ID123",
            ),
    )
}

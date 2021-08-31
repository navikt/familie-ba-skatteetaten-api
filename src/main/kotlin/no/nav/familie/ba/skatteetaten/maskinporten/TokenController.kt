package no.nav.familie.ba.skatteetaten.maskinporten

import org.springframework.context.annotation.Profile
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


/**
 * {
"scope": "nav:familie/v1/barnetrygd/utvidet",
"iss": "https://ver2.maskinporten.no/",
"client_amr": "private_key_jwt",
"token_type": "Bearer",
"exp": 1630406180,
"iat": 1630406060,
"client_id": "enclientid",
"jti": "qCK_osdWOsNsARuA-NHv-yWyEFxu8RWiviLaf8FbwPk",
"consumer": {
"authority": "iso6523-actorid-upis",
"ID": "0192:889640782"
}
}
 */
@RestController
@Profile("preprod")
class TokenController(val maskinportenClient: MaskinportenClient) {

    @GetMapping("/api/token") fun token(): String {
        return maskinportenClient.hentToken("nav:familie/v1/barnetrygd/utvidet")
    }
}
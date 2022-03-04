package no.nav.familie.ba.skatteetaten.maskinporten

import com.nimbusds.jose.JOSEException
import com.nimbusds.jose.JOSEObjectType
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.JWSSigner
import com.nimbusds.jose.crypto.RSASSASigner
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import no.nav.familie.eksterne.kontrakter.skatteetaten.SkatteetatenPerioderRequest
import no.nav.familie.eksterne.kontrakter.skatteetaten.SkatteetatenPerioderResponse
import no.nav.familie.eksterne.kontrakter.skatteetaten.SkatteetatenPersonerResponse
import no.nav.familie.kontrakter.felles.objectMapper
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate
import java.io.File
import java.time.Instant
import java.time.LocalDateTime
import java.util.*


fun main() {
    val jwkPrivate: String = System.getenv("MASKINPORTEN_CLIENT_JWK")
    assert(jwkPrivate.isNotBlank())
    val clientId: String = System.getenv("MASKINPORTEN_CLIENT_ID")
    assert(clientId.isNotBlank())

    val token = MaskinportenClient().hentToken("nav:familie/v1/barnetrygd/utvidet", jwkPrivate, clientId)

    println(token)

    val jsonNodes = objectMapper.readTree(token)
    val restTemplate = RestTemplate()
    val httpHeaders = HttpHeaders()
    httpHeaders.accept = listOf(MediaType.APPLICATION_JSON)
    httpHeaders.setBearerAuth(jsonNodes.get("access_token").asText())

    val entity: HttpEntity<*> = HttpEntity<Any?>(httpHeaders)

    val responsePersoner = restTemplate.exchange(
        "https://familie-ba-skatteetaten-api.ekstern.dev.nav.no/api/v1/personer?aar=2021",
        HttpMethod.GET,
        entity,
        SkatteetatenPersonerResponse::class.java
    )

    val første1000identer = responsePersoner.body?.brukere?.take(1000)?.map { it.ident }!!
    println(objectMapper.writeValueAsString(første1000identer))


    val entityPerioder: HttpEntity<*> = HttpEntity<Any?>(SkatteetatenPerioderRequest("2021",første1000identer), httpHeaders)
    val responsePerioder = restTemplate.exchange(
            "https://familie-ba-skatteetaten-api.ekstern.dev.nav.no/api/v1/perioder",
            HttpMethod.POST,
            entityPerioder,
            SkatteetatenPerioderResponse::class.java)

    val f = File.createTempFile("${LocalDateTime.now()}-skatt-perioder", ".tmp")
    f.writeText(objectMapper.writeValueAsString(responsePerioder.body))
    println("File created ${f.absolutePath}")

}


class MaskinportenClient {

    private val SCOPE = "scope"
    private val GRANT_TYPE_VALUE = "urn:ietf:params:oauth:grant-type:jwt-bearer"
    private val AUD = "https://ver2.maskinporten.no/"
    private val TOKEN_ENDPOINT = "$AUD/token"

    private val restTemplate = RestTemplate()


    private fun createSignedJWT(rsaJwk: RSAKey, claimsSet: JWTClaimsSet?): SignedJWT {
        return try {
            val header = JWSHeader.Builder(JWSAlgorithm.RS256)
                .keyID(rsaJwk.keyID)
                .type(JOSEObjectType.JWT)
            val signedJWT = SignedJWT(header.build(), claimsSet)
            val signer: JWSSigner = RSASSASigner(rsaJwk.toPrivateKey())
            signedJWT.sign(signer)
            signedJWT
        } catch (e: JOSEException) {
            throw RuntimeException(e)
        }
    }


    fun hentToken(scope: String, jwkPrivate: String, clientId: String): String {
        val rsaKey = RSAKey.parse(jwkPrivate)
        val time = Instant.now()
        val jwtClaimsSet = JWTClaimsSet.Builder()
            .audience(AUD)
            .issuer(clientId)
            .issueTime(Date.from(time))
            .expirationTime(Date.from(time.plusSeconds(120)))
            .claim(SCOPE, scope)
            .build()
        val signedJWT = createSignedJWT(rsaKey, jwtClaimsSet)
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
        val requestBody = LinkedMultiValueMap<Any, Any>()
        requestBody.add("grant_type", GRANT_TYPE_VALUE)
        requestBody.add("assertion", signedJWT.serialize())
        val httpEntity = HttpEntity(requestBody, headers)
        val json = restTemplate.exchange(
            TOKEN_ENDPOINT,
            HttpMethod.POST,
            httpEntity,
            String::class.java
        ).body!!

        return json
    }
}
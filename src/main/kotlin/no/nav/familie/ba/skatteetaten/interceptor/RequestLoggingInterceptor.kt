package no.nav.familie.ba.skatteetaten.interceptor

import no.nav.security.token.support.core.jwt.JwtToken
import no.nav.security.token.support.core.jwt.JwtTokenClaims
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.servlet.AsyncHandlerInterceptor
import java.io.IOException
import java.nio.charset.Charset
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class RequestLoggingInterceptor: AsyncHandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val infoFraToken = hentInfoFraToken(request)

        LOG.info("[pre-handle] $infoFraToken - ${request.method}: ${request.requestURI}")
        return super.preHandle(request, response, handler)
    }

    override fun afterCompletion(request: HttpServletRequest,
                            response: HttpServletResponse,
                            handler: Any,
                            ex: Exception?) {

        val infoFraToken = hentInfoFraToken(request)

        val melding = "[post-handle] $infoFraToken - ${request.method}: ${request.requestURI} (${response.status})"

        if (HttpStatus.valueOf(response.status).isError) {
            LOG.warn(melding)
        } else {
            LOG.info(melding)
        }
        super.afterCompletion(request, response, handler, ex)
    }

    private fun hentInfoFraToken(request: HttpServletRequest): String {
        val jwtClaims = hentClaims(request)

        val clientId = jwtClaims?.get("client_id")
        val scope = jwtClaims?.get("scope")
        val consumerId = (jwtClaims?.get("consumer") as? Map<String, String>)?.get("ID")
        val issuer = jwtClaims?.issuer

        val tokenData = "$issuer $clientId $scope $consumerId"
        return tokenData
    }

    private fun hentClaims(request: HttpServletRequest): JwtTokenClaims? {
        val authorizationHeader = request.getHeader("Authorization")
        val token = if (authorizationHeader != null && authorizationHeader.startsWith("Bearer "))
            authorizationHeader.substring(7) else null

        if (token == null) {
            return null
        } else {
            return JwtToken(token).jwtTokenClaims
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(this::class.java)
    }
}
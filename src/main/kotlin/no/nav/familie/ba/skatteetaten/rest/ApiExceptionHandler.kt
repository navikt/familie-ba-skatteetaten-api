package no.nav.familie.ba.skatteetaten.rest

import no.nav.familie.log.IdUtils
import no.nav.familie.log.mdc.MDCConstants
import no.nav.security.token.support.core.exceptions.JwtTokenMissingException
import no.nav.security.token.support.spring.validation.interceptor.JwtTokenUnauthorizedException
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.core.NestedExceptionUtils
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageConversionException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.DateTimeException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.validation.ConstraintViolationException

@ControllerAdvice
class ApiExceptionHandler {

    private val logger = LoggerFactory.getLogger(ApiExceptionHandler::class.java)
    private val secureLogger = LoggerFactory.getLogger("secureLogger")

    @ExceptionHandler(Throwable::class)
    fun handleThrowable(throwable: Throwable, response: HttpServletResponse, request: HttpServletRequest) {
        val callId = MDC.get(MDCConstants.MDC_CALL_ID) ?: IdUtils.generateId()
        val mostSpecificThrowable = NestedExceptionUtils.getMostSpecificCause(throwable)
        val className = if (mostSpecificThrowable != null) "[${mostSpecificThrowable::class.java.name}] " else ""
        secureLogger.error("$className En feil har oppstått: ${mostSpecificThrowable.message}", throwable)
        logger.error("$className En feil har oppstått: ${mostSpecificThrowable.message}")

        response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "En feil har oppstått med callId $callId")
    }

    @ExceptionHandler(value = [HttpMessageConversionException::class, HttpMediaTypeNotSupportedException::class])
    fun onSafeHttpExceptions(ex: HttpMessageConversionException, response: HttpServletResponse, request: HttpServletRequest) {
        val callId = MDC.get(MDCConstants.MDC_CALL_ID) ?: IdUtils.generateId()
        val mostSpecificThrowable = NestedExceptionUtils.getMostSpecificCause(ex)
        val className = if (mostSpecificThrowable != null) "[${mostSpecificThrowable::class.java.name}] " else ""
        secureLogger.error("$className En feil har oppstått: ${mostSpecificThrowable.message}", ex)
        logger.error("$className En feil har oppstått: ${mostSpecificThrowable.message}")

        response.sendError(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "En feil har oppstått med callId $callId message:${mostSpecificThrowable.message}"
        )
    }

    @ExceptionHandler(value = [JwtTokenMissingException::class, JwtTokenUnauthorizedException::class])
    fun onJwtTokenException(ex: RuntimeException, response: HttpServletResponse, request: HttpServletRequest) {
        val callId = MDC.get(MDCConstants.MDC_CALL_ID) ?: IdUtils.generateId()
        val mostSpecificThrowable = NestedExceptionUtils.getMostSpecificCause(ex)
        val className = if (mostSpecificThrowable != null) "[${mostSpecificThrowable::class.java.name}] " else ""

        logger.error("Ugyldig eller mangler JWT token", ex)
        response.sendError(
            HttpStatus.UNAUTHORIZED.value(),
            "$className Ugyldig eller mangler JWT token på $callId"
        )
    }

    @ExceptionHandler(value = [MissingServletRequestParameterException::class, IllegalArgumentException::class, ConstraintViolationException::class, DateTimeException::class])
    fun onConstraintViolation(ex: Exception, response: HttpServletResponse) {
        logger.warn("Valideringsfeil av request. Se securelog for detaljer")
        secureLogger.warn("Valideringsfeil av request", ex)

        response.sendError(HttpStatus.BAD_REQUEST.value(), ex.message)
    }
}

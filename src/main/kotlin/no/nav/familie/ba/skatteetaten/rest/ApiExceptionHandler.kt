package no.nav.familie.ba.skatteetaten.rest

import no.nav.familie.log.IdUtils
import no.nav.familie.log.mdc.MDCConstants
import no.nav.security.token.support.spring.validation.interceptor.JwtTokenUnauthorizedException
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.core.NestedExceptionUtils
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageConversionException
import org.springframework.web.HttpMediaTypeException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


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

        response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "En feil har oppstått med callId ${callId}")
    }

    @ExceptionHandler(value = [HttpMessageConversionException::class, HttpMediaTypeNotSupportedException::class])
    fun onSafeHttpExceptions(ex: HttpMessageConversionException, response: HttpServletResponse, request: HttpServletRequest) {
        val callId = MDC.get(MDCConstants.MDC_CALL_ID) ?: IdUtils.generateId()
        val mostSpecificThrowable = NestedExceptionUtils.getMostSpecificCause(ex)
        val className = if (mostSpecificThrowable != null) "[${mostSpecificThrowable::class.java.name}] " else ""
        secureLogger.error("$className En feil har oppstått: ${mostSpecificThrowable.message}", ex)
        logger.error("$className En feil har oppstått: ${mostSpecificThrowable.message}")

        response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "En feil har oppstått med callId ${callId} message:${mostSpecificThrowable.message}")
    }

    @ExceptionHandler(value = [JwtTokenUnauthorizedException::class])
    fun onJwtTokenUnathorizedException(ex: JwtTokenUnauthorizedException, response: HttpServletResponse, request: HttpServletRequest) {
        val token = request.getHeader("Authorization")
        logger.warn("Unauthorized ${request}", ex)
        secureLogger.warn("Unauthorized: ${request} $token ", ex)
        throw ex
    }

    @ExceptionHandler(value = [MissingServletRequestParameterException::class])
    fun onConstraintViolation(ex: MissingServletRequestParameterException, response: HttpServletResponse) {
        logger.warn("Valideringsfeil av request. Se securelog for detaljer")
        secureLogger.warn("Valideringsfeil av request", ex)

        response.sendError(HttpStatus.BAD_REQUEST.value(), ex.message)
    }
}

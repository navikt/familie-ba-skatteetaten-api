package no.nav.familie.ba.skatteetaten.config

import no.nav.familie.http.interceptor.BearerTokenClientInterceptor
import no.nav.familie.http.interceptor.ConsumerIdClientInterceptor
import no.nav.familie.http.interceptor.InternLoggerInterceptor
import no.nav.familie.http.interceptor.MdcValuesPropagatingClientInterceptor
import no.nav.familie.log.filter.LogFilter
import no.nav.security.token.support.client.core.http.OAuth2HttpClient
import no.nav.security.token.support.client.spring.oauth2.DefaultOAuth2HttpClient
import no.nav.security.token.support.client.spring.oauth2.EnableOAuth2Client
import no.nav.security.token.support.spring.api.EnableJwtTokenValidation
import org.slf4j.LoggerFactory
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.boot.web.servlet.server.ServletWebServerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.client.RestOperations
import java.time.Duration
import java.time.temporal.ChronoUnit

@SpringBootConfiguration
@ConfigurationPropertiesScan("no.nav.familie")
@ComponentScan("no.nav.familie.ba")
@EnableJwtTokenValidation(ignore = ["org.springframework", "springfox.documentation.swagger"])
@EnableOAuth2Client
@Import(ConsumerIdClientInterceptor::class,
        InternLoggerInterceptor::class,
        BearerTokenClientInterceptor::class)
@EnableScheduling
class ApplicationConfig {

    @Bean
    fun servletWebServerFactory(): ServletWebServerFactory {
        val serverFactory = JettyServletWebServerFactory()
        serverFactory.port = 8096
        return serverFactory
    }

    @Bean
    fun logFilter(): FilterRegistrationBean<LogFilter> {
        log.info("Registering LogFilter filter")
        val filterRegistration: FilterRegistrationBean<LogFilter> = FilterRegistrationBean()
        filterRegistration.filter = LogFilter()
        filterRegistration.order = 1
        return filterRegistration
    }

    @Bean
    fun securityHeaderFilter(): FilterRegistrationBean<SecurityHeaderFilter> {
        log.info("Registering SecurityHeaderFilter filter")
        val filterRegistration: FilterRegistrationBean<SecurityHeaderFilter> = FilterRegistrationBean()
        filterRegistration.filter = SecurityHeaderFilter()
        filterRegistration.order = 2
        return filterRegistration
    }


    @Bean("azure-longtimeout")
    fun restTemplateJwtBearer(
        consumerIdClientInterceptor: ConsumerIdClientInterceptor,
        internLoggerInterceptor: InternLoggerInterceptor,
        bearerTokenClientInterceptor: BearerTokenClientInterceptor
    ): RestOperations {
        return RestTemplateBuilder()
            .setReadTimeout(READ_CONNECTION_LONG_TIMEOUT)
            .setConnectTimeout(READ_CONNECTION_LONG_TIMEOUT)
            .additionalInterceptors(
                consumerIdClientInterceptor,
                bearerTokenClientInterceptor,
                MdcValuesPropagatingClientInterceptor()
            ).build()
    }


    /**
     * Overskrever OAuth2HttpClient som settes opp i token-support som ikke kan få med objectMapper fra felles
     * pga .setVisibility(PropertyAccessor.SETTER, JsonAutoDetect.Visibility.NONE)
     * og [OAuth2AccessTokenResponse] som burde settes med setters, då feltnavn heter noe annet enn feltet i json
     */
    @Bean
    @Primary
    fun oAuth2HttpClient(): OAuth2HttpClient {
        return DefaultOAuth2HttpClient(
            RestTemplateBuilder()
                .setConnectTimeout(Duration.of(2, ChronoUnit.SECONDS))
                .setReadTimeout(Duration.of(4, ChronoUnit.SECONDS))
        )
    }

    companion object {
        private val log = LoggerFactory.getLogger(ApplicationConfig::class.java)
        private val READ_CONNECTION_LONG_TIMEOUT = Duration.of(10, ChronoUnit.MINUTES)
    }
}

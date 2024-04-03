package no.nav.familie.ba.skatteetaten.config

import no.nav.familie.http.interceptor.BearerTokenClientInterceptor
import no.nav.familie.http.interceptor.ConsumerIdClientInterceptor
import no.nav.familie.http.interceptor.InternLoggerInterceptor
import no.nav.familie.http.interceptor.MdcValuesPropagatingClientInterceptor
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.web.client.RestOperations
import java.time.Duration
import java.time.temporal.ChronoUnit

@Configuration
@Profile("integrationtest")
class IntegrasjonConfig {
    /**
     * Overstyrer read og connect timeout
     */
    @Bean("azure-longtimeout")
    @Primary
    fun restTemplateJwtBearer(
        consumerIdClientInterceptor: ConsumerIdClientInterceptor,
        internLoggerInterceptor: InternLoggerInterceptor,
        bearerTokenClientInterceptor: BearerTokenClientInterceptor,
    ): RestOperations {
        return RestTemplateBuilder()
            .setReadTimeout(Duration.of(10, ChronoUnit.SECONDS)) // overstyrt
            .setConnectTimeout(Duration.of(10, ChronoUnit.SECONDS)) // overstyrt
            .additionalInterceptors(
                consumerIdClientInterceptor,
                bearerTokenClientInterceptor,
                MdcValuesPropagatingClientInterceptor(),
            ).build()
    }
}

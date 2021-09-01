package no.nav.familie.ba.skatteetaten.config

import com.fasterxml.jackson.databind.ObjectMapper
import no.nav.familie.http.config.NaisProxyCustomizer
import no.nav.familie.http.config.RestTemplateSts
import no.nav.familie.http.interceptor.BearerTokenClientInterceptor
import no.nav.familie.http.interceptor.ConsumerIdClientInterceptor
import no.nav.familie.http.interceptor.InternLoggerInterceptor
import no.nav.familie.http.interceptor.MdcValuesPropagatingClientInterceptor
import no.nav.familie.kontrakter.felles.objectMapper
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.*
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestOperations
import org.springframework.web.client.RestTemplate
import java.time.Duration
import java.time.temporal.ChronoUnit

@Configuration
@Import(BearerTokenClientInterceptor::class,
        MdcValuesPropagatingClientInterceptor::class,
        ConsumerIdClientInterceptor::class,)
class RestTemplateConfig {


    @Bean("azure2")
    fun restTemplateJwtBearer(consumerIdClientInterceptor: ConsumerIdClientInterceptor,
                              bearerTokenClientInterceptor: BearerTokenClientInterceptor): RestOperations {
        val restTemplateBuilder = RestTemplateBuilder()
        return restTemplateBuilder.additionalInterceptors(consumerIdClientInterceptor,
                                                          bearerTokenClientInterceptor,
                                                          MdcValuesPropagatingClientInterceptor()).build()
    }
}

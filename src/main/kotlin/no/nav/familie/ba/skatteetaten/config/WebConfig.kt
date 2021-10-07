package no.nav.familie.ba.skatteetaten.config

import no.nav.familie.ba.skatteetaten.interceptor.MaskinportenTokenLoggingInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(MaskinportenTokenLoggingInterceptor())
        super.addInterceptors(registry)
    }
}
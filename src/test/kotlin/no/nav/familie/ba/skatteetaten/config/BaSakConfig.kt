package no.nav.familie.ba.skatteetaten.config

import okhttp3.mockwebserver.MockWebServer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import java.net.InetAddress

@Configuration
class BaSakConfig {
    @Bean("mock-ba-sak")
    @Profile("mock-ba-sak")
    fun integrationMockServer(): MockWebServer {
        val server = MockWebServer()
        server.start(InetAddress.getByName("localhost"), 8385)
        return server
    }
}

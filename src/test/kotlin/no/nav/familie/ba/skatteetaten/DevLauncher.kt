package no.nav.familie.ba.skatteetaten

import no.nav.familie.ba.skatteetaten.config.ApplicationConfig
import no.nav.familie.ba.skatteetaten.config.RestTemplateConfig
import no.nav.security.token.support.test.spring.TokenGeneratorConfiguration
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Import


@Import(ApplicationConfig::class, RestTemplateConfig::class)
class DevLauncher


fun main(args: Array<String>) {
    System.setProperty("spring.profiles.active", "dev")
    val springApp = SpringApplication(DevLauncher::class.java)
    springApp.setAdditionalProfiles("dev")
    springApp.run(*args)
}
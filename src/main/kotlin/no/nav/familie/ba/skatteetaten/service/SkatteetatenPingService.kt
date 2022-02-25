package no.nav.familie.ba.skatteetaten.service


import no.nav.familie.ba.skatteetaten.integrasjoner.BasakPerioderClient
import no.nav.familie.ba.skatteetaten.integrasjoner.BasakPersonerClient
import no.nav.familie.eksterne.kontrakter.skatteetaten.SkatteetatenPerioderRequest
import no.nav.familie.eksterne.kontrakter.skatteetaten.SkatteetatenPerioderResponse
import no.nav.familie.eksterne.kontrakter.skatteetaten.SkatteetatenPersonerResponse
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.Year

@Service
@Profile("prod|preprod")
class SkatteetatenPingService(val basakPerioderClient: BasakPerioderClient) {

    private val logger = LoggerFactory.getLogger(SkatteetatenPingService::class.java)

    @Scheduled(fixedRate=60*60*1000)
    fun finnPersonerMedUtvidetBarnetrygd() {
        try {
            basakPerioderClient.hentPerioder(SkatteetatenPerioderRequest(Year.now().toString(), listOf("12345678901")))
            logger.info("Ping mot basak OK")
        } catch (e: Exception) {
           logger.error("Feil med oppkobling mot ba-sak", e)
        }
    }
}
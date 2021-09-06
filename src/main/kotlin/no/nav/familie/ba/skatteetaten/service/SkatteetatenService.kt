package no.nav.familie.ba.skatteetaten.service



import no.nav.familie.ba.skatteetaten.integrasjoner.BasakClient
import no.nav.familie.ba.skatteetaten.model.Periode
import no.nav.familie.ba.skatteetaten.model.Perioder
import no.nav.familie.ba.skatteetaten.model.PerioderRequest
import no.nav.familie.ba.skatteetaten.model.PerioderResponse
import no.nav.familie.ba.skatteetaten.model.Person
import no.nav.familie.ba.skatteetaten.model.PersonerResponse
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.OffsetDateTime

@Service
class SkatteetatenService(val basakClient: BasakClient) {

    fun hentPerioderMedUtvidetBarnetrygd(
        perioderRequest: PerioderRequest
    ): PerioderResponse {
        return basakClient.hentPerioder(perioderRequest)
    }


    @Scheduled(fixedDelay = 600000)
    fun triggerbasak() {
        basakClient.hentPersoner("2021")
        basakClient.hentPerioder(PerioderRequest("2021", listOf("123")))
    }

    fun finnPersonerMedUtvidetBarnetrygd(aar: String): PersonerResponse {
        return basakClient.hentPersoner(aar)
    }
}
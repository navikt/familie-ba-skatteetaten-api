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
        return PerioderResponse(listOf(Perioder("01017000110", OffsetDateTime.now(), perioder = listOf(Periode("2020-02", Periode.MaxDelingsprosent._50, tomMaaned = "2022-12")))))
    }


    @Scheduled(fixedDelay = 600000)
    fun triggerbasak() {
        basakClient.hentPersoner("2021")
    }

    fun finnPersonerMedUtvidetBarnetrygd(aar: String): PersonerResponse {
        basakClient.hentPersoner(aar)
        return PersonerResponse(listOf(Person("12345678901", OffsetDateTime.now())))
    }
}
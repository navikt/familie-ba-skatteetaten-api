package no.nav.familie.ba.skatteetaten.service



import no.nav.familie.ba.skatteetaten.model.Periode
import no.nav.familie.ba.skatteetaten.model.Perioder
import no.nav.familie.ba.skatteetaten.model.PerioderRequest
import no.nav.familie.ba.skatteetaten.model.PerioderResponse
import no.nav.familie.ba.skatteetaten.model.Person
import no.nav.familie.ba.skatteetaten.model.PersonerResponse
import org.springframework.stereotype.Service
import java.time.OffsetDateTime

@Service
class SkatteetatenService {

    fun hentPerioderMedUtvidetBarnetrygd(
        perioderRequest: PerioderRequest
    ): PerioderResponse {
        return PerioderResponse(listOf(Perioder("01017000110", OffsetDateTime.now(), perioder = listOf(Periode("2020-02", Periode.MaxDelingsprosent._50, tomMaaned = "2022-12")))))
    }


    fun finnPersonerMedUtvidetBarnetrygd(aar: String): PersonerResponse {
        return PersonerResponse(listOf(Person("12345678901", OffsetDateTime.now())))
    }
}
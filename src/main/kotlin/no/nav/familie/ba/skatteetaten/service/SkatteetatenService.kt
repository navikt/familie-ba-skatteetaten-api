package no.nav.familie.ba.skatteetaten.service



import no.nav.familie.ba.skatteetaten.integrasjoner.BasakClient
import no.nav.familie.ba.skatteetaten.model.PerioderRequest
import no.nav.familie.ba.skatteetaten.model.PerioderResponse
import no.nav.familie.ba.skatteetaten.model.PersonerResponse
import org.springframework.stereotype.Service

@Service
class SkatteetatenService(val basakClient: BasakClient) {

    fun hentPerioderMedUtvidetBarnetrygd(
        perioderRequest: PerioderRequest
    ): PerioderResponse {
        return basakClient.hentPerioder(perioderRequest)
    }

    fun finnPersonerMedUtvidetBarnetrygd(aar: String): PersonerResponse {
        return basakClient.hentPersoner(aar)
    }
}
package no.nav.familie.ba.skatteetaten.service



import no.nav.familie.ba.skatteetaten.integrasjoner.BasakClient
import no.nav.familie.eksterne.kontrakter.skatteetaten.SkatteetatenPerioderRequest
import no.nav.familie.eksterne.kontrakter.skatteetaten.SkatteetatenPerioderResponse
import no.nav.familie.eksterne.kontrakter.skatteetaten.SkatteetatenPersonerResponse
import org.springframework.stereotype.Service

@Service
class SkatteetatenService(val basakClient: BasakClient) {

    fun hentPerioderMedUtvidetBarnetrygd(
        perioderRequest: SkatteetatenPerioderRequest
    ): SkatteetatenPerioderResponse {
        return basakClient.hentPerioder(perioderRequest)
    }

    fun finnPersonerMedUtvidetBarnetrygd(aar: String): SkatteetatenPersonerResponse {
        return basakClient.hentPersoner(aar)
    }
}
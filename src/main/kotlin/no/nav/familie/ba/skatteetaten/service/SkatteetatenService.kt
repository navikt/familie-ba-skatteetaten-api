package no.nav.familie.ba.skatteetaten.service


import no.nav.familie.ba.skatteetaten.integrasjoner.BasakPerioderClient
import no.nav.familie.ba.skatteetaten.integrasjoner.BasakPersonerClient
import no.nav.familie.eksterne.kontrakter.skatteetaten.SkatteetatenPerioderRequest
import no.nav.familie.eksterne.kontrakter.skatteetaten.SkatteetatenPerioderResponse
import no.nav.familie.eksterne.kontrakter.skatteetaten.SkatteetatenPersonerResponse
import org.springframework.stereotype.Service

@Service
class SkatteetatenService(val basakPersonerClient: BasakPersonerClient, val basakPerioderClient: BasakPerioderClient) {

    fun hentPerioderMedUtvidetBarnetrygd(
        perioderRequest: SkatteetatenPerioderRequest
    ): SkatteetatenPerioderResponse {
        return basakPerioderClient.hentPerioder(perioderRequest)
    }

    fun finnPersonerMedUtvidetBarnetrygd(aar: String): SkatteetatenPersonerResponse {
        return basakPersonerClient.hentPersoner(aar)
    }
}
package no.nav.familie.ba.skatteetaten.integrasjoner

import no.nav.familie.kontrakter.felles.Ressurs
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

inline fun <reified T> assertGenerelleSuksessKriterier(it: Ressurs<T>?) {
    val status = it?.status ?: error("Finner ikke ressurs")
    if (status == Ressurs.Status.SUKSESS && it.data == null) error("Ressurs har status suksess, men mangler data")
}

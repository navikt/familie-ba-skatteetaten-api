package no.nav.familie.ba.skatteetaten

import no.nav.familie.eksterne.kontrakter.skatteetaten.SkatteetatenPersonerResponse
import no.nav.familie.kontrakter.felles.objectMapper
import org.springframework.util.StopWatch
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import kotlin.io.path.Path

/**
 * Sammenligner 2 uttrekk av personer-kallet.
 */
fun main() {
    val personer =
        objectMapper.readValue(
            Path("./person-før.json").toFile(),
            SkatteetatenPersonerResponse::class.java,
        ).brukere.map { it.ident }.sorted()
    val personerEtter =
        objectMapper.readValue(
            Path("./person-etter.json").toFile(),
            SkatteetatenPersonerResponse::class.java,
        ).brukere.map { it.ident }.sorted()
    val stopWatch = StopWatch()
    stopWatch.start()
    val diffFørEtter = personer.minus(personerEtter.toHashSet())
    val diffEtterFør = personerEtter.minus(personer.toHashSet())
    stopWatch.stop()
    println("Finnes i person-før, men ikke i etter: $diffFørEtter")
    println("Finnes i person-etter, men ikke i før: $diffEtterFør")
    println("Tid: ${stopWatch.totalTimeSeconds}s")
}

private fun lesFil(filnavn: String): String {
    return Files.readString(
        Path(filnavn),
        StandardCharsets.UTF_8,
    )
}

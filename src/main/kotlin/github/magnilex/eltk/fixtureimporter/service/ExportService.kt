package github.magnilex.eltk.fixtureimporter.service

import github.magnilex.eltk.fixtureimporter.CalendarEventCsvRow
import java.io.File

fun exportToCsv(fixtures: Fixtures, player: String, filename: String): Int {
    File(filename).printWriter().use { out ->
        val fixturesForPlayer = fixtures.fixtures.filter { it.hasPlayer(player) }
        listOf(CalendarEventCsvRow.header()).plus(fixturesForPlayer
                .map { it -> CalendarEventCsvRow(it).toRow() })
                .forEach(out::println)
        return fixturesForPlayer.size
    }
}
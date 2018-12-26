package github.magnilex.eltk.fixtureimporter

import github.magnilex.eltk.fixtureimporter.service.Fixture
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

data class CalendarEventCsvRow(
        val subject: String,
        val startDate: String,
        val startTime: String,
        val endDate: String,
        val endTime: String,
        val allDayEvent: String,
        val description: String,
        val location: String,
        val private: String) {

    fun toRow(): String = listOf(subject, startDate, startTime, endDate, endTime, allDayEvent, description, location, private).joinToString(",")

    constructor(fixture: Fixture) : this(
            toDescription(fixture),
            toDate(fixture.date),
            toTime(fixture.date),
            toDate(getEndDate(fixture.date)),
            toTime(getEndDate(fixture.date)),
            "False",
            toDescription(fixture),
            "Enskede Rackethall, Sockenvägen 290, 120 40 Årsta",
            "True"
    )

    companion object {
        val DATE_FORMAT = DateTimeFormatter.ofPattern("MM/dd/YYYY", Locale.US)
        val TIME_FORMAT = DateTimeFormatter.ofPattern("hh:mm a", Locale.US)

        fun header(): String = "Subject,Start Date,Start Time,End Date,End Time,All Day Event,Description,Location,Private"

        private fun toDescription(fixture: Fixture) = "Match mellan ${fixture.player1} och ${fixture.player2} på bana ${fixture.court}"

        private fun toDate(date: LocalDateTime): String {
            return DATE_FORMAT.format(date)
        }

        private fun toTime(date: LocalDateTime): String {
            return TIME_FORMAT.format(date)
        }

        private fun getEndDate(date: LocalDateTime): LocalDateTime {
            return date.plusHours(1)
        }

    }
}


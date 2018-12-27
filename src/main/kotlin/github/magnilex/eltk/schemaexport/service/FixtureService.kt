package github.magnilex.eltk.schemaexport.service

import org.jsoup.Jsoup.connect
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

val baseHtml = "http://eltk.se/show_page.php?page=senior_gr_5137&operation=oth_grp"
val groupHtml = "http://eltk.se/show_page.php?page=senior_gr_5137&action=save&operation=cntrl&kategori=gruppspel_8079&operation=oth_grp&group_id="

fun getGroups(): List<Group> = connect(baseHtml).get()
        .getElementById("sel_id").child(0).children()
        .map { Group(it.`val`(), it.text()) }

fun getFixtures(group: Group): Fixtures = Fixtures(parseFixtures(connect("$groupHtml${group.id}").get()))

private fun parseFixtures(fixturesHtml: Document): List<Fixture> {
    val fixtureElements = fixturesHtml.select("td.pgcell")
    if (fixtureElements.size.rem(4) != 0) {
        throw IllegalArgumentException("Felaktigt format på HTML")
    }
    return fixtureElements.chunked(4).map { parseFixture(it) }
}

private fun parseFixture(elements: List<Element>): Fixture =
        Fixture(parsePlayer(elements[0]), parsePlayer(elements[1]), parseDateTime(elements[2]), elements[3].text())

private fun parseDateTime(element: Element): LocalDateTime {
    val aElement = element.selectFirst("a")
    val dateTimeString = "${aElement.attr("href").substringAfterLast("=").substringBefore("-")} ${aElement.text()}"
    return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("yyyy dd MMM HH:mm", Locale.US))
}

private fun parsePlayer(element: Element): String = element.selectFirst("a").text()

class Group(val id: String, val name: String) {
    override fun toString(): String = name
}

class Fixtures(val fixtures: List<Fixture>) {
    fun getPlayers(): Set<String> = fixtures.flatMap { listOf(it.player1, it.player2) }.toSortedSet()

    constructor() : this(emptyList())
}

class Fixture(val player1: String, val player2: String, val date: LocalDateTime, val court: String) {
    fun hasPlayer(player: String): Boolean = player1 == player || player2 == player
}
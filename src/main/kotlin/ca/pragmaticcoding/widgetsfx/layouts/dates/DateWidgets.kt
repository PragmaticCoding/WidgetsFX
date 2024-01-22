@file:Suppress("unused")

package ca.pragmaticcoding.widgetsfx.layouts.dates

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
private val jsonFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

fun formatDate(dateToFormat: LocalDate): String {
    return dateToFormat.format(formatter)
}

fun dateFromJson(jsonDateTime: String): LocalDate {
    return LocalDateTime.parse(jsonDateTime, DateTimeFormatter.ISO_INSTANT.withZone(ZoneOffset.UTC)).toLocalDate()
}


fun LocalDate.formatted(): String = formatDate(this)
fun String.fromJsonDate(): LocalDate = dateFromJson(this)

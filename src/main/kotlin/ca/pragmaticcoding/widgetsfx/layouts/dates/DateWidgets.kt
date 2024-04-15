@file:Suppress("unused")

package ca.pragmaticcoding.widgetsfx.layouts.dates

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
private val jsonFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

/**
 * Format a date according to the standard, application-wide date format
 * @param dateToFormat The date to format
 */
fun formatDate(dateToFormat: LocalDate): String {
    return dateToFormat.format(formatter)
}

/**
 * Convert a date from the standard JSON format to [LocalDate]
 * @param jsonDateTime The date & time in standard JSON format
 */
fun dateFromJson(jsonDateTime: String): LocalDate {
    return LocalDateTime.parse(jsonDateTime, DateTimeFormatter.ISO_INSTANT.withZone(ZoneOffset.UTC)).toLocalDate()
}

/**
 * Extension function to convert a [LocalDate] to a String via [formatDate]
 */
fun LocalDate.formatted(): String = formatDate(this)

/**
 * Extension function to convert a String in standord JSON date/time format to a [LocalDate]
 */
fun String.fromJsonDate(): LocalDate = dateFromJson(this)

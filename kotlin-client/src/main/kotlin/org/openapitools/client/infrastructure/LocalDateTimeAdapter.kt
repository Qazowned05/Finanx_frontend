package org.openapitools.client.infrastructure

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

class LocalDateTimeAdapter {
    private val formatter = DateTimeFormatterBuilder()
        .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
        .optionalStart()
        .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
        .optionalEnd()
        .toFormatter()

    @ToJson
    fun toJson(value: LocalDateTime): String {
        return DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(value)
    }

    @FromJson
    fun fromJson(value: String): LocalDateTime {
        return LocalDateTime.parse(value, formatter)
    }
}

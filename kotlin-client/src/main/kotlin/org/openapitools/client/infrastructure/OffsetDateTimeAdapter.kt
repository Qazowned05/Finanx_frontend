package org.openapitools.client.infrastructure

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class OffsetDateTimeAdapter {
    @ToJson
    fun toJson(value: OffsetDateTime): String {
        return DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(value)
    }

    @FromJson
    fun fromJson(value: String): OffsetDateTime {
        return try {
            OffsetDateTime.parse(value, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        } catch (e: Exception) {
            try {
                val truncated = value.substring(0, 19)
                java.time.LocalDateTime.parse(
                    truncated,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                ).atOffset(java.time.ZoneOffset.UTC)
            } catch (e2: Exception) {
                OffsetDateTime.now()
            }
        }
    }

}

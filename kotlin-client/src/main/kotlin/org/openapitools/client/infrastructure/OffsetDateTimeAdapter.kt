package org.openapitools.client.infrastructure

import androidx.annotation.RequiresApi
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.OffsetDateTime
import java.time.OffsetDateTime.parse
import java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME

class OffsetDateTimeAdapter {
    @ToJson
    fun toJson(value: OffsetDateTime): String {
        return ISO_OFFSET_DATE_TIME.format(value)
    }

    @FromJson
    fun fromJson(value: String): OffsetDateTime {
        return parse(value, ISO_OFFSET_DATE_TIME)
    }

}

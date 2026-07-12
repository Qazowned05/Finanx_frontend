package com.elias.mismonedas.data.model

import androidx.room.TypeConverter
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

class RoomTypeConverters {
    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): Long? =
        value?.let { java.time.Duration.between(java.time.LocalDateTime.of(1970, 1, 1, 0, 0), it).toMillis() }

    @TypeConverter
    fun toLocalDateTime(value: Long?): LocalDateTime? =
        value?.let { java.time.LocalDateTime.of(1970, 1, 1, 0, 0).plusNanos(it * 1_000_000) }

    @TypeConverter
    fun fromBigDecimal(value: BigDecimal?): String? = value?.toPlainString()

    @TypeConverter
    fun toBigDecimal(value: String?): BigDecimal? = value?.toBigDecimalOrNull()

    @TypeConverter
    fun fromLocalDate(value: LocalDate?): Long? =
        value?.toEpochDay()

    @TypeConverter
    fun toLocalDate(value: Long?): LocalDate? =
        value?.let { LocalDate.ofEpochDay(it) }
}

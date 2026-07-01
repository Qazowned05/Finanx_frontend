package com.example.finanx.data.model

import androidx.room.TypeConverter
import java.math.BigDecimal
import java.time.LocalDateTime

class RoomTypeConverters {
    @TypeConverter
    fun bigDecimalToString(value: BigDecimal?): String? =
        value?.toPlainString()

    @TypeConverter
    fun stringToBigDecimal(value: String?): BigDecimal? =
        value?.let(::BigDecimal)

    @TypeConverter
    fun localDateTimeToString(value: LocalDateTime?): String? =
        value?.toString()

    @TypeConverter
    fun stringToLocalDateTime(value: String?): LocalDateTime? =
        value?.let(LocalDateTime::parse)

    @TypeConverter
    fun paymentMethodToString(value: PaymentMethod?): String? =
        value?.name

    @TypeConverter
    fun stringToPaymentMethod(value: String?): PaymentMethod? =
        value?.let(PaymentMethod::valueOf)

    @TypeConverter
    fun movementTypeToString(value: TransactionType?): String? =
        value?.name

    @TypeConverter
    fun stringToMovementType(value: String?): TransactionType? =
        value?.let(TransactionType::valueOf)
}

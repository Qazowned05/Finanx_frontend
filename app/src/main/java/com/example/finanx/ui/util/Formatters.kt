package com.example.finanx.ui.util

import com.example.finanx.data.model.PaymentMethod
import com.example.finanx.data.model.TransactionType
import java.math.BigDecimal
import java.text.NumberFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

private val displayDateFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale("es", "PE"))

private val displayDateTimeFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd-MM-yyyy, HH:mm", Locale("es", "PE"))

fun BigDecimal.toPen(): String {
    val formatter = NumberFormat.getNumberInstance(
        Locale("es", "PE")
    ).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }

    return "S/ ${formatter.format(this)}"
}

fun LocalDateTime?.toReadableDate(): String {
    if (this == null) {
        return "Fecha no disponible"
    }

    return format(displayDateTimeFormatter)
}

fun LocalDateTime?.toSearchDate(): String {
    if (this == null) return ""

    return format(displayDateFormatter)
}

fun LocalDate?.toReadableDate(): String {
    if (this == null) {
        return "Fecha no disponible"
    }

    return format(displayDateFormatter)
}

fun LocalDate?.toSearchDate(): String {
    if (this == null) return ""

    return format(displayDateFormatter)
}

fun PaymentMethod.toLabel(): String =
    when (this) {
        PaymentMethod.CASH -> "Efectivo"
        PaymentMethod.CREDIT_CARD -> "Tarjeta de crédito"
        PaymentMethod.DEBIT_CARD -> "Tarjeta de débito"
        PaymentMethod.BANK_TRANSFER -> "Transferencia"
        PaymentMethod.YAPE -> "Yape"
        PaymentMethod.PLIN -> "Plin"
        PaymentMethod.OTHER -> "Otro"
    }

fun TransactionType.toReadableType(): String =
    when (this) {
        TransactionType.SPENT -> "Gasto"
        TransactionType.INCOME -> "Ingreso"
    }

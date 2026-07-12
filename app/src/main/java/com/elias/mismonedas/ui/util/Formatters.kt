package com.elias.mismonedas.ui.util

import org.openapitools.client.models.TransactionResponse
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

fun BigDecimal.toCurrency(currencyCode: String): String {
    val locale = when (currencyCode) {
        "PEN" -> Locale("es", "PE")
        "USD" -> Locale("en", "US")
        "EUR" -> Locale("de", "DE")
        "BOB" -> Locale("es", "BO")
        "CLP" -> Locale("es", "CL")
        "COP" -> Locale("es", "CO")
        "MXN" -> Locale("es", "MX")
        "ARS" -> Locale("es", "AR")
        else -> Locale.getDefault()
    }
    val formatter = NumberFormat.getCurrencyInstance(locale).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }
    return formatter.format(this)
}

fun BigDecimal.toPen(): String = toCurrency("PEN")

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

fun TransactionResponse.PaymentMethod.toLabel(): String = when (this) {
    TransactionResponse.PaymentMethod.CASH -> "Efectivo"
    TransactionResponse.PaymentMethod.CREDIT_CARD -> "Tarjeta de crédito"
    TransactionResponse.PaymentMethod.DEBIT_CARD -> "Tarjeta de débito"
    TransactionResponse.PaymentMethod.BANK_TRANSFER -> "Transferencia"
    TransactionResponse.PaymentMethod.YAPE -> "Yape"
    TransactionResponse.PaymentMethod.PLIN -> "Plin"
    TransactionResponse.PaymentMethod.OTHER -> "Otro"
}

fun TransactionResponse.Type.toReadableType(): String = when (this) {
    TransactionResponse.Type.SPENT -> "Gasto"
    TransactionResponse.Type.INCOME -> "Ingreso"
}

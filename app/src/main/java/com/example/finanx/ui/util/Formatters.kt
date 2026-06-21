package com.example.finanx.ui.util

import com.example.finanx.data.model.PaymentMethod
import java.math.BigDecimal
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

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

    return format(
        DateTimeFormatter.ofPattern(
            "dd MMM yyyy, HH:mm",
            Locale("es", "PE")
        )
    )
}

fun LocalDateTime?.toSearchDate(): String {
    if (this == null) return ""

    return format(
        DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale("es", "PE"))
    )
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
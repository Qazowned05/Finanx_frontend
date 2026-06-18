package com.example.finanx.ui.theme

import androidx.compose.ui.graphics.Color

// Paleta empresarial Finanx — marrón/gris elegante
val BrownPrimary = Color(0xFF2B2118)      // Marrón casi negro: AppBar, botones principales, nav activo
val BrownSecondary = Color(0xFF6B4F3B)    // Marrón medio: acentos, iconos activos, enlaces
val GoldAccent = Color(0xFFC9A876)        // Dorado arena: detalles especiales, badges, progreso
val WarmBackground = Color(0xFFF5F1EB)    // Fondo cálido general (reemplaza blanco frío)
val SurfaceWhite = Color(0xFFFFFFFF)      // Tarjetas y superficies elevadas
val StoneGray = Color(0xFF5C5650)         // Texto secundario / gris piedra
val BorderSoft = Color(0xFFE3DCD0)        // Bordes suaves, divisores

val TextDark = Color(0xFF2B2118)
val TextGray = StoneGray
val TextLight = Color(0xFFFFFFFF)

val IncomeGreen = Color(0xFF3F5C44)       // Verde oliva oscuro para ingresos
val ExpenseRed = Color(0xFF8C3B2E)        // Terracota oscuro para gastos
val White = SurfaceWhite
val GrayBorder = BorderSoft

// Dark mode
val BrownPrimaryDark = Color(0xFFE3DCD0)
val DarkBackground = Color(0xFF1C1712)
val DarkSurface = Color(0xFF2A231D)
val DarkOnSurface = Color(0xFFEDE7DE)

// Alias retrocompatibles (pantallas antiguas que aún referencian colores "Blue*")
val BluePrimary = BrownPrimary
val BlueSecondary = BrownSecondary
val LightBackground = WarmBackground

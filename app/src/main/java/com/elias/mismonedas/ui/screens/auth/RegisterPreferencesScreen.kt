package com.elias.mismonedas.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.elias.mismonedas.ui.viewmodel.AuthViewModel
import com.elias.mismonedas.ui.viewmodel.RegistrationStep
import org.openapitools.client.models.UserPreferencesRequest

private data class CurrencyOption(val code: String, val display: String)
private data class TimeZoneOption(val value: UserPreferencesRequest.TimeZone, val display: String)

private val currencies = listOf(
    CurrencyOption("PEN", "PEN — Sol peruano"),
    CurrencyOption("USD", "USD — Dólar estadounidense"),
    CurrencyOption("EUR", "EUR — Euro"),
    CurrencyOption("COP", "COP — Peso colombiano"),
    CurrencyOption("MXN", "MXN — Peso mexicano"),
    CurrencyOption("CLP", "CLP — Peso chileno"),
    CurrencyOption("ARS", "ARS — Peso argentino"),
    CurrencyOption("BOB", "BOB — Boliviano"),
    CurrencyOption("VES", "VES — Bolívar venezolano"),
)

private val timeZones = listOf(
    TimeZoneOption(UserPreferencesRequest.TimeZone.AMERICA_LIMA, "Lima"),
    TimeZoneOption(UserPreferencesRequest.TimeZone.AMERICA_BOGOTA, "Bogotá"),
    TimeZoneOption(UserPreferencesRequest.TimeZone.AMERICA_QUITO, "Quito"),
    TimeZoneOption(UserPreferencesRequest.TimeZone.AMERICA_CARACAS, "Caracas"),
    TimeZoneOption(UserPreferencesRequest.TimeZone.AMERICA_LA_PAZ, "La Paz"),
    TimeZoneOption(UserPreferencesRequest.TimeZone.AMERICA_ASUNCION, "Asunción"),
    TimeZoneOption(UserPreferencesRequest.TimeZone.AMERICA_MONTEVIDEO, "Montevideo"),
    TimeZoneOption(UserPreferencesRequest.TimeZone.AMERICA_SANTIAGO, "Santiago"),
    TimeZoneOption(UserPreferencesRequest.TimeZone.AMERICA_BUENOS_AIRES, "Buenos Aires"),
    TimeZoneOption(UserPreferencesRequest.TimeZone.AMERICA_GUATEMALA, "Guatemala"),
    TimeZoneOption(UserPreferencesRequest.TimeZone.AMERICA_COSTA_RICA, "Costa Rica"),
    TimeZoneOption(UserPreferencesRequest.TimeZone.AMERICA_TEGUCIGALPA, "Tegucigalpa"),
    TimeZoneOption(UserPreferencesRequest.TimeZone.AMERICA_EL_SALVADOR, "El Salvador"),
    TimeZoneOption(UserPreferencesRequest.TimeZone.AMERICA_MANAGUA, "Managua"),
    TimeZoneOption(UserPreferencesRequest.TimeZone.AMERICA_PANAMA, "Panamá"),
    TimeZoneOption(UserPreferencesRequest.TimeZone.AMERICA_HAVANA, "La Habana"),
    TimeZoneOption(UserPreferencesRequest.TimeZone.AMERICA_SANTO_DOMINGO, "Santo Domingo"),
    TimeZoneOption(UserPreferencesRequest.TimeZone.AMERICA_PUERTO_RICO, "Puerto Rico"),
    TimeZoneOption(UserPreferencesRequest.TimeZone.AMERICA_MEXICO_CITY, "Ciudad de México"),
    TimeZoneOption(UserPreferencesRequest.TimeZone.AMERICA_NEW_YORK, "Nueva York"),
    TimeZoneOption(UserPreferencesRequest.TimeZone.AMERICA_LOS_ANGELES, "Los Ángeles"),
    TimeZoneOption(UserPreferencesRequest.TimeZone.EUROPE_MADRID, "Madrid"),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterPreferencesScreen(
    userId: Long,
    viewModel: AuthViewModel,
    onComplete: () -> Unit
) {
    var selectedCurrency by remember { mutableStateOf(currencies.first()) }
    var expandedCurrency by remember { mutableStateOf(false) }
    var selectedTimeZone by remember { mutableStateOf(timeZones.first()) }
    var expandedTimeZone by remember { mutableStateOf(false) }
    var emailNotifications by remember { mutableStateOf(true) }

    val step = viewModel.registrationStep

    LaunchedEffect(step) {
        if (step is RegistrationStep.Completed) onComplete()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 48.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Configura tu cuenta",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Elige tu moneda y zona horaria para empezar",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Text(
                text = "Moneda predeterminada",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            ExposedDropdownMenuBox(
                expanded = expandedCurrency,
                onExpandedChange = { expandedCurrency = it }
            ) {
                OutlinedTextField(
                    value = selectedCurrency.display,
                    onValueChange = {},
                    readOnly = true,
                    leadingIcon = { Icon(Icons.Default.AttachMoney, contentDescription = null) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCurrency) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                ExposedDropdownMenu(
                    expanded = expandedCurrency,
                    onDismissRequest = { expandedCurrency = false }
                ) {
                    currencies.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option.display) },
                            onClick = {
                                selectedCurrency = option
                                expandedCurrency = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Zona horaria",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            ExposedDropdownMenuBox(
                expanded = expandedTimeZone,
                onExpandedChange = { expandedTimeZone = it }
            ) {
                OutlinedTextField(
                    value = selectedTimeZone.display,
                    onValueChange = {},
                    readOnly = true,
                    leadingIcon = { Icon(Icons.Default.Language, contentDescription = null) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTimeZone) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                ExposedDropdownMenu(
                    expanded = expandedTimeZone,
                    onDismissRequest = { expandedTimeZone = false }
                ) {
                    timeZones.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option.display) },
                            onClick = {
                                selectedTimeZone = option
                                expandedTimeZone = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Email,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Notificaciones por email",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Resúmenes y alertas de tu actividad",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Switch(
                    checked = emailNotifications,
                    onCheckedChange = { emailNotifications = it }
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            when (step) {
                is RegistrationStep.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
                is RegistrationStep.Error -> {
                    Text(
                        text = step.message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Button(
                        onClick = {
                            viewModel.registerPreferences(
                                userId = userId,
                                currencyCode = selectedCurrency.code,
                                timeZone = selectedTimeZone.value,
                                emailNotifications = emailNotifications
                            )
                        },
                        modifier = Modifier.fillMaxWidth().height(54.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Reintentar", style = MaterialTheme.typography.labelLarge)
                    }
                }
                else -> {
                    Button(
                        onClick = {
                            viewModel.registerPreferences(
                                userId = userId,
                                currencyCode = selectedCurrency.code,
                                timeZone = selectedTimeZone.value,
                                emailNotifications = emailNotifications
                            )
                        },
                        modifier = Modifier.fillMaxWidth().height(54.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(
                            "Finalizar configuración",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }
    }
}

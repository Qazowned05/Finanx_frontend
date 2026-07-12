package com.elias.mismonedas.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

/**
 * Maneja el patrón "formulario con posibles cambios sin guardar" que antes
 * estaba duplicado en 3 lugares distintos de AppNavigation.kt (crear
 * movimiento, programar movimiento, programar presupuesto):
 *
 *  - Intercepta el back nativo (system back / gesto).
 *  - Expone [requestBack] para que el botón de back del TopBar dispare
 *    exactamente la misma lógica (así ambos caminos quedan sincronizados,
 *    que era justo el bug que se reportó).
 *  - Si hay un paso intermedio (wizard), [onStepBack] puede consumir el
 *    evento devolviendo `true` sin salir de la pantalla.
 *  - Si hay cambios sin guardar, confirma con un diálogo antes de salir.
 *
 * Uso típico dentro de una screen de creación:
 * ```
 * val backHandler = rememberUnsavedChangesBackHandler(
 *     hasUnsavedChanges = { viewModel.hasUnsavedChanges() },
 *     onStepBack = { viewModel.goToPreviousStepOrNull() },
 *     onLeave = onBack
 * )
 * SetTopBar(TopBarConfig(title = "...", onBack = backHandler.requestBack))
 * ```
 */
class UnsavedChangesBackHandlerState internal constructor(
    val requestBack: () -> Unit
)

@Composable
fun rememberUnsavedChangesBackHandler(
    hasUnsavedChanges: () -> Boolean,
    onLeave: () -> Unit,
    onStepBack: (() -> Boolean)? = null,
    discardTitle: String = "¿Descartar cambios?",
    discardMessage: String = "Si descartas, se perderá la información ingresada en el formulario."
): UnsavedChangesBackHandlerState {
    var showConfirmDiscard by rememberSaveable { mutableStateOf(false) }

    val requestBack: () -> Unit = requestBack@{
        if (onStepBack?.invoke() == true) return@requestBack
        if (hasUnsavedChanges()) {
            showConfirmDiscard = true
        } else {
            onLeave()
        }
    }

    BackHandler(onBack = requestBack)

    if (showConfirmDiscard) {
        AlertDialog(
            onDismissRequest = { showConfirmDiscard = false },
            title = { Text(discardTitle) },
            text = { Text(discardMessage) },
            confirmButton = {
                TextButton(onClick = {
                    showConfirmDiscard = false
                    onLeave()
                }) { Text("Descartar") }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDiscard = false }) { Text("Seguir editando") }
            }
        )
    }

    return UnsavedChangesBackHandlerState(requestBack = requestBack)
}

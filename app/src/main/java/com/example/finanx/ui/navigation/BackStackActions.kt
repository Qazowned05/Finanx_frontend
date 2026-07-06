package com.example.finanx.ui.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

/** Apila un destino secundario encima del actual. */
fun NavBackStack<NavKey>.push(destination: AppDestination) {
    add(destination)
}

/** Pop seguro: nunca deja el back stack vacío. */
fun NavBackStack<NavKey>.pop() {
    if (size > 1) removeAt(lastIndex)
}
/**
 * Cambia de tab del bottom bar. Se eligió un back stack único y simple:
 * cambiar de tab reemplaza todo el stack por ese tab (sin recordar el
 * historial de cada tab por separado). Esto es intencional: es lo que hace
 * que el back nativo y el back del TopBar se comporten siempre igual, sin
 * el bug de "el back nativo vuelve a la raíz" que tenía la versión anterior
 * basada en `popUpTo(...).saveState()/restoreState()`.
 */
fun NavBackStack<NavKey>.navigateToTab(tab: TopLevelDestination) {
    if (lastOrNull() == tab) return
    clear()
    add(tab)
}

/** Quita todo lo apilado por encima de [destination], sin duplicar si ya está en el stack. */
fun NavBackStack<NavKey>.popUpToOrPush(destination: AppDestination) {
    val index = indexOf(destination)
    if (index >= 0) {
        while (lastIndex > index) removeAt(lastIndex)
    } else {
        add(destination)
    }
}
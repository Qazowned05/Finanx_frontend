package com.elias.mismonedas.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Configuración del TopAppBar compartido. Cada screen es dueño de su propia
 * configuración y la publica con [SetTopBar] — nadie más decide por ella.
 *
 * El título/back-arrow "por defecto" (según la forma del back stack) lo pone
 * [AppScaffold]; una screen solo necesita [SetTopBar] cuando quiere algo
 * distinto al default (título dinámico, ocultar refresh, interceptar el back
 * para un wizard, etc).
 */
data class TopBarConfig(
    val title: String = "",
    val subtitle: String? = null,
    val showBack: Boolean? = null, // null = usar el default estructural del AppScaffold
    val onBack: (() -> Unit)? = null, // null = usar el pop por default del AppScaffold
    val onRefresh: (() -> Unit)? = null
)

/** Holder mutable expuesto vía CompositionLocal para que cualquier entry publique su config. */
class TopBarController internal constructor(
    private val setConfig: (TopBarConfig) -> Unit
) {
    fun update(config: TopBarConfig) = setConfig(config)
}

val LocalTopBarController = staticCompositionLocalOf<TopBarController> {
    error("LocalTopBarController no está disponible: falta envolver con AppScaffold")
}

/**
 * Azúcar sintáctica para que una screen publique su TopBar en una sola línea:
 *
 * ```
 * SetTopBar(TopBarConfig(title = "Inicio"))
 * ```
 *
 * Se re-publica cada vez que [config] cambia (útil para subtítulos dinámicos,
 * p.ej. "Paso 2 de 4" en el wizard de creación de movimiento).
 */
@Composable
fun SetTopBar(config: TopBarConfig) {
    val controller = LocalTopBarController.current
    val latestConfig = rememberUpdatedState(config)
    // La clave usa solo los campos "de valor": [TopBarConfig] contiene lambdas
    // (onBack/onRefresh) que son instancias nuevas en cada recomposición, y
    // keyear el efecto por el data class completo lo relanzaba sin necesidad.
    LaunchedEffect(config.title, config.subtitle, config.showBack) {
        controller.update(latestConfig.value)
    }
}

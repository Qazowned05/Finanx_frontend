package com.elias.mismonedas.ui.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.SmartToy
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.elias.mismonedas.ui.components.TutorialPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private data class OnboardingPage(
    val icon: ImageVector,
    val title: String,
    val description: String,
    val trustNote: String? = null
)

@Composable
fun OnboardingScreen(
    userId: Long,
    onComplete: () -> Unit
) {
    var showOnboarding by remember { mutableStateOf<Boolean?>(null) }

    LaunchedEffect(userId) {
        try {
            val done = TutorialPreferences.isOnboardingCompleted(userId).first()
            showOnboarding = !done
        } catch (_: Exception) {
            showOnboarding = true
        }
    }

    when (showOnboarding) {
        null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }
        false -> LaunchedEffect(Unit) { onComplete() }
        true -> {
            val coroutineScope = rememberCoroutineScope()
            OnboardingContent(
                onFinish = {
                    coroutineScope.launch {
                        TutorialPreferences.markOnboardingCompleted(userId)
                        onComplete()
                    }
                }
            )
        }
    }
}

@Composable
private fun OnboardingContent(onFinish: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { 4 })
    val scope = rememberCoroutineScope()
    var showSkipDialog by remember { mutableStateOf(false) }

    val pages = remember {
        listOf(
            OnboardingPage(
                icon = Icons.Outlined.AccountBalanceWallet,
                title = "Toma el control de tus finanzas",
                description = "Mis Monedas te ayuda a registrar ingresos y gastos, crear presupuestos y alcanzar tus metas de ahorro. Todo en un solo lugar.",
                trustNote = "Tus datos viajan cifrados y solo tú tienes acceso a tu información."
            ),
            OnboardingPage(
                icon = Icons.Outlined.SwapHoriz,
                title = "Registra cada movimiento",
                description = "Añade ingresos y gastos al instante. Categoriza cada transacción para tener una visión clara de a dónde va tu dinero.",
                trustNote = null
            ),
            OnboardingPage(
                icon = Icons.AutoMirrored.Outlined.TrendingUp,
                title = "Define presupuestos y metas",
                description = "Establece límites mensuales por categoría y crea metas de ahorro con seguimiento visual de tu progreso.",
                trustNote = null
            ),
            OnboardingPage(
                icon = Icons.Outlined.SmartToy,
                title = "Habla con Finn, tu asistente",
                description = "Finn responde preguntas sobre tus finanzas y te ayuda a tomar mejores decisiones informadas.",
                trustNote = "Tu privacidad es primero. Tus datos están cifrados y nunca se comparten sin tu consentimiento."
            )
        )
    }

    val currentPage = pagerState.currentPage
    val isLastPage = currentPage == pages.lastIndex

    if (showSkipDialog) {
        AlertDialog(
            onDismissRequest = { showSkipDialog = false },
            title = { Text("¿Saltar introducción?") },
            text = { Text("Puedes acceder a los tutoriales desde cada pantalla cuando quieras.") },
            confirmButton = {
                TextButton(onClick = { showSkipDialog = false; onFinish() }) {
                    Text("Saltar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSkipDialog = false }) { Text("Cancelar") }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) { pageIndex ->
            PageContent(page = pages[pageIndex])
        }

        Spacer(modifier = Modifier.height(32.dp))

        PageIndicator(pageCount = pages.size, currentPage = currentPage)

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (isLastPage) {
                    onFinish()
                } else {
                    scope.launch {
                        pagerState.animateScrollToPage(currentPage + 1)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(
                if (isLastPage) "Comenzar" else "Siguiente",
                style = MaterialTheme.typography.labelLarge
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(onClick = { showSkipDialog = true }) {
            Text(
                "Saltar introducción",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun PageContent(page: OnboardingPage) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .size(80.dp)
                .background(
                    MaterialTheme.colorScheme.primaryContainer,
                    RoundedCornerShape(20.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = page.icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = page.title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = page.description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        if (page.trustNote != null) {
            Spacer(modifier = Modifier.height(24.dp))
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = page.trustNote,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }
        }
    }
}

@Composable
private fun PageIndicator(pageCount: Int, currentPage: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        repeat(pageCount) { index ->
            val isSelected = index == currentPage
            Box(
                modifier = Modifier
                    .size(
                        width = if (isSelected) 24.dp else 8.dp,
                        height = 8.dp
                    )
                    .background(
                        color = if (isSelected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(4.dp)
                    )
            )
        }
    }
}

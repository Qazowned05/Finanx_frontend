package com.example.finanx.ui.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class TutorialStep(
    val targetKey: String,
    val message: String,
    private val autoAdvanceCondition: (() -> Boolean)? = null
) {
    fun canAdvance(): Boolean = autoAdvanceCondition?.invoke() ?: true
    val isAutoGated: Boolean get() = autoAdvanceCondition != null
}

class TutorialController(
    private val steps: List<TutorialStep>,
    private val onFinished: () -> Unit
) {
    var currentIndex by mutableStateOf(0)
        private set

    val currentStep: TutorialStep? get() = steps.getOrNull(currentIndex)

    val isLastStep: Boolean
        get() = currentIndex >= steps.lastIndex

    fun next() {
        if (!(currentStep?.canAdvance() ?: true)) return
        if (currentIndex >= steps.lastIndex) { onFinished(); return }
        currentIndex++
    }

    fun skip() = onFinished()
}

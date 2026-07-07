package com.example.finanx.ui.components

import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.boundsInRoot

fun Modifier.tutorialTarget(key: String, registry: MutableMap<String, Rect>) = composed(
    factory = {
        DisposableEffect(key) {
            onDispose { registry.remove(key) }
        }
        onGloballyPositioned { registry[key] = it.boundsInRoot() }
    }
)
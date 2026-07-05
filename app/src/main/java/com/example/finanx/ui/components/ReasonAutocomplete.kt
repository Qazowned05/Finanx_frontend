package com.example.finanx.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import com.example.finanx.data.model.SelectOption

@Composable
fun ReasonAutocomplete(
    text: String,
    reasons: List<SelectOption>,
    enabled: Boolean,
    onReasonChange: (reasonId: Long?, newReason: String) -> Unit,
    modifier: Modifier = Modifier,
    onNext: (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        capitalization = KeyboardCapitalization.Sentences,
        imeAction = ImeAction.Next
    )
) {
    val focusManager = LocalFocusManager.current
    val suggestions = remember(text, reasons) {
        if (text.isBlank()) emptyList()
        else reasons.filter { it.label.contains(text.trim(), ignoreCase = true) }
    }

    var suppressDropdown by remember { mutableStateOf(false) }
    val showDropdown = suggestions.isNotEmpty() && !suppressDropdown

    Box(modifier = modifier) {
        OutlinedTextField(
            value = text,
            onValueChange = { value ->
                suppressDropdown = false
                val exact = reasons.firstOrNull { it.label.equals(value.trim(), ignoreCase = true) }
                if (exact != null) {
                    onReasonChange(exact.id, exact.label)
                } else {
                    onReasonChange(null, value)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            label = { Text("Descripción") },
            singleLine = true,
            keyboardOptions = keyboardOptions,
            keyboardActions = KeyboardActions(
                onNext = {
                    onNext?.invoke() ?: focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            supportingText = {
                Text("Escribe o selecciona un motivo")
            }
        )

        DropdownMenu(
            expanded = showDropdown,
            onDismissRequest = { suppressDropdown = true },
            properties = PopupProperties(focusable = false)
        ) {
            suggestions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.label) },
                    onClick = {
                        suppressDropdown = true
                        onReasonChange(option.id, option.label)
                    }
                )
            }
        }
    }
}

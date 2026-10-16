package com.elias.mismonedas.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable

//todo vreificar uso
@Composable
fun SearchBar(value: String, onValueChange: (String) -> Unit, label: String) {
    FinanxOutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
    )
}

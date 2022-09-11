package com.acc.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CheckButton(
    text: String,
    onChecked: (Boolean) -> Unit,
    selected: Boolean = false,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .selectable(
                selected = selected,
                onClick = { onChecked(!selected) }
            )
            .padding(horizontal = 16.dp),
    ) {
        Checkbox(checked = selected, onCheckedChange = onChecked)
        Text(text)
    }
}
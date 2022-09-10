package com.acc.common.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OptionButton(text: String, selectedOption: String = "", onClick: () -> Unit = {}) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .selectable(
                selected = text == selectedOption,
                onClick = onClick
            )
            .padding(horizontal = 16.dp),
    ) {
        RadioButton(
            selected = text == selectedOption,
            onClick = onClick
        )
        Text(
            text = text,
            style = MaterialTheme.typography.body1.merge(),
            modifier = Modifier.padding(start = 16.dp)
        )
//        Text(text)
    }
}
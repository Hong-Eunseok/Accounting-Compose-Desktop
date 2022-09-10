package com.acc.common.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.acc.common.ui.largePadding
import com.acc.common.ui.smallPadding

@Composable
fun RowTextField(
    value: String,
    setValue: (String) -> Unit = {},
    label: String,
    errorMessage: String? = null,
    modifier: Modifier = Modifier
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        Text(text = label, modifier = Modifier.widthIn(min = 120.dp))

        Spacer(modifier = Modifier.width(largePadding))

        BasicTextField(
            value = value,
            onValueChange = setValue,
            enabled = true,
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colors.onBackground),
            modifier = Modifier
                .border(
                    width = 1.25.dp,
                    color = MaterialTheme.colors.primary.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(smallPadding)
                .weight(.5f)
        )

        if (errorMessage != null) {
            Text(text = errorMessage, color = com.acc.common.ui.error)
        }
    }
}
package com.acc.common.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.acc.common.ui.largePadding
import com.acc.common.ui.mediumPadding
import java.awt.Color
import java.awt.Dimension
import javax.swing.JPanel
import javax.swing.JTextField

@Composable
fun SwingsRowTextField(
    textField: JTextField,
    label: String,
    errorMessage: String? = null,
) {
    val density = LocalDensity.current
    val rgb: Int = MaterialTheme.colors.background.toArgb()

    Column {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = largePadding)) {
            Text(text = label, modifier = Modifier.widthIn(min = 120.dp))
            Spacer(modifier = Modifier.width(largePadding))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(40.dp)
                    .onSizeChanged {
                        var width: Int
                        var height: Int
                        with(density) {
                            width = it.width.toDp().value.toInt()
                            height = it.height.toDp().value.toInt()
                        }
                        textField.preferredSize = Dimension(width, height)
                    }
            ) {
                SwingPanel(
                    factory = {
                        JPanel().apply {
                            background = Color(rgb)
                            add(textField)
                        }
                    }
                )
            }
        }
        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = com.acc.common.ui.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(start = 140.dp, bottom = mediumPadding)
            )
        }
    }
}
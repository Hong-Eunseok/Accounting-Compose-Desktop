package com.acc.common.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.acc.common.ui.error
import com.acc.common.ui.rowHeight
import com.acc.common.ui.seed
import com.acc.common.ui.smallPadding

@Composable
fun AppRowActionsWithoutModify(
    selected: Boolean,
    onDelete: () -> Unit
) {

    val width by animateDpAsState(if (selected) rowHeight else 0.dp)
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(smallPadding),
        modifier = Modifier.height(rowHeight)
    ) {
        if (showDeleteConfirmation) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxHeight()
                    .clickable { onDelete() }
                    .width(width)
            ) {
                Icons.Default.Info
                AppIcon(imageVector = Icons.Default.Check, tint = seed)
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxHeight()
                    .clickable { showDeleteConfirmation = false }
                    .width(width)
            ) {
                AppIcon(imageVector = Icons.Default.Close, tint = error)
            }
        } else {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxHeight()
                    .clickable { showDeleteConfirmation = true }
                    .width(width)
            ) {
                AppIcon(Icons.Default.Delete)
            }
        }
    }
}
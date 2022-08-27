package com.acc.features.home.partners.list.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.acc.common.components.AppIcon
import com.acc.common.components.AppRowActions
import com.acc.common.ui.mediumPadding
import com.acc.common.ui.rowHeight
import com.acc.common.ui.smallPadding
import com.acc.di.AppComponent
import com.acc.features.di.ViewModel
import com.acc.features.home.partners.list.presentation.viewmodel.PartnersViewModel
import javax.inject.Inject

class PartnersScreen(appComponent: AppComponent) {

    init {
        appComponent.inject(this)
    }

    @Inject lateinit var viewModel: PartnersViewModel

    @Composable
    fun PartnersScreen(navigateAddPartner: () -> Unit) {

        val partners by viewModel.partners.collectAsState(initial = emptyList())
        val selectedPartner by viewModel.selectedPartner.collectAsState()

        Scaffold(
            floatingActionButton = {
                FloatingActionButton(onClick = navigateAddPartner) {
                    AppIcon(imageVector = Icons.Default.Add)
                }
            }
        ) {
            LazyColumn {
                itemsIndexed(partners) { index, item ->

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(mediumPadding),
                        modifier = Modifier
                            .clickable { viewModel.selectPartner(item) }
                            .fillMaxWidth()
                            .height(rowHeight)
                            .background(
                                MaterialTheme.colors.surface.copy(
                                    if (selectedPartner?.id == item.id) 0.1f
                                    else if (index % 2 == 0) 0.8f
                                    else 0.5f
                                )

                            )
                    ) {
                        Text(text = item.name, modifier = Modifier.padding(start = smallPadding))
                        Text(text = item.address)
                        Text(text = item.phoneNumber)
                        Spacer(modifier = Modifier.weight(1f))
                        AppRowActions(
                            selected = selectedPartner?.id == item.id,
                            onDelete = { viewModel.deletePartner(item.id) }
                        )
                    }
                }
            }
        }
    }
}

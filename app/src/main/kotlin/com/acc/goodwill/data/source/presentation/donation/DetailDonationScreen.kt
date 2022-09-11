package com.acc.goodwill.data.source.presentation.donation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.acc.di.AppComponent
import com.acc.goodwill.domain.model.Donate
import javax.inject.Inject

class DetailDonationScreen(appComponent: AppComponent) {

    @Inject lateinit var viewModel: DonationViewModel

    init {
        appComponent.inject(this)
    }

    @Composable
    fun DetailDonationScreen(
        navigateBack: () -> Unit,
        donate: Donate
    ) {
        val products by viewModel.productResult.collectAsState()
        viewModel.queryProduct(donate.primaryKey.value)
        ConfirmDonationContent(products, {}, {})
    }

}


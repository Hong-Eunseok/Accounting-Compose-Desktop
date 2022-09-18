package com.acc.goodwill.presentation.contributor

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.acc.di.AppComponent
import com.acc.goodwill.domain.model.Contributor
import com.acc.goodwill.domain.model.Donate
import com.acc.goodwill.presentation.donation.ConfirmDonationContent
import com.acc.goodwill.presentation.donation.DonationViewModel
import javax.inject.Inject

class DetailContributorScreen(appComponent: AppComponent) {

    @Inject lateinit var viewModel: DonationViewModel

    init {
        appComponent.inject(this)
    }

    @Composable
    fun DetailDonationScreen(
        navigateBack: () -> Unit,
        contributor: Contributor
    ) {
//        val products by viewModel.productResult.collectAsState()
//        viewModel.queryProduct(donate.primaryKey.value)
//        ConfirmDonationContent(products, {}, {})
    }

}


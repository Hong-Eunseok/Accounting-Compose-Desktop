package com.acc.goodwill.presentation.contributor

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.acc.common.components.AppIcon
import com.acc.common.ui.largePadding
import com.acc.common.ui.mediumPadding
import com.acc.di.AppComponent
import com.acc.goodwill.domain.model.Contributor
import com.acc.goodwill.domain.model.ContributorSearchResult
import com.acc.goodwill.domain.model.DonationStatics
import com.acc.goodwill.domain.model.Product
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
        val result by viewModel.searchContributorResult.collectAsState()
        viewModel.queryContributorAndProduct(contributor.primaryKey.value)

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "기부 정보", style = MaterialTheme.typography.h4) },
                    navigationIcon = {
                        IconButton(onClick = navigateBack) { AppIcon(imageVector = Icons.Default.ArrowBack) }
                    }
                )
            }
        ) {
            Box(modifier = Modifier.fillMaxSize().padding(largePadding)) {
                Column {
                    detailContributor(result)
                    detailDonation(result.donation)
                }
            }
        }
    }

}

@Composable
private fun detailContributor(result: ContributorSearchResult) {
    val contributor = result.contributor ?: return
    Column {
        Text(text = "이름 : ${contributor.name}")
        Text(text = "핸드폰 번호 : ${contributor.phoneNumber}")
        Text(text = "주소 : ${contributor.address}")
        Text(text = "주민/사업자번호 : ${contributor.registrationNumber}")
        Text(text = "추천 : ${Contributor.RECOMMEND[contributor.recommend]}")
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun detailDonation(
    donations: List<DonationStatics>,
    state: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.8f)
            .padding(end = 12.dp),
        state = state
    ) {
        donations.forEach {
            val donation = it.donation.donate
            stickyHeader {
                Column {
                    Text(
                        "기부 날짜 : ${donation.createAt}",
                        style = MaterialTheme.typography.body2,
                        color = LocalContentColor.current,
                        modifier = Modifier
                    )
                    Text(
                        "총수량 / 불량 / 양품 : ${donation.total} / ${donation.error} / ${donation.correct}",
                        style = MaterialTheme.typography.body2,
                        color = LocalContentColor.current,
                        modifier = Modifier
                    )
                    Text(
                        "기부환산금액 : ${donation.price}원",
                        style = MaterialTheme.typography.body1,
                        color = LocalContentColor.current.copy(alpha = ContentAlpha.high),
                        modifier = Modifier
                    )
                }
            }

            val products = it.products
            items(products.size) { index ->
                val product = products[index]
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(mediumPadding)
                ) {
                    Column {
                        Text("카테고리 : ${Product.CATEGORIES[product.category]}")
                        Text("기증품 : ${product.label}")
                        Text("수량 / 불량 / 양품 : ${product.total} / ${product.error} / ${product.correct}")
                        Text("기부환산금액 : ${product.transferPrice} (개당 가격 : ${product.price})")
                    }
                }

            }
        }
    }

}


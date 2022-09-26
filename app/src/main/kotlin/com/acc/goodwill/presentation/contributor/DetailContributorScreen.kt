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
import androidx.compose.ui.Alignment
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
import java.text.DecimalFormat
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class DetailContributorScreen(appComponent: AppComponent) {

    @Inject lateinit var viewModel: DonationViewModel

    private val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

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
                    title = { Text(text = "기부정보", style = MaterialTheme.typography.h4) },
                    navigationIcon = {
                        IconButton(onClick = navigateBack) { AppIcon(imageVector = Icons.Default.ArrowBack) }
                    }
                )
            }
        ) {
            Box(modifier = Modifier.fillMaxSize().padding(largePadding)) {
                Column {
                    Row(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.45f)) {
                        detailContributor(result)
                        summaryDonation(result.donation)
                    }


                    Spacer(modifier = Modifier.height(largePadding))

                    Text(
                        text = "상세정보",
                        style = MaterialTheme.typography.h1,
                        color = LocalContentColor.current,
                    )
                    detailDonation(result.donation)
                }
            }
        }
    }
}

@Composable
private fun summaryDonation(
    donations: List<DonationStatics>,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(1f).fillMaxHeight(0.9f)
            .background(Color.White)
            .padding(mediumPadding),
    ) {
        Column {
            Text(
                text = "기부 정보",
                style = MaterialTheme.typography.h1,
                color = LocalContentColor.current,
            )

            Spacer(modifier = Modifier.height(largePadding))

            Text(
                text = "총 기부횟수",
                style = MaterialTheme.typography.h5,
                color = LocalContentColor.current,
            )
            Text(
                text = donations.size.toString(),
                style = MaterialTheme.typography.h5,
                color = LocalContentColor.current.copy(alpha = ContentAlpha.high),
            )

            Spacer(modifier = Modifier.height(largePadding))

            Text(
                text = "총 기부금액",
                style = MaterialTheme.typography.h5,
                color = LocalContentColor.current,
            )
            Text(
                text = DecimalFormat("#,###원").format(donations.sumOf { it.donation.donate.price }.toLong()),
                style = MaterialTheme.typography.h5,
                color = LocalContentColor.current.copy(alpha = ContentAlpha.high),
            )

            Spacer(modifier = Modifier.height(largePadding))

            Text(
                text = "가장 최근기부",
                style = MaterialTheme.typography.h5,
                color = LocalContentColor.current,
            )
            Text(
                text = if (donations.isEmpty()) "없습니다." else donations[0].donation.donate.createAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                style = MaterialTheme.typography.h5,
                color = LocalContentColor.current.copy(alpha = ContentAlpha.high),
            )
        }
    }
}

@Composable
private fun detailContributor(result: ContributorSearchResult) {
    val contributor = result.contributor ?: return
    Box(
        modifier = Modifier
            .fillMaxWidth(0.5f).fillMaxHeight(0.9f)
            .background(Color.White)
            .padding(mediumPadding)
    ) {
        Column {
            Text(
                text = "회원 정보",
                style = MaterialTheme.typography.h1,
                color = LocalContentColor.current,
            )

            Spacer(modifier = Modifier.height(largePadding))

            Text(
                text = "이름",
                style = MaterialTheme.typography.h5,
                color = LocalContentColor.current,
            )
            Text(
                text = contributor.name,
                style = MaterialTheme.typography.h5,
                color = LocalContentColor.current.copy(alpha = ContentAlpha.high),
            )

            Spacer(modifier = Modifier.height(largePadding))

            Text(
                text = "핸드폰 번호",
                style = MaterialTheme.typography.h5,
                color = LocalContentColor.current,
            )
            Text(
                text = contributor.phoneNumber.orEmpty().ifEmpty { "정보가 없습니다." },
                style = MaterialTheme.typography.h5,
                color = LocalContentColor.current.copy(alpha = ContentAlpha.high),
            )

            Spacer(modifier = Modifier.height(largePadding))

            Text(
                text = "주소",
                style = MaterialTheme.typography.h5,
                color = LocalContentColor.current,
            )
            Text(
                text = contributor.address.orEmpty().ifEmpty { "정보가 없습니다." },
                style = MaterialTheme.typography.h5,
                color = LocalContentColor.current.copy(alpha = ContentAlpha.high),
            )

            Spacer(modifier = Modifier.height(largePadding))

            Text(
                text = "주민/사업자번호",
                style = MaterialTheme.typography.h5,
                color = LocalContentColor.current,
            )
            Text(
                text = contributor.registrationNumber.orEmpty().ifEmpty { "정보가 없습니다." },
                style = MaterialTheme.typography.h5,
                color = LocalContentColor.current.copy(alpha = ContentAlpha.high),
            )

            Spacer(modifier = Modifier.height(mediumPadding))
            Text(text = "추천 : ${Contributor.RECOMMEND[contributor.recommend]}")
        }
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
            .padding(mediumPadding),
        state = state
    ) {
        donations.forEach {
            val donation = it.donation.donate
            stickyHeader {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.primary)
                        .padding(mediumPadding)
                ) {
                    Column {
                        Text(
                            "기부 날짜 : ${donation.createAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))}",
                            style = MaterialTheme.typography.h5,
                            color = Color.White,
                            modifier = Modifier
                        )
                        Text(
                            "총수량 / 불량 / 양품 : ${donation.total} / ${donation.error} / ${donation.correct}",
                            style = MaterialTheme.typography.h5,
                            color = Color.White,
                            modifier = Modifier
                        )
                        Text(
                            "기부환산금액 : ${DecimalFormat("#,###원").format(donation.price.toLong())}",
                            style = MaterialTheme.typography.h5,
                            color = Color.White,
                            modifier = Modifier
                        )
                    }
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
                        Text("기증품 : ${product.label} (${Product.CATEGORIES[product.category]})")
                        Text("수량 / 불량 / 양품 : ${product.total} / ${product.error} / ${product.correct}")
                        Text("기부환산금액 : ${DecimalFormat("#,###원").format(product.transferPrice.toLong())} (개당 가격 : ${DecimalFormat("#,###원").format(product.price.toLong())})")
                    }
                }

            }
        }
    }

}


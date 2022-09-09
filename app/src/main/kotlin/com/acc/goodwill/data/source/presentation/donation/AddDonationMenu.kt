package com.acc.goodwill.data.source.presentation.donation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.acc.goodwill.data.source.presentation.common.LocaleComposition
import com.acc.goodwill.data.source.presentation.navigation.Confirm
import com.acc.goodwill.data.source.presentation.navigation.DonationRoute
import com.acc.goodwill.data.source.presentation.navigation.SearchContribute

@Composable
fun AddDonationMenu(
    currentRoute: DonationRoute,
    navigateSearchContributor: () -> Unit,
    navigateAddProduct: () -> Unit,
    navigateAddConfirm: () -> Unit
) {
    val locale = LocaleComposition.current

    Row {
        Button(
            onClick = navigateSearchContributor,
            modifier = Modifier.padding(8.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if (currentRoute is SearchContribute) MaterialTheme.colors.primary else MaterialTheme.colors.secondary
            )
        ) {
            Text(text = locale.stepSearchContributor)
        }
        Button(
            onClick = navigateAddProduct,
            modifier = Modifier.padding(8.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if (currentRoute is SearchContribute) MaterialTheme.colors.primary else MaterialTheme.colors.secondary
            ),
            //enabled = currentRoute.value >= AddProduct.value
        ) {
            Text(text = locale.stepAddProduct)
        }
        Button(
            onClick = navigateAddConfirm,
            modifier = Modifier.padding(8.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if (currentRoute is SearchContribute) MaterialTheme.colors.primary else MaterialTheme.colors.secondary
            ),
            enabled = currentRoute.value >= Confirm.value
        ) {
            Text(text = locale.stepConfirm)
        }
    }
}

//카테고리 : 의류/도서/잡화생활/잡화유아/문구,완구/가구/가전/주방/기타
//기증품목 : XXXXXX
//수량 : XXXXXX
//불량 : XXXX
//금액 : XXXX
//추가
//
//다음
//
//추가된 리스트 보이고
//뭔가 이쁘게

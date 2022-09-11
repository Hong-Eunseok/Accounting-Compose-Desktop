package com.acc.goodwill.data.source.presentation.donation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.acc.common.components.CheckButton
import com.acc.common.components.OptionButton
import com.acc.common.ui.largePadding
import com.acc.common.ui.mediumPadding
import com.acc.goodwill.domain.model.Product

@Composable
fun ConfirmDonationContent(
    products: List<Product>,
    navigateBack: () -> Unit,
    navigateConfirm: (Triple<Int, Int, Boolean>) -> Unit
) {

    val from = listOf("기증", "수거", "기타")
    val organization = listOf("개인", "단체")
    val (selectedFrom, onFromSelected) = remember { mutableStateOf(from[0]) }
    val (selectedOrganization, onOrganizationSelected) = remember { mutableStateOf(organization[0]) }
    val (checked, onChecked) = remember { mutableStateOf(true) }

    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colors.background),
    ) {
        Box(
            modifier = Modifier.fillMaxSize().padding(largePadding),
        ) {
            Column {
                Text("기증통로", modifier = Modifier.padding(end = largePadding))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    from.forEach { text ->
                        OptionButton(
                            text = text,
                            selectedOption = selectedFrom,
                            onClick = { onFromSelected(text) }
                        )
                    }
                }

                Text("개인/단체", modifier = Modifier.padding(end = largePadding))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    organization.forEach { text ->
                        OptionButton(
                            text = text,
                            selectedOption = selectedOrganization,
                            onClick = { onOrganizationSelected(text) }
                        )
                    }
                    CheckButton("교인", onChecked, checked)
                }

                Spacer(modifier = Modifier.height(30.dp))

                Box(
                    contentAlignment = Alignment.CenterStart,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text("기부 환산금액 : ${products.sumOf { it.transferPrice }}원")
                        Text("총 수량 : ${products.sumOf { it.total }}")
                        Text("총 불량수 / 총 양품수 : ${products.sumOf { it.error }} / ${products.sumOf { it.correct }}")
                        Spacer(modifier = Modifier.height(mediumPadding))
                        Text("기부 물품 내역")
                        Spacer(modifier = Modifier.height(largePadding))

                        ProductResult(-1, {}, products)
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Row {
                    Button(onClick = navigateBack) {
                        Text("기부물품 수정")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = {
                            navigateConfirm(
                                Triple(
                                    from.indexOf(selectedFrom),
                                    organization.indexOf(selectedOrganization),
                                    checked
                                )
                            )
                        }
                    ) {
                        Text("완료")
                    }
                }
            }
        }
    }
}
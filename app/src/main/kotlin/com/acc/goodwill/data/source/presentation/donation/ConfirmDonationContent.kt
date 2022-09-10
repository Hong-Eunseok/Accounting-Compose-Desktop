package com.acc.goodwill.data.source.presentation.donation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.acc.common.components.CheckButton
import com.acc.common.components.OptionButton

@Composable
fun ConfirmDonationContent(navigateBack: () -> Unit) {
    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colors.background),
    ) {
        Column {
            Text("기증통로")
            Row {
                OptionButton("기증")
                OptionButton("수거")
                OptionButton("기타")
            }

            Text("개인/단체")
            Row {
                OptionButton("개인")
                OptionButton("단체")
                CheckButton("교인")
            }

            Row {
                Text("총수량 : 100")
                Text("양품 : 50")
                Text("불량 : 50")
            }

            Text("기부 환산금액")

            Text("기부 물품 내역")
            ProductResult(1, {}, listOf())
            
            Row {
                Button(onClick = {}) {
                    Text("기부물품 수정")
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = navigateBack) {
                    Text("완료")
                }
            }
        }
    }
}
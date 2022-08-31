package com.acc.goodwill.presentation.donation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.acc.di.AppComponent
import com.acc.goodwill.domain.repository.DonationRepository
import javax.inject.Inject

class AddDonationContent(appComponent: AppComponent) {

    @Inject lateinit var donationRepository: DonationRepository

    init {
        appComponent.inject(this)
    }

    @Composable
    fun addDonationContent() {
        Column {
            Row {
                Button(onClick = {}, modifier = Modifier.padding(8.dp)) {
                    Text(text = "1. 기부 명단 찾기")
                }
                Button(onClick = {}, modifier = Modifier.padding(8.dp), enabled = false) {
                    Text(text = "2. 기부 물품 입력")
                }
                Button(onClick = {}, modifier = Modifier.padding(8.dp), enabled = false) {
                    Text(text = "3. 최종입력 확인")
                }
            }
            SearchLayout()
        }
    }

    @Composable
    fun SearchLayout() {
        var search by remember { mutableStateOf(TextFieldValue("")) }
        Row {
            OutlinedTextField(
                value = search,
                leadingIcon = { Icon(Icons.Default.AccountBox, contentDescription = "Phone & Name") },
                modifier = Modifier.padding(8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                label = { Text(text = "Phone or Number") },
                placeholder = { Text(text = "010-3020-6909 or 홍길동") },
                onValueChange = {
                    search = it
                }
            )
            Button(onClick = { donationRepository.query() }, modifier = Modifier.padding(8.dp)) {
                Text(text = "검색")
            }
        }
    }

}
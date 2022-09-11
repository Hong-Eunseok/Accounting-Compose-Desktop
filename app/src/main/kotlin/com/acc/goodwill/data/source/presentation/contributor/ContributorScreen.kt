package com.acc.goodwill.data.source.presentation.contributor

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.acc.common.components.AppIcon
import com.acc.common.components.OptionButton
import com.acc.common.components.RowTextField
import com.acc.common.ui.largePadding
import com.acc.di.AppComponent
import com.acc.goodwill.data.source.presentation.donation.DonationViewModel
import com.acc.goodwill.domain.model.Contributor
import com.acc.goodwill.domain.model.CreateContributorResult
import com.acc.goodwill.domain.model.CreateContributorState
import com.acc.goodwill.domain.model.rememberContributor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named


class ContributorScreen(appComponent: AppComponent) {

    @Named("main") @Inject lateinit var mainScope: CoroutineScope
    @Inject lateinit var viewModel: DonationViewModel
    init {
        appComponent.inject(this)
    }

    @Composable
    fun ContributorScreen(
        navigateBack: () -> Unit,
        modifyContributor: Contributor? = null
    ) {
        val scaffoldState = rememberScaffoldState()
        val result by viewModel.result.collectAsState()
        val contributor = rememberContributor()
        val radioOptions = listOf("교인", "인터넷", "지인소개", "기타")
        val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }

        val registerString = if (modifyContributor == null) "등록" else "수정"

        when (result) {
            CreateContributorResult.SUCCESS -> "${registerString}에 성공하였습니다."
            CreateContributorResult.ERROR -> "${registerString}에 실패하였습니다."
            else -> null
        }?.let { message ->
            mainScope.launch {
                scaffoldState.snackbarHostState.showSnackbar(message)
                navigateBack()
            }
        }

        modifyContributor?.let {
            contributor.name = it.name
            contributor.phoneNumber = it.phoneNumber.orEmpty()
            contributor.address = it.address.orEmpty()
            contributor.registrationNumber = it.registrationNumber.orEmpty()
            contributor.registrationType = it.registrationType
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "기부자 정보 입력", style = MaterialTheme.typography.h4) },
                    navigationIcon = {
                        IconButton(onClick = navigateBack) { AppIcon(imageVector = Icons.Default.ArrowBack) }
                    }
                )
            },
            scaffoldState = scaffoldState
        ) {
            Box(
                modifier = Modifier.fillMaxSize().padding(top = largePadding),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    RowTextField(
                        value = contributor.name,
                        setValue = {
                            contributor.name = it
                        },
                        label = "이름",
                        modifier = Modifier.padding(bottom = largePadding),
                        deleteLastChar = { contributor.name = contributor.name.substring(0 until contributor.name.length - 1) },
                        errorMessage = takeIf { contributor.valid == CreateContributorState.Validator.NAME_ERROR }?.run { "이름을 입력하세요 (필수 입력)" }
                    )

                    RowTextField(
                        value = contributor.phoneNumber,
                        setValue = { value ->
                            contributor.phoneNumber = value.filter { it.isDigit() || it == '-' }
                        },
                        label = "연락처",
                        modifier = Modifier.padding(bottom = largePadding),
                        errorMessage = takeIf { contributor.valid == CreateContributorState.Validator.PHONE_NUMBER_ERROR }?.run { "전화번호가 잘못 입력되었습니다." }
                    )

                    RowTextField(
                        value = contributor.registrationNumber,
                        setValue = { value ->
                            contributor.registrationNumber = value.filter { it.isDigit() || it == '-' }
                        },
                        label = "주민/사업자 번호",
                        modifier = Modifier.padding(bottom = largePadding),
                        errorMessage = takeIf { contributor.valid == CreateContributorState.Validator.REGISTRATION_NUMBER_ERROR }?.run { "주민/사업자 번호가 잘못입력하였습니다." }
                    )

                    RowTextField(
                        value = contributor.address,
                        setValue = { contributor.address = it },
                        label = "주소",
                        modifier = Modifier.padding(bottom = largePadding),
                        deleteLastChar = { contributor.address = contributor.address.substring(0 until contributor.address.length - 1) }
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "가입경로", modifier = Modifier.padding(end = largePadding))
                        radioOptions.forEach { text ->
                            OptionButton(
                                text = text,
                                selectedOption = selectedOption,
                                onClick = { onOptionSelected(text) }
                            )
                        }
                    }

                    Row {
                        Spacer(modifier = Modifier.weight(1f))
                        Button(
                            onClick = {
                                if (modifyContributor == null) {
                                    viewModel.addContributor(contributor, radioOptions.indexOf(selectedOption))
                                } else {
                                    viewModel.modifyContributor(
                                        contributor,
                                        radioOptions.indexOf(selectedOption),
                                        modifyContributor.primaryKey.value
                                    )
                                }

                            },
                            enabled = contributor.valid == CreateContributorState.Validator.VALID,
                            modifier = Modifier.padding(horizontal = largePadding)
                        ) {
                            Text(registerString)
                        }
                    }
                }
            }
        }
    }
}


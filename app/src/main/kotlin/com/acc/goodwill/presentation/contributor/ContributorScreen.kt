package com.acc.goodwill.presentation.contributor

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
import com.acc.goodwill.domain.model.Contributor
import com.acc.goodwill.domain.model.CreateContributorResult
import com.acc.goodwill.domain.model.CreateContributorState
import com.acc.goodwill.presentation.donation.DonationViewModel
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
        contributor: CreateContributorState,
        modifyContributor: Contributor? = null,
    ) {
        val scaffoldState = rememberScaffoldState()
        val result by viewModel.result.collectAsState()
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
            Box(modifier = Modifier.fillMaxSize().padding(largePadding)) {
                Column {
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

                    Text(text = "가입경로", modifier = Modifier.padding(end = largePadding))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CreateContributorState.RECOMMAND.forEach { text ->
                            OptionButton(
                                text = text,
                                selectedOption = contributor.recommand,
                                onClick = { contributor.recommand = text }
                            )
                        }
                    }

                    Row {
                        Spacer(modifier = Modifier.weight(1f))
                        Button(
                            onClick = {
                                if (modifyContributor == null) {
                                    viewModel.addContributor(contributor)
                                } else {
                                    viewModel.modifyContributor(
                                        contributor,
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


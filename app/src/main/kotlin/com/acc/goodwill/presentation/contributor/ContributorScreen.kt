package com.acc.goodwill.presentation.contributor

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.acc.common.components.AppIcon
import com.acc.common.components.OptionButton
import com.acc.common.components.RowTextField
import com.acc.common.components.SwingsRowTextField
import com.acc.common.ui.largePadding
import com.acc.common.ui.mediumPadding
import com.acc.di.AppComponent
import com.acc.goodwill.domain.model.Contributor
import com.acc.goodwill.domain.model.CreateContributorResult
import com.acc.goodwill.domain.model.CreateContributorState
import com.acc.goodwill.presentation.donation.DonationViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named
import javax.swing.JTextField


class ContributorScreen(appComponent: AppComponent) {

    @Named("main") @Inject lateinit var mainScope: CoroutineScope
    @Inject lateinit var viewModel: DonationViewModel

    private val nameTextField = JTextField("")
    private val addressTextField = JTextField("")

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
                    SwingsRowTextField(
                        nameTextField,
                        label = "이름",
                        errorMessage = takeIf { contributor.validCustom == CreateContributorState.Validator.NAME_ERROR }?.run { "이름을 입력하세요 (필수 입력)" }
                    )

                    RowTextField(
                        value = contributor.phoneNumber,
                        setValue = { value ->
                            contributor.phoneNumber = value.filter { it.isDigit() || it == '-' }
                        },
                        label = "연락처",
                        modifier = Modifier.padding(bottom = largePadding),
                        errorMessage = takeIf { contributor.validCustom == CreateContributorState.Validator.PHONE_NUMBER_ERROR }?.run { "전화번호가 잘못 입력되었습니다." },
                        focusChanged = { contributor.checkValid(nameTextField.text) }
                    )

                    RowTextField(
                        value = contributor.registrationNumber,
                        setValue = { value ->
                            contributor.registrationNumber = value.filter { it.isDigit() || it == '-' }
                        },
                        label = "주민/사업자 번호",
                        modifier = Modifier.padding(bottom = largePadding),
                        errorMessage = takeIf { contributor.validCustom == CreateContributorState.Validator.REGISTRATION_NUMBER_ERROR }?.run { "주민/사업자 번호가 잘못입력하였습니다." },
                        focusChanged = { contributor.checkValid(nameTextField.text) }
                    )

                    SwingsRowTextField(
                        addressTextField,
                        label = "주소",
                    )

                    Text(text = "가입경로", modifier = Modifier.padding(end = largePadding))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CreateContributorState.RECOMMEND.forEach { text ->
                            OptionButton(
                                text = text,
                                selectedOption = contributor.recommand,
                                onClick = {
                                    contributor.recommand = text
                                    contributor.checkValid(nameTextField.text)
                                }
                            )
                        }
                    }
                    if (contributor.validCustom == CreateContributorState.Validator.RECOMMEND) {
                        Text(
                            text = "가입경로를 선택해주세요.",
                            color = com.acc.common.ui.error,
                            style = MaterialTheme.typography.caption,
                            modifier = Modifier.padding(start = 140.dp, bottom = mediumPadding)
                        )
                    }

                    Row {
                        Spacer(modifier = Modifier.weight(1f))
                        Button(
                            onClick = {
                                if (contributor.checkValid(nameTextField.text)) {
                                    if (modifyContributor == null) {
                                        viewModel.addContributor(contributor)
                                    } else {
                                        viewModel.modifyContributor(
                                            contributor,
                                            modifyContributor.primaryKey.value
                                        )
                                    }
                                }
                            },
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


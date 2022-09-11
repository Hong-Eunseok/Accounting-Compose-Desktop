package com.acc.goodwill.data.source.presentation.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.window.AwtWindow
import com.acc.common.components.AppIcon
import com.acc.common.components.AppTextField
import com.acc.di.AppComponent
import java.awt.FileDialog
import java.awt.Frame
import javax.inject.Inject


class SettingScreen(appComponent: AppComponent) {
    @Inject lateinit var viewModel: SettingViewModel

    init {
        appComponent.inject(this)
    }

    @Composable
    fun SettingsScreen(
        navigateBack: () -> Unit
    ) {
        var year by remember { mutableStateOf("2022") }
        var isFileChooserOpen by remember { mutableStateOf(false) }
        var filePath by remember { mutableStateOf("") }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "csv import", style = MaterialTheme.typography.h4)
                    },
                    navigationIcon = {
                        IconButton(onClick = navigateBack) { AppIcon(imageVector = Icons.Default.ArrowBack) }
                    }
                )
            },
        ) {
            Column {
                AppTextField(
                    year,
                    { year = it },
                    "날짜입력"
                )
                Button(
                    onClick = { isFileChooserOpen = !isFileChooserOpen }
                ) {
                    Text("파일 열기")
                }
                Button(
                    onClick = { viewModel.fileOpen(filePath, year.toInt()) },
                    enabled = filePath.isNotEmpty()
                ) {
                    Text("시작")
                }
            }
        }

        if (isFileChooserOpen) {
            FileDialog(
                onCloseRequest = {
                    isFileChooserOpen = false
                    filePath = it.orEmpty()
                    println("path : $filePath")
                }
            )
        }
    }

    @Composable
    private fun FileDialog(
        parent: Frame? = null,
        onCloseRequest: (result: String?) -> Unit
    ) = AwtWindow(
        create = {
            object : FileDialog(parent, "Choose a file", LOAD) {
                override fun setVisible(value: Boolean) {
                    super.setVisible(value)

                    if (value) {
                        onCloseRequest(directory+file)
                    }
                }
            }
        },
        dispose = FileDialog::dispose
    )

}
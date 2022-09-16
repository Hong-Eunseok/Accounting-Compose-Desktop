package com.acc.goodwill.presentation.report

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.acc.common.components.AppIcon
import com.acc.di.AppComponent
import com.acc.goodwill.domain.model.SnackbarResult
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

class ReportContent(appComponent: AppComponent) {

    @Inject lateinit var reportViewModel: ReportViewModel

    private val years: List<String> by lazy {
        val year = LocalDateTime.now().year
        mutableListOf<String>().apply {
            for (i in 1..10) add("${year - i}")
            for (i in 0..20) add("${year + i}")
        }.also {
            it.sort()
        }
    }

    init {
        appComponent.inject(this)
    }

    @Composable
    fun ReportContent() {
        var showCompanies by remember { mutableStateOf(false) }
        var selectedYear by remember { mutableStateOf("${LocalDateTime.now().year}") }
        val coroutineScope = rememberCoroutineScope()
        val scaffoldState = rememberScaffoldState()
        val result by reportViewModel.result.collectAsState()

        when (result) {
            SnackbarResult.SUCCESS -> "성공하였습니다."
            SnackbarResult.FAILED -> "실패하였습니다."
            else -> null
        }?.let { message ->
            coroutineScope.launch {
                scaffoldState.snackbarHostState.showSnackbar(message)
                reportViewModel.idleSnackbar()
            }
        }

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            scaffoldState = scaffoldState
        ) {
            Column {
                Box {
                    OutlinedButton(
                        onClick = { showCompanies = true },
                        modifier = Modifier.fillMaxWidth(0.5f)
                    ) {
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier) {
                            Text(text = selectedYear)
                            AppIcon(imageVector = if (showCompanies) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown)
                        }
                    }
                    DropdownMenu(
                        expanded = showCompanies,
                        onDismissRequest = { showCompanies = false },
                        modifier = Modifier
                    ) {
                        years.forEachIndexed { index, value ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedYear = value
                                    showCompanies = false
                                }
                            ) {
                                Text(text = value)
                            }
                        }
                    }
                }

                Button(
                    onClick = { reportViewModel.makeMonthReport(selectedYear.toInt()) }
                ) {
                    Text("기증 품목 보고서")
                }

                Button(
                    onClick = { }
                ) {
                    Text("월별 기증 명단")
                }
            }
        }
    }

}

/** initializeComponents, This creates the user interface for the basic demo.  */

//{
//    val jFrame = JFrame()
//    jFrame.setTitle("LGoodDatePicker Basic Demo " + InternalUtilities.getProjectVersionString())
//    jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
//    jFrame.setLayout(FlowLayout())
//    jFrame.setSize(Dimension(640, 480))
//    jFrame.setLocationRelativeTo(null)
//
//    // Create a date picker, and add it to the form.
//
//    // Create a date picker, and add it to the form.
//
//
//
//    // Create a time picker, and add it to the form.
//
//    // Create a time picker, and add it to the form.
//    val timePicker1 = TimePicker()
//    jFrame.add(timePicker1)
//
//    /**
//     * The code below shows: 1) How to create a DateTimePicker (with default settings), 2) How to
//     * create a DatePicker with some custom settings, and 3) How to create a TimePicker with some
//     * custom settings. To keep the Basic Demo interface simpler, the lines for adding these
//     * components to the form have been commented out.
//     */
//
//    /**
//     * The code below shows: 1) How to create a DateTimePicker (with default settings), 2) How to
//     * create a DatePicker with some custom settings, and 3) How to create a TimePicker with some
//     * custom settings. To keep the Basic Demo interface simpler, the lines for adding these
//     * components to the form have been commented out.
//     */
//    // Create a DateTimePicker. (But don't add it to the form).
//    /**
//     * The code below shows: 1) How to create a DateTimePicker (with default settings), 2) How to
//     * create a DatePicker with some custom settings, and 3) How to create a TimePicker with some
//     * custom settings. To keep the Basic Demo interface simpler, the lines for adding these
//     * components to the form have been commented out.
//     */
//    /**
//     * The code below shows: 1) How to create a DateTimePicker (with default settings), 2) How to
//     * create a DatePicker with some custom settings, and 3) How to create a TimePicker with some
//     * custom settings. To keep the Basic Demo interface simpler, the lines for adding these
//     * components to the form have been commented out.
//     */
//
//    // Create a DateTimePicker. (But don't add it to the form).
//    val dateTimePicker1 = DateTimePicker()
//    // To display this picker, uncomment this line.
//    // add(dateTimePicker1);
//
//    // Create a date picker with some custom settings.
//    // To display this picker, uncomment this line.
//    // add(dateTimePicker1);
//
//    // Create a date picker with some custom settings.
//    val dateSettings = DatePickerSettings()
//    dateSettings.setFirstDayOfWeek(DayOfWeek.MONDAY)
//    val datePicker2 = DatePicker(dateSettings)
//    // To display this picker, uncomment this line.
//    // add(datePicker2);
//
//    // Create a time picker with some custom settings.
//    // To display this picker, uncomment this line.
//    // add(datePicker2);
//
//    // Create a time picker with some custom settings.
//    val timeSettings = TimePickerSettings()
//    timeSettings.setColor(TimeArea.TimePickerTextValidTime, Color.blue)
//    timeSettings.initialTime = LocalTime.now()
//    val timePicker2 = TimePicker(timeSettings)
//    // To display this picker, uncomment this line.
//    // add(timePicker2);
//
//    val panel = ComposePanel()
//
//}
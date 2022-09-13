package com.acc.goodwill.presentation.report

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JPanel
import javax.swing.JTextField

val test = JTextField("")
@Composable
fun ReportContent() {
    val rgb: Int = MaterialTheme.colors.background.toArgb()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("hello report")
            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier.fillMaxWidth().height(40.dp)
            ) {
                Box(
                    modifier = Modifier.width(100.dp).height(40.dp)
                ) {
                    SwingPanel(
                        factory = {
                            JPanel().apply {
                                background = java.awt.Color(rgb)
                                add(
                                    test.apply { preferredSize = Dimension(100, 40) },
                                    BorderLayout.EAST
                                )
                            }
                        }
                    )
            }




//                SwingPanel(
////                    factory = {
////                        JPanel().apply {
//////                            val datePicker1 = DatePicker()
//////                            datePicker1.addDateChangeListener { println(datePicker1.date) }
////                            val jtextFiled1 = JTextField("")
//////                            val jtextFiled2 = JTextField("")
////                            jtextFiled1.preferredSize = Dimension(100, 40)
//////                            jtextFiled2.preferredSize = Dimension(100, 50)
//////                            jtextFiled2.addActionListener {
//////                                println(jtextFiled2.text)
//////                            }
////                            add(jtextFiled1, BorderLayout.CENTER)
//////                            add(jtextFiled2)
////                        }
////                    },
//                    factory = {
//                        ComposePanel().apply {
//                            setContent {
//                                Box {
//
//                                }
//                            }
//                            val jtextFiled1 = JTextField("")
//                            jtextFiled1.preferredSize = Dimension(100, 40)
//                            add(jtextFiled1, BorderLayout.CENTER)
//                        }
//                    },
//                )
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
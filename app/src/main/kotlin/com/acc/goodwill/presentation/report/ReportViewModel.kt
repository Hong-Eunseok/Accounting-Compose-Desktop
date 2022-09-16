package com.acc.goodwill.presentation.report

import com.acc.goodwill.data.source.DonationDao
import com.acc.goodwill.domain.model.MonthlyStatics
import com.acc.goodwill.domain.model.SnackbarResult
import io.github.evanrupert.excelkt.Sheet
import io.github.evanrupert.excelkt.workbook
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.util.CellRangeAddress
import java.io.File
import javax.inject.Inject
import javax.inject.Named

class ReportViewModel @Inject constructor(
    private val donationDao: DonationDao,
    @Named("io") private val ioCoroutineScope: CoroutineScope
) {

    private val _result: MutableStateFlow<SnackbarResult> = MutableStateFlow(SnackbarResult.IDLE)
    val result: StateFlow<SnackbarResult> = _result

    fun makeMonthReport(year: Int) {
        ioCoroutineScope.launch {
            writeExcelMain(donationDao.queryMonths(year))
        }
    }

    suspend fun idleSnackbar() {
        _result.emit(SnackbarResult.IDLE)
    }

    private suspend fun writeExcelMain(results: List<MonthlyStatics>) {
        val path = System.getProperty("user.home") + "\\Documents\\"
        val testFile = File("test.xlsx")
        if (testFile.isFile) testFile.delete()
        workbook {
            sheet("월별기증품목합계") {
                title("월별기증품목관리")
                header()
                monthReport(results)
            }

            sheet("주별기증품목합계") {
                title("주별기증품목관리")
                header()
                weeklyReport(results)
            }
        }.write("${path}report_${System.currentTimeMillis()}.xlsx")
        _result.emit(SnackbarResult.SUCCESS)
        println("finished!")
    }

    fun Sheet.title(content: String) {
        xssfSheet.addMergedRegion(CellRangeAddress(0, 1, 0, 15))
        row {
            cell(
                content,
                createCellStyle {
                    setFont(
                        createFont {
                            bold = true
                            fontHeightInPoints = 36
                        }
                    )
                    alignment = HorizontalAlignment.CENTER
                }
            )
            xssfRow.heightInPoints = 36f
        }
    }

    fun Sheet.header() {
        val headings = listOf(
            "NO",
            "순번",
            "날짜",
            "성명",
            "핸드폰번호",
            "주소",
            "기증품목 / 수량",
            "개인/기관 (교)",
            "발행여부",
            "기증통로",
            "합계",
            "불량",
            "양품",
            "합계금액",
            "의류",
            "불량",
            "양품",
            "생활",
            "유아",
            "불량",
            "양품",
            "완구/문구",
            "불량",
            "양품",
            "도서",
            "불량",
            "양품",
            "가구",
            "가전",
            "불량",
            "양품",
            "주방",
            "기타",
            "불량",
            "양품"
        )
        row(
            style = createCellStyle {
                setFont(createFont {
                    bold = true
                    fontHeightInPoints = 11
                })
                alignment = HorizontalAlignment.CENTER
            },
            init = {
                xssfRow.rowNum = 4
                headings.forEach { cell(it) }
            }
        )
    }

}




fun Sheet.customersHeader() {
    val headings = listOf("Id", "Name", "Address", "Age")

    val headingStyle = createCellStyle {
        setFont(createFont {
            fontName = "IMPACT"
            color = IndexedColors.PINK.index
        })

        fillPattern = FillPatternType.SOLID_FOREGROUND
        fillForegroundColor = IndexedColors.AQUA.index
    }

    row(headingStyle) {
        headings.forEach { cell(it) }
    }
}



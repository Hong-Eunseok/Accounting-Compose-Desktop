package com.acc.goodwill.presentation.report

import com.acc.goodwill.data.source.DonationDao
import com.acc.goodwill.domain.model.MonthlyStatics
import com.acc.goodwill.domain.model.PeopleCellStyle
import com.acc.goodwill.domain.model.ReportCellStyle
import com.acc.goodwill.domain.model.SnackbarResult
import io.github.evanrupert.excelkt.Sheet
import io.github.evanrupert.excelkt.workbook
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.IndexedColors
import javax.inject.Inject
import javax.inject.Named

class ReportViewModel @Inject constructor(
    private val donationDao: DonationDao,
    @Named("io") private val ioCoroutineScope: CoroutineScope
) {

    private val path = System.getProperty("user.home") + "\\Documents\\"

    private val staticsHeader = listOf(
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

    private val monthHeader = listOf(
        "월", "명", "총수량", "불량", "양품", "합계금액", "의류", "불량", "양품", "생활", "유아", "불량", "양품", "완구/문구", "불량", "양품", "도서", "불량", "양품", "가구", "가전", "불량", "양품", "주방", "기타", "불량", "양품"
    )

    private val _result: MutableStateFlow<SnackbarResult> = MutableStateFlow(SnackbarResult.IDLE)
    val result: StateFlow<SnackbarResult> = _result

    fun makeMonthReport(year: Int) {
        ioCoroutineScope.launch {
            _result.emit(SnackbarResult.PROCESSING)
            writeExcelMain(donationDao.queryMonths(year), year)
        }
    }

    fun makePeopleReport(year: Int) {
        ioCoroutineScope.launch {
            _result.emit(SnackbarResult.PROCESSING)
            writePeopleReport(donationDao.queryMonths(year), year)
        }
    }

    suspend fun idleSnackbar() {
        _result.emit(SnackbarResult.IDLE)
    }

    private suspend fun writeExcelMain(results: List<MonthlyStatics>, year: Int) {

        workbook {
            val reportStyle = ReportCellStyle.instance(this)
            sheet("월별기증품목합계") {
                title("월별기증품목관리")
                header(year, reportStyle)
                header(staticsHeader, reportStyle)
                monthReport(results, reportStyle)
            }

            sheet("주별기증품목합계") {
                title("주별기증품목관리")
                header(year, reportStyle)
                header(staticsHeader, reportStyle)
                weeklyReport(results, reportStyle)
            }

            sheet("월별전체합계") {
                title("월별전체합계")
                header(monthHeader, reportStyle)
                totalReport(results, year, reportStyle)
            }

        }.write("${path}${year} 기증품목관리보고서${System.currentTimeMillis()}.xlsx")
        _result.emit(SnackbarResult.SUCCESS)
    }

    private suspend fun writePeopleReport(results: List<MonthlyStatics>, year: Int) {
        workbook {
            val reportStyle = PeopleCellStyle.instance(this)
            results.forEach { month ->
                sheet("${month.month}월 기증 명단") {
                    peopleTitle(year, month.month, reportStyle)
                    goodwillstoreHeader(reportStyle)
                    peopleHeader(reportStyle)
                    writhePeopleContent(month.statics, reportStyle)
                }
            }
        }.write("${path}${year} 기증 명단 보고서_${System.currentTimeMillis()}.xlsx")
        _result.emit(SnackbarResult.SUCCESS)
    }

}

fun Sheet.customersHeader() {
    val headings = listOf("Id", "Name", "Address", "Age")

    val headingStyle = createCellStyle {
        setFont(createFont {
            fontName = "IMPACT"
            color = IndexedColors.PINK.index
        })

        fillPattern = FillPatternType.NO_FILL
        fillForegroundColor = IndexedColors.AQUA.index
    }

    row(headingStyle) {
        headings.forEach { cell(it) }
    }
}



package com.acc.goodwill.presentation.report

import com.acc.goodwill.data.source.DonationDao
import com.acc.goodwill.domain.model.Donate
import com.acc.goodwill.domain.model.ExcelData
import io.github.evanrupert.excelkt.Formula
import io.github.evanrupert.excelkt.Row
import io.github.evanrupert.excelkt.Sheet
import io.github.evanrupert.excelkt.workbook
import kotlinx.coroutines.CoroutineScope
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

    fun makeMonthReport() {
        ioCoroutineScope.launch {
            writeExcelMain(donationDao.queryMonth())
        }
    }

}

private fun writeExcelMain(results: List<ExcelData>) {
    val testFile = File("test.xlsx")
    if (testFile.isFile) testFile.delete()
    workbook {
        sheet("8월 보고서") {
            title()
            header()
            contents(results)
        }

    }.write("test.xlsx")
    println("finished!")
}

fun Sheet.title() {
    xssfSheet.addMergedRegion(CellRangeAddress(0, 1, 0, 15))
    row {
        cell(
            "월별 기증 품목 관리 대장",
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

fun Sheet.contents(results: List<ExcelData>) {
    val dateStyle = createCellStyle {
        dataFormat = xssfWorkbook.creationHelper.createDataFormat().getFormat("mm/dd")
    }
    val priceStyle = createCellStyle {
        dataFormat = xssfWorkbook.creationHelper.createDataFormat().getFormat("#,##0")
    }

    results.forEachIndexed { index, excelData ->
        row(
            init = {
                xssfRow.rowNum = 5 + index

                val donation = excelData.donation.donate
                val contributor = excelData.donation.contributor
                val products = excelData.products

                cell(index + 1)
                cell(donation.createAt, dateStyle)
                cell(contributor?.name?.ifEmpty { "무명" } ?: "무명")
                cell(contributor?.phoneNumber.orEmpty())
                cell(contributor?.address.orEmpty())

                val productString = excelData.products.mapNotNull {
                    buildString {
                        append(it.label)
                        if (it.error == 0u) {
                            append(it.total)
                        } else {
                            append(it.total).append('/').append(it.error)
                        }
                    }.takeIf { it.isNotEmpty() }
                }.joinToString(" ").trim()
                if (productString.isNotEmpty()) {
                    cell(productString)
                } else {
                    cell(donation.optionParsingData.orEmpty())
                }
                val org = buildString {
                    append(Donate.ORGANIZATION[donation.organization])
                    if (donation.member) append(" (교)")
                }
                cell(org)
                cell(contributor?.registrationNumber.orEmpty())
                cell(Donate.FROM_TYPE[donation.fromType])
                cell(donation.total)
                cell(donation.error)
                cell(donation.correct)
                cell(donation.price.toLong(), priceStyle)

                // "의류", "불량", "양품"
                cell(products.filter { it.category == 0 }.sumOf { it.total }.takeUnless { it == 0u }
                    ?.toInt() ?: "")
                cell(products.filter { it.category == 0 }.sumOf { it.error }.takeUnless { it == 0u }
                    ?.toInt() ?: "")
                cell(products.filter { it.category == 0 }.sumOf { it.correct }
                    .takeUnless { it == 0u }?.toInt() ?: "")

                // "생활", "유아", "불량", "양품"
                cell(products.filter { it.category == 2 }.sumOf { it.total }.takeUnless { it == 0u }
                    ?.toInt() ?: "")
                cell(products.filter { it.category == 3 }.sumOf { it.total }.takeUnless { it == 0u }
                    ?.toInt() ?: "")
                cell(products.filter { it.category in 2..3 }.sumOf { it.error }
                    .takeUnless { it == 0u }?.toInt() ?: "")
                cell(products.filter { it.category in 2..3 }.sumOf { it.correct }
                    .takeUnless { it == 0u }?.toInt() ?: "")

                // "완구/문구", "불량", "양품"
                cell(products.filter { it.category == 4 }.sumOf { it.total }.takeUnless { it == 0u }
                    ?.toInt() ?: "")
                cell(products.filter { it.category == 4 }.sumOf { it.error }.takeUnless { it == 0u }
                    ?.toInt() ?: "")
                cell(products.filter { it.category == 4 }.sumOf { it.correct }
                    .takeUnless { it == 0u }?.toInt() ?: "")

                // "도서", "불량", "양품"
                cell(products.filter { it.category == 1 }.sumOf { it.total }.takeUnless { it == 0u }
                    ?.toInt() ?: "")
                cell(products.filter { it.category == 1 }.sumOf { it.error }.takeUnless { it == 0u }
                    ?.toInt() ?: "")
                cell(products.filter { it.category == 1 }.sumOf { it.correct }
                    .takeUnless { it == 0u }?.toInt() ?: "")

                // "가구", "가전", "불량", "양품"
                cell(products.filter { it.category == 5 }.sumOf { it.total }.takeUnless { it == 0u }
                    ?.toInt() ?: "")
                cell(products.filter { it.category == 6 }.sumOf { it.total }.takeUnless { it == 0u }
                    ?.toInt() ?: "")
                cell(products.filter { it.category in 5..6 }.sumOf { it.error }
                    .takeUnless { it == 0u }?.toInt() ?: "")
                cell(products.filter { it.category in 5..6 }.sumOf { it.correct }
                    .takeUnless { it == 0u }?.toInt() ?: "")

                // "주방", "기타", "불량", "양품"
                cell(products.filter { it.category == 7 }.sumOf { it.total }.takeUnless { it == 0u }
                    ?.toInt() ?: "")
                cell(products.filter { it.category == 8 }.sumOf { it.total }.takeUnless { it == 0u }
                    ?.toInt() ?: "")
                cell(products.filter { it.category in 7..8 }.sumOf { it.error }
                    .takeUnless { it == 0u }?.toInt() ?: "")
                cell(products.filter { it.category in 7..8 }.sumOf { it.correct }
                    .takeUnless { it == 0u }?.toInt() ?: "")
            }
        )
    }

    val lastRow = 5 + results.size
    xssfSheet.addMergedRegion(CellRangeAddress(lastRow, lastRow, 0, 8))
    row(
        init = {
            xssfRow.rowNum = lastRow
            cell("최종합계")
            for (i in 0..7) skip()
            val columnName = listOf("J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "AA", "AB", "AC", "AD", "AE", "AF", "AG", "AH")
            columnName.forEach {
                cell(Formula("SUM(${it}6:${it}$lastRow)"), priceStyle)
            }
        }
    )

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

fun Row.skip() = cell("")

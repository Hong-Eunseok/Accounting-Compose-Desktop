package com.acc.goodwill.presentation.report

import com.acc.goodwill.domain.model.*
import io.github.evanrupert.excelkt.ExcelElement
import io.github.evanrupert.excelkt.Formula
import io.github.evanrupert.excelkt.Row
import io.github.evanrupert.excelkt.Sheet
import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.atomic.AtomicInteger


private val borderStyle = BorderStyle.THIN

fun Sheet.monthReport(
    results: List<MonthlyStatics>,
    reportCellStyle: ReportCellStyle,
    no: AtomicInteger = AtomicInteger(1),
    lastRow: AtomicInteger = AtomicInteger(5),
) {
    results.filter { it.statics.isNotEmpty() }.forEach { month ->
        month.statics.forEachIndexed { index, donationStatics ->
            row(
                style = createCellStyle {
                    borderTop = borderStyle
                    borderLeft = borderStyle
                    borderBottom = borderStyle
                    borderRight= borderStyle
                },
                init = {
                    xssfRow.rowNum = lastRow.get() + index
                    val donation = donationStatics.donation.donate
                    val contributor = donationStatics.donation.contributor
                    val products = donationStatics.products
                    writeDonationCell(no, index, donation, contributor, products, reportCellStyle)
                }
            )
        }

        val sumRow = lastRow.addAndGet(month.statics.size)
        xssfSheet.addMergedRegion(CellRangeAddress(sumRow, sumRow, 0, 9))
        row(
            init = {
                xssfRow.rowNum = sumRow
                cell("${month.month}월 최종합계", reportCellStyle.totalTitleStyle)
                skip(9, reportCellStyle.totalTitleStyle)
                val columnName = listOf("K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "AA", "AB", "AC", "AD", "AE", "AF", "AG", "AH", "AI")
                columnName.forEach {
                    cell(Formula("SUM(${it}${sumRow - month.statics.size + 1}:${it}$sumRow)"), reportCellStyle.totalContentStyle)
                }
            }
        )
        lastRow.incrementAndGet()
    }
}

fun Sheet.weeklyReport(
    results: List<MonthlyStatics>,
    reportCellStyle: ReportCellStyle,
    no: AtomicInteger = AtomicInteger(1),
    lastRow: AtomicInteger = AtomicInteger(5),
    lastWeek: AtomicInteger = AtomicInteger(-1),
    indexWeek: AtomicInteger = AtomicInteger(-1)
) {
    val calendar = Calendar.getInstance()
    results.filter { it.statics.isNotEmpty() }.forEach { month ->
        month.statics.forEach { donationStatics ->
            val donation = donationStatics.donation.donate
            val contributor = donationStatics.donation.contributor
            val products = donationStatics.products

            calendar.set(donation.createAt.year, donation.createAt.monthValue - 1, donation.createAt.dayOfMonth)
            val before = lastWeek.getAndSet(calendar.get(Calendar.WEEK_OF_YEAR))
            if (before != -1 && lastWeek.get() != before) {
                sumTitle(lastRow, indexWeek, reportCellStyle, before)
            }

            val index = indexWeek.incrementAndGet()
            row(init = {
                xssfRow.rowNum = lastRow.get() + index
                writeDonationCell(no, index, donation, contributor, products, reportCellStyle)
            })
        }
    }

    if (lastWeek.get() != -1) {
        sumTitle(lastRow, indexWeek, reportCellStyle, lastWeek.get())
    }

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
fun Sheet.header(year: Int, reportCellStyle: ReportCellStyle) {
    xssfSheet.addMergedRegion(CellRangeAddress(3, 3, 1, 5))
    xssfSheet.addMergedRegion(CellRangeAddress(3, 3, 10, 13))
    xssfSheet.addMergedRegion(CellRangeAddress(3, 3, 14, 16))
    xssfSheet.addMergedRegion(CellRangeAddress(3, 3, 17, 20))
    xssfSheet.addMergedRegion(CellRangeAddress(3, 3, 21, 26))
    xssfSheet.addMergedRegion(CellRangeAddress(3, 3, 27, 34))

    val dateFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd")
    val now = LocalDateTime.now()
    val beforeDate = now.withYear(year).withMonth(1).withDayOfMonth(1).run { dateFormat.format(this) }
    val afterDate = now.withYear(year).withMonth(12).withDayOfMonth(31).run { dateFormat.format(this) }
    row(
        init = {
            xssfRow.rowNum = 3
            skip()
            cell("${beforeDate}~${afterDate}", reportCellStyle.boldStyle)
            skip(4, reportCellStyle.boldStyle)
            cell("수원굿윌스토어", reportCellStyle.boldStyle)
            skip(3)
            cell("전체합계", reportCellStyle.sumStyle)
            skip(3, reportCellStyle.sumStyle)
            cell("의류", reportCellStyle.categoryStyle)
            skip(2, reportCellStyle.categoryStyle)
            cell("잡화", reportCellStyle.categoryStyle)
            skip(3, reportCellStyle.categoryStyle)
            cell("문구완구도서", reportCellStyle.categoryStyle)
            skip(5, reportCellStyle.categoryStyle)
            cell("가구/가전/주방/기타", reportCellStyle.categoryStyle)
            skip(7, reportCellStyle.categoryStyle)
        }
    )
}
fun Sheet.header(headings: List<String>, reportCellStyle: ReportCellStyle) {
    row(init = {
        xssfRow.rowNum = 4
        headings.forEach { cell(it, reportCellStyle.headerStyle) }
    })
}

fun Sheet.totalReport(
    results: List<MonthlyStatics>,
    year: Int
) {
    val priceStyle = createCellStyle {
        dataFormat = xssfWorkbook.creationHelper.createDataFormat().getFormat("#,##0")
    }

    results.forEachIndexed { index, month ->
        row(init = {
            xssfRow.rowNum = 5 + index
            cell("${month.month}월말 기증 전체 합계")
            if (month.statics.isNotEmpty()) {
                cell(month.statics.size)
                cell(month.statics.sumOf { it.donation.donate.total })
                cell(month.statics.sumOf { it.donation.donate.error })
                cell(month.statics.sumOf { it.donation.donate.correct })
                cell(month.statics.sumOf { it.donation.donate.price }.toLong())

                val products = month.statics.map { it.products }.flatten()
                // "의류", "불량", "양품"
                cell(products.filter { it.category == 0 }.sumOf { it.total }.toLong())
                cell(products.filter { it.category == 0 }.sumOf { it.error }.toLong())
                cell(products.filter { it.category == 0 }.sumOf { it.correct }.toLong())
                // "생활", "유아", "불량", "양품"
                cell(products.filter { it.category == 2 }.sumOf { it.total }.toLong())
                cell(products.filter { it.category == 3 }.sumOf { it.total }.toLong())
                cell(products.filter { it.category == 2 || it.category == 3 }.sumOf { it.error }.toLong())
                cell(products.filter { it.category == 2 || it.category == 3 }.sumOf { it.correct }.toLong())
                // "완구/문구", "불량", "양품"
                cell(products.filter { it.category == 4 }.sumOf { it.total }.toLong())
                cell(products.filter { it.category == 4 }.sumOf { it.error }.toLong())
                cell(products.filter { it.category == 4 }.sumOf { it.correct }.toLong())
                // "도서", "불량", "양품"
                cell(products.filter { it.category == 1 }.sumOf { it.total }.toLong())
                cell(products.filter { it.category == 1 }.sumOf { it.error }.toLong())
                cell(products.filter { it.category == 1 }.sumOf { it.correct }.toLong())
                // "가구", "가전", "불량", "양품"
                cell(products.filter { it.category == 5 }.sumOf { it.total }.toLong())
                cell(products.filter { it.category == 6 }.sumOf { it.total }.toLong())
                cell(products.filter { it.category == 5 || it.category == 6 }.sumOf { it.error }.toLong())
                cell(products.filter { it.category == 5 || it.category == 6 }.sumOf { it.correct }.toLong())
                // "주방", "기타", "불량", "양품"
                cell(products.filter { it.category == 7 }.sumOf { it.total }.toLong())
                cell(products.filter { it.category == 8 }.sumOf { it.total }.toLong())
                cell(products.filter { it.category == 7 || it.category == 8 }.sumOf { it.error }.toLong())
                cell(products.filter { it.category == 7 || it.category == 8 }.sumOf { it.correct }.toLong())
            }
        })
    }
    row(init = {
        xssfRow.rowNum = 5 + results.size
        cell("${year}년 기증 전체 합계")
        val columnName = listOf(
            "B",
            "C",
            "D",
            "E",
            "F",
            "G",
            "H",
            "I",
            "J",
            "K",
            "L",
            "M",
            "N",
            "O",
            "P",
            "Q",
            "R",
            "S",
            "T",
            "U",
            "V",
            "W",
            "X",
            "Y",
            "Z",
            "AA",
        )
        columnName.forEach {
            cell(Formula("SUM(${it}6:${it}17)"), priceStyle)
        }
    })
}

private fun Sheet.sumTitle(
    lastRow: AtomicInteger,
    indexWeek: AtomicInteger,
    reportCellStyle: ReportCellStyle,
    week: Int
) {
    val sumRow = lastRow.addAndGet(indexWeek.get() + 1)
    xssfSheet.addMergedRegion(CellRangeAddress(sumRow, sumRow, 0, 9))
    row(
        init = {
            xssfRow.rowNum = sumRow
            cell("${week}주차 최종합계", reportCellStyle.totalTitleStyle)
            skip(9, reportCellStyle.totalTitleStyle)
            val columnName = listOf(
                "K",
                "L",
                "M",
                "N",
                "O",
                "P",
                "Q",
                "R",
                "S",
                "T",
                "U",
                "V",
                "W",
                "X",
                "Y",
                "Z",
                "AA",
                "AB",
                "AC",
                "AD",
                "AE",
                "AF",
                "AG",
                "AH",
                "AI"
            )
            columnName.forEach {
                cell(Formula("SUM(${it}${sumRow - indexWeek.get()}:${it}$sumRow)"), reportCellStyle.totalContentStyle)
            }
        }
    )
    lastRow.incrementAndGet()
    indexWeek.set(-1)
}


private fun Row.writeDonationCell(
    no: AtomicInteger,
    index: Int,
    donation: Donate.Donation,
    contributor: Contributor?,
    products: List<Product>,
    reportCellStyle: ReportCellStyle,
) {
    cell(no.getAndIncrement(), reportCellStyle.normalStyle)
    cell(index + 1, reportCellStyle.normalStyle)
    cell(donation.createAt, reportCellStyle.dateStyle)
    cell(contributor?.name?.ifEmpty { "무명" } ?: "무명", reportCellStyle.normalStyle)
    cell(contributor?.phoneNumber.orEmpty(), reportCellStyle.normalStyle)
    cell(contributor?.address.orEmpty(), reportCellStyle.normalStyle)

    val productString = products.mapNotNull {
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
        cell(productString, reportCellStyle.normalStyle)
    } else {
        cell(donation.optionParsingData.orEmpty(), reportCellStyle.normalStyle)
    }
    val org = buildString {
        append(Donate.ORGANIZATION[donation.organization])
        if (donation.member) append(" (교)")
    }
    cell(org, reportCellStyle.normalStyle)
    cell(contributor?.registrationNumber.orEmpty(), reportCellStyle.normalStyle)
    cell(Donate.FROM_TYPE[donation.fromType], reportCellStyle.normalStyle)
    cell(donation.total, reportCellStyle.sumStyle)
    cell(donation.error, reportCellStyle.sumStyle)
    cell(donation.correct, reportCellStyle.sumStyle)
    cell(donation.price.toLong(), reportCellStyle.sumStyle)

    // "의류", "불량", "양품"
    cell(products.filter { it.category == 0 }.sumOf { it.total }
        .takeUnless { it == 0u }?.toInt() ?: "", reportCellStyle.itemCountStyle)
    cell(products.filter { it.category == 0 }.sumOf { it.error }
        .takeUnless { it == 0u }?.toInt() ?: "", reportCellStyle.itemCountStyle)
    cell(products.filter { it.category == 0 }.sumOf { it.correct }
        .takeUnless { it == 0u }?.toInt() ?: "", reportCellStyle.correctItemStyle)

    // "생활", "유아", "불량", "양품"
    cell(products.filter { it.category == 2 }.sumOf { it.total }
        .takeUnless { it == 0u }?.toInt() ?: "", reportCellStyle.itemCountStyle)
    cell(products.filter { it.category == 3 }.sumOf { it.total }
        .takeUnless { it == 0u }?.toInt() ?: "", reportCellStyle.itemCountStyle)
    cell(products.filter { it.category in 2..3 }.sumOf { it.error }
        .takeUnless { it == 0u }?.toInt() ?: "", reportCellStyle.itemCountStyle)
    cell(products.filter { it.category in 2..3 }.sumOf { it.correct }
        .takeUnless { it == 0u }?.toInt() ?: "", reportCellStyle.correctItemStyle)

    // "완구/문구", "불량", "양품"
    cell(products.filter { it.category == 4 }.sumOf { it.total }
        .takeUnless { it == 0u }?.toInt() ?: "", reportCellStyle.itemCountStyle)
    cell(products.filter { it.category == 4 }.sumOf { it.error }
        .takeUnless { it == 0u }?.toInt() ?: "", reportCellStyle.itemCountStyle)
    cell(products.filter { it.category == 4 }.sumOf { it.correct }
        .takeUnless { it == 0u }?.toInt() ?: "", reportCellStyle.correctItemStyle)

    // "도서", "불량", "양품"
    cell(products.filter { it.category == 1 }.sumOf { it.total }
        .takeUnless { it == 0u }?.toInt() ?: "", reportCellStyle.itemCountStyle)
    cell(products.filter { it.category == 1 }.sumOf { it.error }
        .takeUnless { it == 0u }?.toInt() ?: "", reportCellStyle.itemCountStyle)
    cell(products.filter { it.category == 1 }.sumOf { it.correct }
        .takeUnless { it == 0u }?.toInt() ?: "", reportCellStyle.correctItemStyle)

    // "가구", "가전", "불량", "양품"
    cell(products.filter { it.category == 5 }.sumOf { it.total }
        .takeUnless { it == 0u }?.toInt() ?: "", reportCellStyle.itemCountStyle)
    cell(products.filter { it.category == 6 }.sumOf { it.total }
        .takeUnless { it == 0u }?.toInt() ?: "", reportCellStyle.itemCountStyle)
    cell(products.filter { it.category in 5..6 }.sumOf { it.error }
        .takeUnless { it == 0u }?.toInt() ?: "", reportCellStyle.itemCountStyle)
    cell(products.filter { it.category in 5..6 }.sumOf { it.correct }
        .takeUnless { it == 0u }?.toInt() ?: "", reportCellStyle.correctItemStyle)

    // "주방", "기타", "불량", "양품"
    cell(products.filter { it.category == 7 }.sumOf { it.total }
        .takeUnless { it == 0u }?.toInt() ?: "", reportCellStyle.itemCountStyle)
    cell(products.filter { it.category == 8 }.sumOf { it.total }
        .takeUnless { it == 0u }?.toInt() ?: "", reportCellStyle.itemCountStyle)
    cell(products.filter { it.category in 7..8 }.sumOf { it.error }
        .takeUnless { it == 0u }?.toInt() ?: "", reportCellStyle.itemCountStyle)
    cell(products.filter { it.category in 7..8 }.sumOf { it.correct }
        .takeUnless { it == 0u }?.toInt() ?: "", reportCellStyle.correctItemStyle)
}

private fun Row.skip(times: Int = 1, style: XSSFCellStyle? = null) = repeat(times) { cell("", style) }



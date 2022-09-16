package com.acc.goodwill.presentation.report

import com.acc.goodwill.domain.model.Contributor
import com.acc.goodwill.domain.model.Donate
import com.acc.goodwill.domain.model.MonthlyStatics
import com.acc.goodwill.domain.model.Product
import io.github.evanrupert.excelkt.Formula
import io.github.evanrupert.excelkt.Row
import io.github.evanrupert.excelkt.Sheet
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

fun Sheet.monthReport(
    results: List<MonthlyStatics>,
    no: AtomicInteger = AtomicInteger(1),
    lastRow: AtomicInteger = AtomicInteger(5)
) {
    val dateStyle = createCellStyle {
        dataFormat = xssfWorkbook.creationHelper.createDataFormat().getFormat("mm/dd")
    }
    val priceStyle = createCellStyle {
        dataFormat = xssfWorkbook.creationHelper.createDataFormat().getFormat("#,##0")
    }

    results.filter { it.statics.isNotEmpty() }.forEach { month ->
        month.statics.forEachIndexed { index, donationStatics ->
            row(init = {
                xssfRow.rowNum = lastRow.get() + index

                val donation = donationStatics.donation.donate
                val contributor = donationStatics.donation.contributor
                val products = donationStatics.products
                writeDonationCell(no, index, donation, dateStyle, contributor, products, priceStyle)
            })
        }

        val sumRow = lastRow.addAndGet(month.statics.size)
        xssfSheet.addMergedRegion(CellRangeAddress(sumRow, sumRow, 0, 9))
        row(
            init = {
                xssfRow.rowNum = sumRow
                cell(
                    "${month.month}월 최종합계",
                    createCellStyle {
                        setFont(
                            createFont {
                                bold = true
                                fontHeightInPoints = 20
                            }
                        )
                        alignment = HorizontalAlignment.CENTER
                    }
                )
                for (i in 0..8) skip()
                val columnName = listOf("K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "AA", "AB", "AC", "AD", "AE", "AF", "AG", "AH", "AI")
                columnName.forEach {
                    cell(Formula("SUM(${it}${sumRow - month.statics.size + 1}:${it}$sumRow)"), priceStyle)
                }
            }
        )
        lastRow.incrementAndGet()
    }
}

fun Sheet.weeklyReport(
    results: List<MonthlyStatics>,
    no: AtomicInteger = AtomicInteger(1),
    lastRow: AtomicInteger = AtomicInteger(5),
    lastWeek: AtomicInteger = AtomicInteger(-1),
    indexWeek: AtomicInteger = AtomicInteger(-1)
) {
    val dateStyle = createCellStyle {
        dataFormat = xssfWorkbook.creationHelper.createDataFormat().getFormat("mm/dd")
    }
    val priceStyle = createCellStyle {
        dataFormat = xssfWorkbook.creationHelper.createDataFormat().getFormat("#,##0")
    }

    val calendar = Calendar.getInstance()

    results.filter { it.statics.isNotEmpty() }.forEach { month ->
        month.statics.forEach { donationStatics ->
            val donation = donationStatics.donation.donate
            val contributor = donationStatics.donation.contributor
            val products = donationStatics.products

            calendar.set(donation.createAt.year, donation.createAt.monthValue - 1, donation.createAt.dayOfMonth)
            val before = lastWeek.getAndSet(calendar.get(Calendar.WEEK_OF_YEAR))
            if (before != -1 && lastWeek.get() != before) {
                sumTitle(lastRow, indexWeek, priceStyle, before)
            }

            val index = indexWeek.incrementAndGet()
            row(init = {
                xssfRow.rowNum = lastRow.get() + index
                writeDonationCell(no, index, donation, dateStyle, contributor, products, priceStyle)
            })
        }
    }

    if (lastWeek.get() != -1) {
        sumTitle(lastRow, indexWeek, priceStyle, lastWeek.get())
    }

}

private fun Sheet.sumTitle(
    lastRow: AtomicInteger,
    indexWeek: AtomicInteger,
    priceStyle: XSSFCellStyle,
    week: Int
) {
    val sumRow = lastRow.addAndGet(indexWeek.get() + 1)
    xssfSheet.addMergedRegion(CellRangeAddress(sumRow, sumRow, 0, 9))
    row(
        init = {
            xssfRow.rowNum = sumRow
            cell(
                "${week}주차 최종합계",
                createCellStyle {
                    setFont(
                        createFont {
                            bold = true
                            fontHeightInPoints = 20
                        }
                    )
                    alignment = HorizontalAlignment.CENTER
                }
            )
            for (i in 0..8) skip()
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
                cell(Formula("SUM(${it}${sumRow - indexWeek.get()}:${it}$sumRow)"), priceStyle)
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
    dateStyle: XSSFCellStyle,
    contributor: Contributor?,
    products: List<Product>,
    priceStyle: XSSFCellStyle
) {
    cell(no.getAndIncrement())
    cell(index + 1)
    cell(donation.createAt, dateStyle)
    cell(contributor?.name?.ifEmpty { "무명" } ?: "무명")
    cell(contributor?.phoneNumber.orEmpty())
    cell(contributor?.address.orEmpty())

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
    cell(products.filter { it.category == 0 }.sumOf { it.total }
        .takeUnless { it == 0u }?.toInt() ?: "")
    cell(products.filter { it.category == 0 }.sumOf { it.error }
        .takeUnless { it == 0u }?.toInt() ?: "")
    cell(products.filter { it.category == 0 }.sumOf { it.correct }
        .takeUnless { it == 0u }?.toInt() ?: "")

    // "생활", "유아", "불량", "양품"
    cell(products.filter { it.category == 2 }.sumOf { it.total }
        .takeUnless { it == 0u }?.toInt() ?: "")
    cell(products.filter { it.category == 3 }.sumOf { it.total }
        .takeUnless { it == 0u }?.toInt() ?: "")
    cell(products.filter { it.category in 2..3 }.sumOf { it.error }
        .takeUnless { it == 0u }?.toInt() ?: "")
    cell(products.filter { it.category in 2..3 }.sumOf { it.correct }
        .takeUnless { it == 0u }?.toInt() ?: "")

    // "완구/문구", "불량", "양품"
    cell(products.filter { it.category == 4 }.sumOf { it.total }
        .takeUnless { it == 0u }?.toInt() ?: "")
    cell(products.filter { it.category == 4 }.sumOf { it.error }
        .takeUnless { it == 0u }?.toInt() ?: "")
    cell(products.filter { it.category == 4 }.sumOf { it.correct }
        .takeUnless { it == 0u }?.toInt() ?: "")

    // "도서", "불량", "양품"
    cell(products.filter { it.category == 1 }.sumOf { it.total }
        .takeUnless { it == 0u }?.toInt() ?: "")
    cell(products.filter { it.category == 1 }.sumOf { it.error }
        .takeUnless { it == 0u }?.toInt() ?: "")
    cell(products.filter { it.category == 1 }.sumOf { it.correct }
        .takeUnless { it == 0u }?.toInt() ?: "")

    // "가구", "가전", "불량", "양품"
    cell(products.filter { it.category == 5 }.sumOf { it.total }
        .takeUnless { it == 0u }?.toInt() ?: "")
    cell(products.filter { it.category == 6 }.sumOf { it.total }
        .takeUnless { it == 0u }?.toInt() ?: "")
    cell(products.filter { it.category in 5..6 }.sumOf { it.error }
        .takeUnless { it == 0u }?.toInt() ?: "")
    cell(products.filter { it.category in 5..6 }.sumOf { it.correct }
        .takeUnless { it == 0u }?.toInt() ?: "")

    // "주방", "기타", "불량", "양품"
    cell(products.filter { it.category == 7 }.sumOf { it.total }
        .takeUnless { it == 0u }?.toInt() ?: "")
    cell(products.filter { it.category == 8 }.sumOf { it.total }
        .takeUnless { it == 0u }?.toInt() ?: "")
    cell(products.filter { it.category in 7..8 }.sumOf { it.error }
        .takeUnless { it == 0u }?.toInt() ?: "")
    cell(products.filter { it.category in 7..8 }.sumOf { it.correct }
        .takeUnless { it == 0u }?.toInt() ?: "")
}

private fun Row.skip() = cell("")


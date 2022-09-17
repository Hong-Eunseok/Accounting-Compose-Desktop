package com.acc.goodwill.presentation.report

import com.acc.goodwill.domain.model.Contributor
import com.acc.goodwill.domain.model.Donate
import com.acc.goodwill.domain.model.MonthlyStatics
import com.acc.goodwill.domain.model.Product
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
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.atomic.AtomicInteger


private val borderStyle = BorderStyle.THIN

fun Sheet.monthReport(
    results: List<MonthlyStatics>,
    no: AtomicInteger = AtomicInteger(1),
    lastRow: AtomicInteger = AtomicInteger(5)
) {
    val dateStyle = cellStyle(format = "mm/dd", alignment = HorizontalAlignment.CENTER)
    val priceStyle = cellStyle(format = "#,##0", alignment = HorizontalAlignment.CENTER)

    val titleCellStyle = cellStyle(
        bold = true,
        heightInPoints = 20f,
        alignment = HorizontalAlignment.CENTER,
        indexColor = IndexedColors.LIGHT_YELLOW
    )
    val titleContentCell = cellStyle(
        bold = true,
        heightInPoints = 14f,
        alignment = HorizontalAlignment.CENTER,
        indexColor = IndexedColors.LIGHT_YELLOW,
        format = "#,##0"
    )

    val centerStyle = cellStyle(alignment = HorizontalAlignment.CENTER)

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
                    writeDonationCell(no, index, donation, dateStyle, contributor, products, priceStyle, centerStyle)
                }
            )
        }

        val sumRow = lastRow.addAndGet(month.statics.size)
        xssfSheet.addMergedRegion(CellRangeAddress(sumRow, sumRow, 0, 9))
        row(
            init = {
                xssfRow.rowNum = sumRow
                cell("${month.month}월 최종합계", titleCellStyle)
                skip(9, titleCellStyle)
                val columnName = listOf("K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "AA", "AB", "AC", "AD", "AE", "AF", "AG", "AH", "AI")
                columnName.forEach {
                    cell(Formula("SUM(${it}${sumRow - month.statics.size + 1}:${it}$sumRow)"), titleContentCell)
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
    val dateStyle = cellStyle(format = "mm/dd", alignment = HorizontalAlignment.CENTER)
    val priceStyle = cellStyle(format = "#,##0", alignment = HorizontalAlignment.CENTER)
    val centerStyle = cellStyle(alignment = HorizontalAlignment.CENTER)

    val calendar = Calendar.getInstance()

    val titleCellStyle = cellStyle(
        bold = true,
        heightInPoints = 20f,
        alignment = HorizontalAlignment.CENTER,
        indexColor = IndexedColors.LIGHT_YELLOW
    )
    val titleContentCell = cellStyle(
        bold = true,
        heightInPoints = 14f,
        alignment = HorizontalAlignment.CENTER,
        indexColor = IndexedColors.LIGHT_YELLOW,
        format = "#,##0"
    )

    results.filter { it.statics.isNotEmpty() }.forEach { month ->
        month.statics.forEach { donationStatics ->
            val donation = donationStatics.donation.donate
            val contributor = donationStatics.donation.contributor
            val products = donationStatics.products

            calendar.set(donation.createAt.year, donation.createAt.monthValue - 1, donation.createAt.dayOfMonth)
            val before = lastWeek.getAndSet(calendar.get(Calendar.WEEK_OF_YEAR))
            if (before != -1 && lastWeek.get() != before) {
                sumTitle(lastRow, indexWeek, titleCellStyle, titleContentCell, before)
            }

            val index = indexWeek.incrementAndGet()
            row(init = {
                xssfRow.rowNum = lastRow.get() + index
                writeDonationCell(no, index, donation, dateStyle, contributor, products, priceStyle, centerStyle)
            })
        }
    }

    if (lastWeek.get() != -1) {
        sumTitle(lastRow, indexWeek, titleCellStyle, titleContentCell, lastWeek.get())
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

private fun ExcelElement.cellStyle(
    bold: Boolean = false,
    alignment: HorizontalAlignment = HorizontalAlignment.GENERAL,
    heightInPoints: Float = 11f,
    indexColor: IndexedColors? = null,
    format: String? = null
) = createCellStyle {
    setFont(
        createFont {
            this.bold = bold
            this.fontHeightInPoints = heightInPoints.toInt().toShort()
        }
    )
    this.alignment = alignment
    if (indexColor != null) {
        fillForegroundColor = indexColor.index
        fillPattern = FillPatternType.THICK_FORWARD_DIAG
    }
    borderTop = borderStyle
    borderLeft = borderStyle
    borderBottom = borderStyle
    borderRight= borderStyle

    if (format != null) {
        dataFormat = xssfWorkbook.creationHelper.createDataFormat().getFormat(format)
    }
}

private fun ExcelElement.boldCenterStyle() = cellStyle(bold = true, alignment = HorizontalAlignment.CENTER)
private fun ExcelElement.paleBlue() = cellStyle(bold = false, alignment = HorizontalAlignment.CENTER, indexColor = IndexedColors.PALE_BLUE)
private fun ExcelElement.lightTurquoise() = cellStyle(bold = true, alignment = HorizontalAlignment.CENTER, indexColor = IndexedColors.LIGHT_TURQUOISE)
private fun ExcelElement.tan() = cellStyle(bold = true, alignment = HorizontalAlignment.CENTER, indexColor = IndexedColors.TAN)

private fun ExcelElement.white() = cellStyle(bold = true, alignment = HorizontalAlignment.CENTER, indexColor = IndexedColors.LIGHT_CORNFLOWER_BLUE)
fun Sheet.header(year: Int) {
    xssfSheet.addMergedRegion(CellRangeAddress(3, 3, 1, 5))
    xssfSheet.addMergedRegion(CellRangeAddress(3, 3, 10, 13))
    xssfSheet.addMergedRegion(CellRangeAddress(3, 3, 14, 16))
    xssfSheet.addMergedRegion(CellRangeAddress(3, 3, 17, 20))
    xssfSheet.addMergedRegion(CellRangeAddress(3, 3, 21, 26))
    xssfSheet.addMergedRegion(CellRangeAddress(3, 3, 27, 34))

    val paleBlue = paleBlue().copy().apply { setFont(createFont { bold = true }) }
    val tan = tan()
    val bold = cellStyle(bold = true)

    val dateFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd")
    val now = LocalDateTime.now()
    val beforeDate = now.withYear(year).withMonth(1).withDayOfMonth(1).run { dateFormat.format(this) }
    val afterDate = now.withYear(year).withMonth(12).withDayOfMonth(31).run { dateFormat.format(this) }
    row(
        init = {
            xssfRow.rowNum = 3
            skip()
            cell("${beforeDate}~${afterDate}", boldCenterStyle())
            skip(4, boldCenterStyle())
            cell("수원굿윌스토어", bold)
            skip(3)
            cell("전체합계", tan)
            skip(3, tan)
            cell("의류", paleBlue)
            skip(2, paleBlue)
            cell("잡화", paleBlue)
            skip(3, paleBlue)
            cell("문구완구도서", paleBlue)
            skip(5, paleBlue)
            cell("가구/가전/주방/기타", paleBlue)
            skip(7, paleBlue)
        }
    )
}
fun Sheet.header(headings: List<String>) {
    val white = white()
    row(init = {
        xssfRow.rowNum = 4
        headings.forEach { cell(it, white) }
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
    titleStyle: XSSFCellStyle,
    priceStyle: XSSFCellStyle,
    week: Int
) {
    val sumRow = lastRow.addAndGet(indexWeek.get() + 1)
    xssfSheet.addMergedRegion(CellRangeAddress(sumRow, sumRow, 0, 9))
    row(
        init = {
            xssfRow.rowNum = sumRow
            cell("${week}주차 최종합계", titleStyle)
            skip(9, titleStyle)
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
    priceStyle: XSSFCellStyle,
    centerStyle: XSSFCellStyle,
) {
    cell(no.getAndIncrement(), centerStyle)
    cell(index + 1, centerStyle)
    cell(donation.createAt, dateStyle)
    cell(contributor?.name?.ifEmpty { "무명" } ?: "무명", centerStyle)
    cell(contributor?.phoneNumber.orEmpty(), centerStyle)
    cell(contributor?.address.orEmpty())

    val paleBlue = paleBlue()
    val lightTurquoise = lightTurquoise()
    val tan = tan()

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
    cell(org, centerStyle)
    cell(contributor?.registrationNumber.orEmpty(), centerStyle)
    cell(Donate.FROM_TYPE[donation.fromType], centerStyle)

    val tanPrice = priceStyle.copy().apply {
        setFont(
            createFont {
                bold = true
                fontHeightInPoints = 11.toShort()
            }
        )
        alignment = HorizontalAlignment.CENTER
        fillForegroundColor = IndexedColors.TAN.index
        fillPattern = FillPatternType.THICK_FORWARD_DIAG
        borderTop = borderStyle
        borderLeft = borderStyle
        borderBottom = borderStyle
        borderRight= borderStyle
    }

    cell(donation.total, tan)
    cell(donation.error, tan)
    cell(donation.correct, tan)
    cell(donation.price.toLong(), tanPrice)

    // "의류", "불량", "양품"
    cell(products.filter { it.category == 0 }.sumOf { it.total }
        .takeUnless { it == 0u }?.toInt() ?: "", paleBlue)
    cell(products.filter { it.category == 0 }.sumOf { it.error }
        .takeUnless { it == 0u }?.toInt() ?: "", paleBlue)
    cell(products.filter { it.category == 0 }.sumOf { it.correct }
        .takeUnless { it == 0u }?.toInt() ?: "", lightTurquoise)

    // "생활", "유아", "불량", "양품"
    cell(products.filter { it.category == 2 }.sumOf { it.total }
        .takeUnless { it == 0u }?.toInt() ?: "", paleBlue)
    cell(products.filter { it.category == 3 }.sumOf { it.total }
        .takeUnless { it == 0u }?.toInt() ?: "", paleBlue)
    cell(products.filter { it.category in 2..3 }.sumOf { it.error }
        .takeUnless { it == 0u }?.toInt() ?: "", paleBlue())
    cell(products.filter { it.category in 2..3 }.sumOf { it.correct }
        .takeUnless { it == 0u }?.toInt() ?: "", lightTurquoise)

    // "완구/문구", "불량", "양품"
    cell(products.filter { it.category == 4 }.sumOf { it.total }
        .takeUnless { it == 0u }?.toInt() ?: "", paleBlue)
    cell(products.filter { it.category == 4 }.sumOf { it.error }
        .takeUnless { it == 0u }?.toInt() ?: "", paleBlue)
    cell(products.filter { it.category == 4 }.sumOf { it.correct }
        .takeUnless { it == 0u }?.toInt() ?: "", lightTurquoise)

    // "도서", "불량", "양품"
    cell(products.filter { it.category == 1 }.sumOf { it.total }
        .takeUnless { it == 0u }?.toInt() ?: "", paleBlue)
    cell(products.filter { it.category == 1 }.sumOf { it.error }
        .takeUnless { it == 0u }?.toInt() ?: "", paleBlue)
    cell(products.filter { it.category == 1 }.sumOf { it.correct }
        .takeUnless { it == 0u }?.toInt() ?: "", lightTurquoise)

    // "가구", "가전", "불량", "양품"
    cell(products.filter { it.category == 5 }.sumOf { it.total }
        .takeUnless { it == 0u }?.toInt() ?: "", paleBlue)
    cell(products.filter { it.category == 6 }.sumOf { it.total }
        .takeUnless { it == 0u }?.toInt() ?: "", paleBlue)
    cell(products.filter { it.category in 5..6 }.sumOf { it.error }
        .takeUnless { it == 0u }?.toInt() ?: "", paleBlue)
    cell(products.filter { it.category in 5..6 }.sumOf { it.correct }
        .takeUnless { it == 0u }?.toInt() ?: "", lightTurquoise)

    // "주방", "기타", "불량", "양품"
    cell(products.filter { it.category == 7 }.sumOf { it.total }
        .takeUnless { it == 0u }?.toInt() ?: "", paleBlue)
    cell(products.filter { it.category == 8 }.sumOf { it.total }
        .takeUnless { it == 0u }?.toInt() ?: "", paleBlue)
    cell(products.filter { it.category in 7..8 }.sumOf { it.error }
        .takeUnless { it == 0u }?.toInt() ?: "", paleBlue)
    cell(products.filter { it.category in 7..8 }.sumOf { it.correct }
        .takeUnless { it == 0u }?.toInt() ?: "", lightTurquoise)
}

private fun Row.skip(times: Int = 1, style: XSSFCellStyle? = null) = repeat(times) { cell("", style) }



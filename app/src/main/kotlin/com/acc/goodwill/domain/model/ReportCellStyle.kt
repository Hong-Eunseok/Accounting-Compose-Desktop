package com.acc.goodwill.domain.model

import io.github.evanrupert.excelkt.ExcelElement
import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.xssf.usermodel.XSSFCellStyle

data class ReportCellStyle(
    val normalStyle: XSSFCellStyle,
    val planeStyle: XSSFCellStyle,
    val dateStyle: XSSFCellStyle,
    val normalPriceStyle: XSSFCellStyle,
    val totalTitleStyle: XSSFCellStyle,
    val totalContentStyle: XSSFCellStyle,
    val sumStyle: XSSFCellStyle,
    val itemCountStyle: XSSFCellStyle,
    val correctItemStyle: XSSFCellStyle,
    val boldStyle: XSSFCellStyle,
    val categoryStyle: XSSFCellStyle,
    val headerStyle: XSSFCellStyle,
) {
    companion object {
        fun instance(sheet: ExcelElement): ReportCellStyle {
            return ReportCellStyle(
                normalStyle = cellStyle(sheet),
                planeStyle = cellStyle(sheet, alignment = HorizontalAlignment.GENERAL),
                dateStyle = cellStyle(sheet, format = "mm/dd"),
                normalPriceStyle = cellStyle(sheet, format = "#,##0"),
                totalTitleStyle = cellStyle(
                    sheet,
                    bold = true,
                    heightInPoints = 20f,
                    indexColor = IndexedColors.LIGHT_YELLOW
                ),
                totalContentStyle = cellStyle(
                    sheet,
                    bold = true,
                    heightInPoints = 14f,
                    indexColor = IndexedColors.LIGHT_YELLOW,
                    format = "#,##0"
                ),
                sumStyle = cellStyle(
                    sheet,
                    bold = true,
                    indexColor = IndexedColors.TAN,
                    format = "#,##0"
                ),
                itemCountStyle = cellStyle(
                    sheet,
                    indexColor = IndexedColors.PALE_BLUE,
                ),
                correctItemStyle = cellStyle(
                    sheet,
                    bold = true,
                    indexColor = IndexedColors.LIGHT_TURQUOISE,
                ),
                boldStyle = cellStyle(sheet, bold = true),
                categoryStyle = cellStyle(
                    sheet,
                    bold = true,
                    indexColor = IndexedColors.PALE_BLUE,
                ),
                headerStyle = cellStyle(
                    sheet,
                    bold = true,
                    indexColor = IndexedColors.LIGHT_CORNFLOWER_BLUE,
                ),
            )
        }
    }
}

private fun cellStyle(
    sheet: ExcelElement,
    bold: Boolean = false,
    alignment: HorizontalAlignment = HorizontalAlignment.CENTER,
    heightInPoints: Float = 11f,
    indexColor: IndexedColors? = null,
    format: String? = null,
    borderStyle: BorderStyle = BorderStyle.THIN
) = sheet.createCellStyle {
    setFont(
        sheet.createFont {
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
    borderRight = borderStyle

    if (format != null) {
        dataFormat = sheet.xssfWorkbook.creationHelper.createDataFormat().getFormat(format)
    }
}

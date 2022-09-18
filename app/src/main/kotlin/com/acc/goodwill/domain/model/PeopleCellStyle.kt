package com.acc.goodwill.domain.model

import io.github.evanrupert.excelkt.ExcelElement
import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.xssf.usermodel.XSSFCellStyle

data class PeopleCellStyle(
    val titleStyle: XSSFCellStyle,
    val centerStyle: XSSFCellStyle,
    val generalStyle: XSSFCellStyle,
    val dateStyle: XSSFCellStyle,
) {
    companion object {
        fun instance(sheet: ExcelElement): PeopleCellStyle {
            return PeopleCellStyle(
                titleStyle = cellStyle(
                    sheet = sheet,
                    borderStyle = BorderStyle.NONE,
                    bold = true,
                    heightInPoints = 28f
                ),
                centerStyle = cellStyle(sheet),
                generalStyle = cellStyle(sheet, alignment = HorizontalAlignment.GENERAL),
                dateStyle = cellStyle(sheet, bold = true, format = "mm/dd")
            )
        }
    }
}

private fun cellStyle(
    sheet: ExcelElement,
    bold: Boolean = true,
    alignment: HorizontalAlignment = HorizontalAlignment.CENTER,
    heightInPoints: Float = 11f,
    indexColor: IndexedColors? = null,
    format: String? = null,
    borderStyle: BorderStyle = BorderStyle.DOTTED
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


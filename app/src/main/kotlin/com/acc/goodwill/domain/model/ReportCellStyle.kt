package com.acc.goodwill.domain.model

import org.apache.poi.xssf.usermodel.XSSFCellStyle

data class ReportCellStyle(
    val normalStyle: XSSFCellStyle,
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
)
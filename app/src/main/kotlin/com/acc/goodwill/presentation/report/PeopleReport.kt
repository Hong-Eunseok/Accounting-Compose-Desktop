package com.acc.goodwill.presentation.report

import com.acc.goodwill.domain.model.Donate
import com.acc.goodwill.domain.model.DonationStatics
import com.acc.goodwill.domain.model.PeopleCellStyle
import io.github.evanrupert.excelkt.Sheet
import org.apache.poi.ss.util.CellRangeAddress

fun Sheet.peopleTitle(year: Int, month: Int, peopleCellStyle: PeopleCellStyle) {
    xssfSheet.addMergedRegion(CellRangeAddress(0, 0, 0, 8))
    row {
        cell(
            "${year}년 ${month}월 기증자 명단 리스트",
            peopleCellStyle.titleStyle
        )
        xssfRow.heightInPoints = 36f
    }
}

fun Sheet.goodwillstoreHeader(peopleCellStyle: PeopleCellStyle) {
    xssfSheet.addMergedRegion(CellRangeAddress(2, 2, 0, 4))
    row(init = {
        xssfRow.rowNum = 2
        cell("수원굿윌스토어 도네이션센터", peopleCellStyle.generalStyle)
    })
}

fun Sheet.peopleHeader(peopleCellStyle: PeopleCellStyle) {
    row(init = {
        xssfRow.rowNum = 3
        listOf("NO", "날 짜", "성 명", "핸드폰번호", "주    소", "기증품목 / 수량", "개인/기관/교인", "발행여부", "기증통로\n(수거.기증)").forEach {
            cell(it, peopleCellStyle.centerStyle)
        }
        xssfRow.heightInPoints = 25f
    })
}

fun Sheet.writhePeopleContent(donations: List<DonationStatics>, style: PeopleCellStyle) {
    donations.forEachIndexed { index, donationStatics ->
        val donation = donationStatics.donation.donate
        val products = donationStatics.products
        val contributor = donationStatics.donation.contributor
        row(init = {
            xssfRow.rowNum = 4 + index
            cell("${index + 1}", style.centerStyle)
            cell(donation.createAt, style.dateStyle)
            cell(contributor?.name ?: "무명", style.centerStyle)
            cell(contributor?.phoneNumber.orEmpty(), style.centerStyle)
            cell(contributor?.address.orEmpty(), style.generalStyle)

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
                cell(productString, style.generalStyle)
            } else {
                cell(donation.optionParsingData.orEmpty(), style.generalStyle)
            }

            val org = buildString {
                append(Donate.ORGANIZATION[donation.organization])
                if (donation.member) append(" (교)")
            }
            cell(org, style.centerStyle)
            cell(contributor?.registrationNumber.orEmpty(), style.centerStyle)
            cell(Donate.FROM_TYPE[donation.fromType], style.centerStyle)
        })
    }
    xssfSheet.setColumnWidth(0, (xssfSheet.getColumnWidth(0) / 2))
    xssfSheet.setColumnWidth(3, xssfSheet.getColumnWidth(3) * 2)
    xssfSheet.setColumnWidth(4, xssfSheet.getColumnWidth(4) * 7)
    xssfSheet.setColumnWidth(5, xssfSheet.getColumnWidth(5) * 7)
    xssfSheet.setColumnWidth(6, xssfSheet.getColumnWidth(6) * 2)
}
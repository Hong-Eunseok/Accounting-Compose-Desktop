package com.acc.goodwill.domain.model

import androidx.compose.runtime.*
import kotlin.math.floor

@Composable
fun rememberContributor(): CreateContributorState {
    return remember { CreateContributorState() }
}

class CreateContributorState {
    var name by mutableStateOf("")
    var phoneNumber by mutableStateOf("")
    var address by mutableStateOf("")
    var registrationNumber by mutableStateOf("")

    val valid by derivedStateOf {
        when {
            name.isEmpty() -> Validator.NAME_ERROR
            phoneNumberValidator() -> Validator.PHONE_NUMBER_ERROR
            registrationNumberValidator() -> Validator.REGISTRATION_NUMBER_ERROR
            else -> Validator.VALID
        }
    }

    private fun phoneNumberValidator(): Boolean {
        return when {
            phoneNumber.isEmpty() -> false
            "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$".toRegex().matches(phoneNumber) -> false
            else -> true
        }
    }

    private fun registrationNumberValidator(): Boolean {
        return when {
            registrationNumber.isEmpty() -> false
            (isBisNum() || isCorporNum() || isRsdnNum()) -> false
            else -> true
        }
    }

    enum class Validator {
        VALID, NAME_ERROR, PHONE_NUMBER_ERROR, REGISTRATION_NUMBER_ERROR
    }

    private fun isBisNum(): Boolean {
        val strBisNum = registrationNumber.replace("\\D".toRegex(), "")
        if (strBisNum.length != 10) return false

        val share =
            (floor((Integer.parseInt(strBisNum.substring(8, 9)) * 5).toDouble()) / 10).toInt()
        val rest = (Integer.parseInt(strBisNum.substring(8, 9)) * 5) % 10

        val sum = (Integer.parseInt(strBisNum.substring(0, 1))) +
                ((Integer.parseInt(strBisNum.substring(1, 2)) * 3) % 10) +
                ((Integer.parseInt(strBisNum.substring(2, 3)) * 7) % 10) +
                ((Integer.parseInt(strBisNum.substring(3, 4)) * 1) % 10) +
                ((Integer.parseInt(strBisNum.substring(4, 5)) * 3) % 10) +
                ((Integer.parseInt(strBisNum.substring(5, 6)) * 7) % 10) +
                ((Integer.parseInt(strBisNum.substring(6, 7)) * 1) % 10) +
                ((Integer.parseInt(strBisNum.substring(7, 8)) * 3) % 10) +
                (Integer.parseInt(strBisNum.substring(9, 10))) + share + rest

        return sum % 10 == 0
    }

    private fun isCorporNum(): Boolean {
        val strCorporNum = registrationNumber.replace("\\D".toRegex(), "")
        if (strCorporNum.length != 13) return false
        var sum = 0f
        for (i in 0..11) sum += (i % 2 + 1) * strCorporNum[i].toString().toFloat()
        return strCorporNum.substring(12, 13).toFloat() == 10 - sum % 10 % 10
    }

    private fun isRsdnNum(): Boolean {
        val strRsdnNum = registrationNumber.replace("\\D".toRegex(), "")
        if (strRsdnNum.length != 13) return false

        val mm = Integer.parseInt(strRsdnNum.substring(2, 4))
        val dd = Integer.parseInt(strRsdnNum.substring(4, 6))
        if (mm > 12 || mm < 1 || dd < 1 || dd > 31) return false

        val sum = (strRsdnNum.substring(0, 1).toFloat() * 2) +
                (strRsdnNum.substring(1, 2).toFloat() * 3) +
                (strRsdnNum.substring(2, 3).toFloat() * 4) +
                (strRsdnNum.substring(3, 4).toFloat() * 5) +
                (strRsdnNum.substring(4, 5).toFloat() * 6) +
                (strRsdnNum.substring(5, 6).toFloat() * 7) +
                (strRsdnNum.substring(6, 7).toFloat() * 8) +
                (strRsdnNum.substring(7, 8).toFloat() * 9) +
                (strRsdnNum.substring(8, 9).toFloat() * 2) +
                (strRsdnNum.substring(9, 10).toFloat() * 3) +
                (strRsdnNum.substring(10, 11).toFloat() * 4) +
                (strRsdnNum.substring(11, 12).toFloat() * 5)

        return strRsdnNum.substring(12, 13).toFloat() == (11 - (sum % 11)) % 10
    }

}


package com.jcs.suadeome.professionals

import com.jcs.suadeome.generators.EntityType
import com.jcs.suadeome.types.EntityConstructionFailed

data class PhoneNumber(private val _number: String) {

    val number = _number.replace("\\s+".toRegex(), "")

    init {
        if (number.isBlank() || !isNumeric(number)) {
            throw EntityConstructionFailed(EntityType.PHONE_NUMBER, number)
        }
    }

    private fun isNumeric(n: String): Boolean {
        return n.matches("^\\d+$".toRegex())
    }

}



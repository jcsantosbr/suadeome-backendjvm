package com.jcs.suadeome.professionals

import com.jcs.suadeome.generators.EntityType
import com.jcs.suadeome.types.EntityConstructionFailed
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test

class PhoneNumberTest {

    @Test
    fun shouldThrowExceptionForInvalidPhoneNumbers() {
        val invalidInputs = listOf("123abc", "abc", "123 456", "kj")

        invalidInputs.forEach { invalidInput ->
            try {
                PhoneNumber(invalidInput)
            } catch (e: EntityConstructionFailed) {
                assertThat(e.entityType, equalTo(EntityType.PHONE_NUMBER))
                assertThat(e.invalidValue, equalTo(invalidInput))
            }
        }
    }

    @Test
    fun shouldOnlyAcceptPhoneNumbersWithNumbers() {
        val validInput = listOf("123", "01151029090", "123456")

        validInput.forEach { validInput ->
            assertThat(PhoneNumber(validInput).number, equalTo(validInput))
        }
    }

    @Test
    fun numbersWithSpacesAreAcceptedButNormalized() {
        val input = "123 456"
        assertThat(PhoneNumber(input).number, equalTo("123456"))
    }

}

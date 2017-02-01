package com.jcs.suadeome.professionals

import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Test

class ServiceTest {

    @Test
    fun shouldNormalizeServiceNameToGenerateId() {
        val outputByInput: Map<String, String> = mapOf(
                "commonService" to "commonservice",
                "áéíóúÁÉÍÓÚça" to "aeiouaeiouca",
                "service with space" to "servicewithspace"
        )

        outputByInput.entries.forEach { entry ->
            Assert.assertThat(Service(entry.key).id, CoreMatchers.equalTo(entry.value))
        }
    }
}


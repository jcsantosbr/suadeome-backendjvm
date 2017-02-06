package com.jcs.suadeome.professionals

import com.jcs.suadeome.services.Service
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
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
            assertThat(Service.normalizeName(entry.key), equalTo(entry.value))
        }
    }
}


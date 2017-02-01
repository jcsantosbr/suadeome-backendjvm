package com.jcs.suadeome.professionals

import com.jcs.suadeome.generators.EntityType
import com.jcs.suadeome.types.EntityConstructionFailed
import java.text.Normalizer.Form.NFD
import java.text.Normalizer.normalize

data class Service(val name: String) {

    val id = normalizeName(name)

    init {
        if (id.isBlank()) {
            throw EntityConstructionFailed(EntityType.SERVICE, name)
        }
    }

    private fun normalizeName(name: String): String {
        return normalize(name, NFD)
                .replace("\\W".toRegex(), "")
                .toLowerCase()
    }
}



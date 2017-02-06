package com.jcs.suadeome.services

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.jcs.suadeome.generators.EntityType
import com.jcs.suadeome.types.EntityConstructionFailed
import com.jcs.suadeome.types.Id
import java.lang.reflect.Type
import java.text.Normalizer.Form.NFD
import java.text.Normalizer.normalize

data class Service protected constructor(val id: Id, val normalizedName: String, val originalName: String) {

    init {
        if (normalizedName.isBlank()) {
            throw EntityConstructionFailed(EntityType.SERVICE, originalName)
        }
    }

    companion object {
        fun service(id: Id, name: String, normalizedName: String = ""): Service {
            val _normalizedName =
                    if (normalizedName.isBlank()) normalizeName(name)
                    else normalizedName

            return Service(id, _normalizedName, name)
        }

        fun normalizeName(name: String): String {
            return normalize(name, NFD)
                    .replace("\\W".toRegex(), "")
                    .toLowerCase()
        }

    }

    class ServiceJsonSerializer : JsonSerializer<Service> {
        override fun serialize(src: Service, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
            val obj = JsonObject();
            obj.addProperty("type", src.javaClass.simpleName)
            obj.addProperty("id", src.id.value)
            obj.addProperty("normalized_name", src.normalizedName)
            obj.addProperty("original_name", src.originalName)
            return obj
        }
    }
}


